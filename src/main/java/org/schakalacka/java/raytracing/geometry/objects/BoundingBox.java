package org.schakalacka.java.raytracing.geometry.objects;

import org.schakalacka.java.raytracing.Constants;
import org.schakalacka.java.raytracing.geometry.tracing.Ray;
import org.schakalacka.java.raytracing.math.Matrix;
import org.schakalacka.java.raytracing.math.RTPoint;
import org.schakalacka.java.raytracing.math.Tuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoundingBox {

    private final Map<Matrix, List<Tuple>> transformedCorners = new HashMap<>();
    private final List<Tuple> corners;

    private final RTPoint lower;
    private final RTPoint upper;

    public BoundingBox(RTPoint lower, RTPoint upper) {
        this.lower = lower;
        this.upper = upper;
        corners = List.of(Tuple.point(lower.x(), lower.y(), lower.z()), Tuple.point(lower.x(), lower.y(), upper.z()), Tuple.point(upper.x(), lower.y(), upper.z()), Tuple.point(upper.x(), lower.y(), lower.z()), Tuple.point(lower.x(), upper.y(), lower.z()), Tuple.point(lower.x(), upper.y(), upper.z()), Tuple.point(upper.x(), upper.y(), upper.z()), Tuple.point(upper.x(), upper.y(), lower.z()));
    }

    public RTPoint lower() {
        return lower;
    }

    public RTPoint upper() {
        return upper;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoundingBox boundingBox = (BoundingBox) o;
        return lower.equals(boundingBox.lower) && upper.equals(boundingBox.upper);
    }

    @Override
    public String toString() {
        return "BoundingBox{" + "lower=" + lower + ", upper=" + upper + '}';
    }

    public BoundingBox getTransformedBounds(Matrix scaleMatrix) {
        var transformedCorners = getTransformedCorners(scaleMatrix);
        double minX, minY, minZ, maxX, maxY, maxZ;
        minX = minY = minZ = maxX = maxY = maxZ = 0;

        for (Tuple corner : transformedCorners) {
            minX = Math.min(minX, corner.x());
            minY = Math.min(minY, corner.y());
            minZ = Math.min(minZ, corner.z());
            maxX = Math.max(maxX, corner.x());
            maxY = Math.max(maxY, corner.y());
            maxZ = Math.max(maxZ, corner.z());

        }

        return new BoundingBox(Tuple.point(minX, minY, minZ), Tuple.point(maxX, maxY, maxZ));
    }

    /***
     * Returns the corners of the bounds. Starting with the lower left front corner and going clockwise
     * @return a non-null list of 8 tuples
     */
    public List<Tuple> getCorners() {
        return corners;
    }

    public List<Tuple> getTransformedCorners(Matrix matrix) {
        return transformedCorners.computeIfAbsent(matrix, k -> {
            var originalCornerList = getCorners();
            var transformedCorners = new ArrayList<Tuple>();

            for (Tuple corner : originalCornerList) {
                transformedCorners.add(matrix.mulT(corner));
            }
            return transformedCorners;
        });


    }

    public boolean contains(RTPoint point) {
        // first, test for infinities
        final boolean xInfinityLowerMatch = point.x() <= Constants.NEGATIVE_INFINITY && lower.x() <= Constants.NEGATIVE_INFINITY;
        final boolean xInfinityUpperMatch = point.x() >= Constants.POSITIVE_INFINITY && upper.x() >= Constants.POSITIVE_INFINITY;
        final boolean yInfinityLowerMatch = point.y() <= Constants.NEGATIVE_INFINITY && lower.y() <= Constants.NEGATIVE_INFINITY;
        final boolean yInfinityUpperMatch = point.y() >= Constants.POSITIVE_INFINITY && upper.y() >= Constants.POSITIVE_INFINITY;
        final boolean zInfinityLowerMatch = point.z() <= Constants.NEGATIVE_INFINITY && lower.z() <= Constants.NEGATIVE_INFINITY;
        final boolean zInfinityUpperMatch = point.z() >= Constants.POSITIVE_INFINITY && upper.z() >= Constants.POSITIVE_INFINITY;

        return (point.x() >= lower.x() || xInfinityLowerMatch)
                && (point.x() <= upper.x() || xInfinityUpperMatch)
                && (point.y() >= lower.y() ||   yInfinityLowerMatch)
                && (point.y() <= upper.y()  ||  yInfinityUpperMatch)
                && (point.z() >= lower.z() ||  zInfinityLowerMatch)
                && (point.z() <= upper.z() ||  zInfinityUpperMatch);

    }

    public boolean contains(BoundingBox otherBox) {
        return contains(otherBox.lower()) && contains(otherBox.upper());
    }

    public boolean intersects(Ray ray) {
        double[] xminMax = checkAxis(lower.x(), upper.x(),ray.origin().x(), ray.direction().x());
        double[] yminMax = checkAxis(lower.y(), upper.y(),ray.origin().y(), ray.direction().y());
        double[] zminMax = checkAxis(lower.z(), upper.z(), ray.origin().z(), ray.direction().z());

        double tmin = Math.max(xminMax[0], Math.max(yminMax[0], zminMax[0]));
        double tmax = Math.min(xminMax[1], Math.min(yminMax[1], zminMax[1]));

        return !(tmin > tmax);

    }

    // TODO this and the intersect thing is a copy of the Cube implementation. Can we refactor/deduplicate?
    // sadly, a bounding box isn't a cube, but a cube might be a AABB?
    private double[] checkAxis(double min, double max, double origin, double direction) {
        var tminNumerator = min - origin;
        var tmaxNumerator = max - origin;

        double tmin;
        double tmax;

        if (Math.abs(direction) >= Constants.SHAPE_POINT_OFFSET_EPSILON) {
            tmin = tminNumerator / direction;
            tmax = tmaxNumerator / direction;
        } else {
            tmin = tminNumerator * Constants.POSITIVE_INFINITY;
            tmax = tmaxNumerator * Constants.POSITIVE_INFINITY;
        }

        if (tmin > tmax) {
            return new double[]{tmax, tmin};
        } else {
            return new double[]{tmin, tmax};
        }
    }
}
