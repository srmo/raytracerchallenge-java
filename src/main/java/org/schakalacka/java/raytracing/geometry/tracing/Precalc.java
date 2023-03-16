package org.schakalacka.java.raytracing.geometry.tracing;

import org.schakalacka.java.raytracing.geometry.objects.Shape;
import org.schakalacka.java.raytracing.math.Tuple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.schakalacka.java.raytracing.Constants.SHAPE_POINT_OFFSET_EPSILON;

public class Precalc {


    private final float distance;
    private final Shape object;
    private final Tuple point;
    private final Tuple eyeVector;
    private final Tuple normalVector;
    private final boolean inside;

    // a special point slightly lifted above some surface
    // reason: due to floating point rounding errors, the points for reflection, shadow etc.
    // might end up 'below' a surface.
    private final Tuple overPoint;
    private final Tuple underPoint;
    private final Tuple reflectVector;

    private final List<Intersection> encounteredIntersections;
    private final Intersection hit;
    private double n1 = 0;
    private double n2 = 0;

    public Precalc(Intersection hit, Ray ray) {
        this(hit, ray, Collections.singletonList(hit));
    }

    public Precalc(Intersection intersection, Ray ray, List<Intersection> intersections) {
        this.distance = intersection.getDistance();
        this.object = intersection.getIntersectedObject();
        this.point = ray.position(distance);
        this.eyeVector = ray.direction().negate();
        this.encounteredIntersections = intersections;
        this.hit = intersection;

        if (object.normalVectorAt(point).dot(eyeVector) < 0) {
            this.inside = true;
            this.normalVector = object.normalVectorAt(point).negate();
        } else {
            this.inside = false;
            this.normalVector = object.normalVectorAt(point);
        }

        this.reflectVector = ray.direction().reflect(normalVector);
        this.overPoint = point.add(normalVector.mul(SHAPE_POINT_OFFSET_EPSILON));
        this.underPoint = point.sub(normalVector.mul(SHAPE_POINT_OFFSET_EPSILON));

        calculateN1N2();
    }

    private void calculateN1N2() {
        // this will hold all the objects we have encountered so far and where we are still inside with our ray
        var containers = new ArrayList<Shape>();


        for (Intersection intersection : encounteredIntersections) {
            if (intersection.equals(this.hit)) {
                if (containers.isEmpty()) {
                    this.n1 = 1;
                } else {
                    // refractive index of the last object we encountered
                    this.n1 = containers.get(containers.size() - 1).material().refractiveIndex();
                }
            }

            if (containers.contains(intersection.getIntersectedObject())) {
                containers.remove(intersection.getIntersectedObject());
            } else {
                containers.add(intersection.getIntersectedObject());
            }

            if (intersection.equals(this.hit)) {
                if (containers.isEmpty()) {
                    this.n2 = 1;
                } else {
                    // refractive index of the last object we encountered
                    this.n2 = containers.get(containers.size() - 1).material().refractiveIndex();
                }

                // return early once we found n2
                return;
            }
        }
    }

    public float getDistance() {
        return distance;
    }

    public Shape getObject() {
        return object;
    }

    public Tuple getPoint() {
        return point;
    }

    public Tuple getEyeVector() {
        return eyeVector;
    }

    public Tuple getNormalVector() {
        return normalVector;
    }

    public boolean inside() {
        return inside;
    }

    public Tuple getOverPoint() {
        return overPoint;
    }
    public Tuple getUnderPoint() {
        return underPoint;
    }

    public Tuple getReflectVector() {
        return reflectVector;
    }

    public double getN1() {
        return n1;
    }

    public double getN2() {
        return n2;
    }

    public double schlick() {
        double cos = eyeVector.dot(normalVector);

        // total internal reflection can only occur if n1 > n2
        if (n1 > n2) {
            var n = n1 / n2;
            var sin2_t = n * n * (1.0 - cos * cos);
            if (sin2_t > 1.0) {
                return 1.0;
            }

            // compute cos(theta_t) using trig identity
            cos = Math.sqrt(1.0 - sin2_t);
        }

        var r0 = Math.pow((n1 - n2) / (n1 + n2), 2);
        return r0 + (1 - r0) * Math.pow(1 - cos, 5);
    }
}
