package org.schakalacka.java.raytracing.geometry.objects;

import org.schakalacka.java.raytracing.geometry.Tuple;
import org.schakalacka.java.raytracing.geometry.tracing.Intersection;
import org.schakalacka.java.raytracing.geometry.tracing.Ray;

import java.util.Arrays;
import java.util.Comparator;

public class Sphere implements GeometryObject {

    private final Tuple position = Tuple.point(0, 0, 0);


    public Tuple position() {
        return position;
    }

    public double radius() {
        return 1;
    }

    @Override
    public Intersection[] intersect(Ray ray) {
        Intersection[] result = new Intersection[0];

        var vectorSphereToRay = ray.origin().sub(Tuple.point(0, 0, 0));
        var a = ray.direction().dot(ray.direction());
        var b = 2 * ray.direction().dot(vectorSphereToRay);
        var c = vectorSphereToRay.dot(vectorSphereToRay) - 1;

        var discriminant = b * b - 4 * a * c;

        if (discriminant >= 0) {
            result = new Intersection[2];
            result[0] = new Intersection(this, (-b - Math.sqrt(discriminant)) / (2 * a));
            result[1] = new Intersection(this, (-b + Math.sqrt(discriminant)) / (2 * a));
            Arrays.sort(result, Comparator.comparingDouble(Intersection::getDistance));
        }

        return result;
    }
}
