package org.schakalacka.java.raytracing.geometry.objects;

import org.schakalacka.java.raytracing.Constants;
import org.schakalacka.java.raytracing.geometry.tracing.Intersection;
import org.schakalacka.java.raytracing.geometry.tracing.Ray;
import org.schakalacka.java.raytracing.math.Tuple;

import java.util.ArrayList;
import java.util.List;

public class Cylinder extends Shape {

    final double minimum;
    final double maximum;

    boolean isClosed = false;

    public Cylinder() {
        this(Constants.NEGATIVE_INFINITY, Constants.POSITIVE_INFINITY);
    }

    public Cylinder(double minimum, double maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    @Override
    public BoundingBox getBounds() {
        return new BoundingBox(Tuple.point(-1, minimum, -1), Tuple.point(1, maximum, 1));
    }

    @Override
    public List<Intersection> localIntersect(Ray ray) {
        var a = Math.pow(ray.direction().x(),2) + Math.pow(ray.direction().z(), 2);

        if (!isClosed && a < Constants.SHAPE_POINT_OFFSET_EPSILON) {
            return List.of();
        }

        var b = 2 * (ray.origin().x() * ray.direction().x()) + 2 * (ray.origin().z() * ray.direction().z());
        var c = Math.pow(ray.origin().x(),2) + Math.pow(ray.origin().z(),2) - 1;
        var discriminant = Math.pow(b,2) - 4 * a * c;

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

        var intersections = new ArrayList<Intersection>();

        var y0 = ray.origin().y() + t0 * ray.direction().y();
        if (this.minimum < y0 && y0 < this.maximum) {
            intersections.add(new Intersection(this, t0));
        }
        var y1 = ray.origin().y() + t1 * ray.direction().y();
        if (this.minimum < y1 && y1 < this.maximum) {
            intersections.add(new Intersection(this, t1));
        }

        intersections.addAll(this.intersectCaps(ray));
        return intersections;
    }

    private boolean checkCaps(Ray ray, double distance) {
        var x = ray.origin().x() + distance * ray.direction().x();
        var z = ray.origin().z() + distance * ray.direction().z();
        return (x * x + z * z) <= 1;
    }
    private List<Intersection> intersectCaps(Ray ray) {
        var intersections = new ArrayList<Intersection>();

        if (!this.isClosed || Math.abs(ray.direction().y()) < Constants.SHAPE_POINT_OFFSET_EPSILON) {
            return intersections;
        }

        // Check for an intersection with the lower end cap by intersecting
        // the ray with the plane at y=cylinder.minimum
        var t = (this.minimum - ray.origin().y()) / ray.direction().y();
        if (this.checkCaps(ray, t)) {
            intersections.add(new Intersection(this, t));
        }

        // Check for an intersection with the upper end cap by intersecting
        // the ray with the plane at y=cylinder.maximum
        t = (this.maximum - ray.origin().y()) / ray.direction().y();
        if (this.checkCaps(ray, t)) {
            intersections.add(new Intersection(this, t));
        }

        return intersections;
    }

    @Override
    public Tuple localNormalVectorAt(Tuple point) {
        if (!isClosed) {
            return Tuple.vector(point.x(), 0, point.z());
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

    public double getMinimum() {
        return this.minimum;
    }

    public double getMaximum() {
        return this.maximum;
    }

    public void setClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }

    public boolean isClosed() {
        return this.isClosed;
    }

}
