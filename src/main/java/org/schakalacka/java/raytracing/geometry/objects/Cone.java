package org.schakalacka.java.raytracing.geometry.objects;

import org.schakalacka.java.raytracing.Constants;
import org.schakalacka.java.raytracing.geometry.tracing.Intersection;
import org.schakalacka.java.raytracing.geometry.tracing.Ray;
import org.schakalacka.java.raytracing.math.Tuple;

import java.util.ArrayList;
import java.util.List;

public class Cone extends Cylinder {


    public Cone() {
        super();
    }

    public Cone(double minimum, double maximum) {
        super(minimum, maximum);
    }

    @Override
    public List<Intersection> localIntersect(Ray ray) {
        var intersections = new ArrayList<Intersection>();

        final Tuple direction = ray.direction();
        final Tuple origin = ray.origin();

        var a = Math.pow(direction.x(),2) - Math.pow(direction.y(), 2) + Math.pow(direction.z(), 2);
        var b = 2 * (origin.x() * direction.x()) - 2 * (origin.y() * direction.y()) + 2 * (origin.z() * direction.z());
        var c = Math.pow(origin.x(),2) - Math.pow(origin.y(), 2) + Math.pow(origin.z(),2);

        if (Math.abs(a) <= Constants.SHAPE_POINT_OFFSET_EPSILON && Math.abs(b) <= Constants.SHAPE_POINT_OFFSET_EPSILON) {
            return List.of();
        } else if (Math.abs(a) <= Constants.SHAPE_POINT_OFFSET_EPSILON && Math.abs(b) > Constants.SHAPE_POINT_OFFSET_EPSILON) {
            var t = -c / (2 * b);
            intersections.add(new Intersection(this, t));
        } else {

            var discriminant = Math.pow(b, 2) - 4 * a * c;

            if (discriminant < 0) {
                return List.of();
            }

            double t0 = (-b - Math.sqrt(discriminant)) / (2 * a);
            double t1 = (-b + Math.sqrt(discriminant)) / (2 * a);

            if (t0 > t1) {
                var temp = t0;
                t0 = t1;
                t1 = temp;
            }


            var y0 = origin.y() + t0 * direction.y();
            if (this.minimum < y0 && y0 < this.maximum) {
                intersections.add(new Intersection(this, t0));
            }
            var y1 = origin.y() + t1 * direction.y();
            if (this.minimum < y1 && y1 < this.maximum) {
                intersections.add(new Intersection(this, t1));
            }
        }
        intersections.addAll(this.intersectCaps(ray));
        return intersections;
    }

    private List<Intersection> intersectCaps(Ray ray) {
        var intersections = new ArrayList<Intersection>();

        if (!this.isClosed || Math.abs(ray.direction().y()) < Constants.SHAPE_POINT_OFFSET_EPSILON) {
            return intersections;
        }

        // Check for an intersection with the lower end cap by intersecting
        // the ray with the plane at y=cylinder.minimum
        var t = (this.minimum - ray.origin().y()) / ray.direction().y();
        if (this.checkCaps(ray, t, this.minimum)) {
            intersections.add(new Intersection(this, t));
        }

        // Check for an intersection with the upper end cap by intersecting
        // the ray with the plane at y=cylinder.maximum
        t = (this.maximum - ray.origin().y()) / ray.direction().y();
        if (this.checkCaps(ray, t, this.maximum)) {
            intersections.add(new Intersection(this, t));
        }

        return intersections;
    }

    boolean checkCaps(Ray ray, double distance, double y) {
        var x = ray.origin().x() + distance * ray.direction().x();
        var z = ray.origin().z() + distance * ray.direction().z();

        return (x * x + z * z) <= y * y;
    }

    @Override
    public Tuple localNormalVectorAt(Tuple point) {
        if (!isClosed) {
            var y = Math.sqrt(Math.pow(point.x(), 2) + Math.pow(point.z(), 2));
            if (point.y() > 0) {
                y = y * -1;
            }
            return Tuple.vector(point.x(), y, point.z());
        } else {
            var distanceFromY = Math.pow(point.x(), 2) + Math.pow(point.z(), 2);

            if (distanceFromY < 1 && point.y() >= this.maximum - Constants.SHAPE_POINT_OFFSET_EPSILON) {
                return Tuple.vector(0, 1, 0);
            } else if (distanceFromY < 1 && point.y() <= this.minimum + Constants.SHAPE_POINT_OFFSET_EPSILON) {
                return Tuple.vector(0, -1, 0);
            } else {
                return Tuple.vector(point.x(), 0, point.z());
            }
        }
    }
}
