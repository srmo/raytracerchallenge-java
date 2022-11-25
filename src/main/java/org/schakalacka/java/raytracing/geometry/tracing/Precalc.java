package org.schakalacka.java.raytracing.geometry.tracing;

import org.schakalacka.java.raytracing.geometry.algebra.Tuple;
import org.schakalacka.java.raytracing.geometry.objects.GeometryObject;

public class Precalc {


    private final double distance;
    private final GeometryObject object;
    private final Tuple point;
    private final Tuple eyeVector;
    private final Tuple normalVector;
    private final boolean inside;

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
    }

    public double getDistance() {
        return distance;
    }

    public GeometryObject getObject() {
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
}
