package org.schakalacka.java.raytracing.geometry.objects;

import org.schakalacka.java.raytracing.Constants;
import org.schakalacka.java.raytracing.geometry.tracing.Intersection;
import org.schakalacka.java.raytracing.geometry.tracing.Ray;
import org.schakalacka.java.raytracing.math.Tuple;

import java.util.List;

public class Cylinder extends Shape {


    @Override
    public List<Intersection> localIntersect(Ray ray) {
        var a = Math.pow(ray.direction().x(),2) + Math.pow(ray.direction().z(), 2);

        if (a < Constants.SHAPE_POINT_OFFSET_EPSILON) {
            return List.of();
        }

        var b = 2 * (ray.origin().x() * ray.direction().x()) + 2 * (ray.origin().z() * ray.direction().z());
        var c = Math.pow(ray.origin().x(),2) + Math.pow(ray.origin().z(),2) - 1;
        var discriminant = Math.pow(b,2) - 4 * a * c;

        if (discriminant < 0) {
            return List.of();
        }

        float t0 = (float) ((-b - Math.sqrt(discriminant)) / (2 * a));
        float t1 = (float) ((-b + Math.sqrt(discriminant)) / (2 * a));
        return List.of(new Intersection(this, t0), new Intersection(this, t1));
    }

    @Override
    public Tuple localNormalVectorAt(Tuple point) {
        return Tuple.vector(point.x(), 0, point.z());
    }
}
