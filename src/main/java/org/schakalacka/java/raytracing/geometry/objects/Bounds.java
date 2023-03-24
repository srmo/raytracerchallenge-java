package org.schakalacka.java.raytracing.geometry.objects;

import org.schakalacka.java.raytracing.math.Matrix;
import org.schakalacka.java.raytracing.math.RTPoint;
import org.schakalacka.java.raytracing.math.Tuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bounds {

    private final Map<Matrix, List<Tuple>> transformedCorners = new HashMap<>();
    private final List<Tuple> corners;

    private final RTPoint lower;
    private final RTPoint upper;

    public Bounds(RTPoint lower, RTPoint upper) {
        this.lower = lower;
        this.upper = upper;
        corners = List.of(
                Tuple.point(lower.x(), lower.y(), lower.z()),
                Tuple.point(lower.x(), lower.y(), upper.z()),
                Tuple.point(upper.x(), lower.y(), upper.z()),
                Tuple.point(upper.x(), lower.y(), lower.z()),
                Tuple.point(lower.x(), upper.y(), lower.z()),
                Tuple.point(lower.x(), upper.y(), upper.z()),
                Tuple.point(upper.x(), upper.y(), upper.z()),
                Tuple.point(upper.x(), upper.y(), lower.z())
        );
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
        Bounds bounds = (Bounds) o;
        return lower.equals(bounds.lower) && upper.equals(bounds.upper);
    }

    @Override
    public String toString() {
        return "Bounds{" +
                "lower=" + lower +
                ", upper=" + upper +
                '}';
    }

    public Bounds getTransformedBounds(Matrix scaleMatrix) {
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

        return new Bounds(Tuple.point(minX, minY, minZ), Tuple.point(maxX, maxY, maxZ));
    }

    /***
     * Returns the corners of the bounds. Starting with the lower left front corner and going clockwise
     * @return a non-null list of 8 tuples
     */
    public List<Tuple> getCorners() {
        return corners;
    }

    public List<Tuple> getTransformedCorners(Matrix matrix) {
        return transformedCorners.computeIfAbsent(matrix, k ->
        {
            var originalCornerList = getCorners();
            var transformedCorners = new ArrayList<Tuple>();

            for (Tuple corner : originalCornerList) {
                transformedCorners.add(matrix.mulT(corner));
            }
            return transformedCorners;
        });


    }
}
