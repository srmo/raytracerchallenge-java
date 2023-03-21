package org.schakalacka.java.raytracing.geometry.objects;

import org.schakalacka.java.raytracing.Constants;
import org.schakalacka.java.raytracing.geometry.tracing.Intersection;
import org.schakalacka.java.raytracing.geometry.tracing.Ray;
import org.schakalacka.java.raytracing.math.Tuple;

import java.util.List;

public class Cube extends Shape {
    @Override
    public Bounds getBounds() {
        return new Bounds(Tuple.point(-1, -1, -1), Tuple.point(1, 1, 1));
    }

    @Override
    public List<Intersection> localIntersect(Ray ray) {
        double[] xminMax = checkAxis(ray.origin().x(), ray.direction().x());
        double[] yminMax = checkAxis(ray.origin().y(), ray.direction().y());
        double[] zminMax = checkAxis(ray.origin().z(), ray.direction().z());

        double tmin = Math.max(xminMax[0], Math.max(yminMax[0], zminMax[0]));
        double tmax = Math.min(xminMax[1], Math.min(yminMax[1], zminMax[1]));

        if (tmin > tmax) {
            return List.of();
        } else {
            return List.of(new Intersection(this,  tmin), new Intersection(this,  tmax));
        }
    }

    private double[] checkAxis(double origin, double direction) {
        var tminNumerator = -1 - origin;
        var tmaxNumerator = 1 - origin;

        double tmin;
        double tmax;

        if (Math.abs(direction) >= Constants.SHAPE_POINT_OFFSET_EPSILON) {
            tmin = tminNumerator / direction;
            tmax = tmaxNumerator / direction;
        } else {
            tmin = tminNumerator * Double.POSITIVE_INFINITY;
            tmax = tmaxNumerator * Double.POSITIVE_INFINITY;
        }

        if (tmin > tmax) {
            return new double[]{tmax, tmin};
        } else {
            return new double[]{tmin, tmax};
        }
    }

    @Override
    public Tuple localNormalVectorAt(Tuple point) {
        var maxc = Math.max(Math.abs(point.x()), Math.max(Math.abs(point.y()), Math.abs(point.z())));
        if (maxc == Math.abs(point.x())) {
            return Tuple.vector(point.x(), 0, 0);
        } else if (maxc == Math.abs(point.y())) {
            return Tuple.vector(0, point.y(), 0);
        } else {
            return Tuple.vector(0, 0, point.z());
        }
    }
}
