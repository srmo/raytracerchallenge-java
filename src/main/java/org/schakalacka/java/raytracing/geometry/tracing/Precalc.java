package org.schakalacka.java.raytracing.geometry.tracing;

import org.schakalacka.java.raytracing.geometry.objects.Shape;
import org.schakalacka.java.raytracing.math.Tuple;

import static org.schakalacka.java.raytracing.Constants.SHAPE_POINT_OFFSET_EPSILON;

public class Precalc {


    private final float distance;
    private final Shape object;
    private final Tuple point;
    private final Tuple eyeVector;
    private final Tuple normalVector;
    private final boolean inside;

    // a special point slightly lifted above some surface
    // reason: due to floating point rounding errors, the points for reflection, shadow etc
    // might end up 'below' a surface.
    private final Tuple overPoint;
    private final Tuple reflectVector;

    public Precalc(Intersection intersection, Ray ray) {
        distance = intersection.getDistance();
        object = intersection.getIntersectedObject();
        point = ray.position(distance);
        eyeVector = ray.direction().negate();

        if (object.normalVectorAt(point).dot(eyeVector) < 0) {
            this.inside = true;
            this.normalVector = object.normalVectorAt(point).negate();
        } else {
            this.inside = false;
            this.normalVector = object.normalVectorAt(point);
        }

        reflectVector = ray.direction().reflect(normalVector);
        overPoint = point.add(normalVector.mul(SHAPE_POINT_OFFSET_EPSILON));
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

    public Tuple getReflectVector() {
        return reflectVector;
    }
}
