package org.schakalacka.java.raytracing.geometry.tracing;

import org.schakalacka.java.raytracing.geometry.objects.Shape;

import java.util.Objects;

public class Intersection {
    private final Shape object;
    private final double distance;

    public Intersection(Shape object, double ticks) {
        this.object = object;
        this.distance = ticks;
    }

    public Shape getIntersectedObject() {
        return this.object;
    }

    public double getDistance() {
        return this.distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Intersection that = (Intersection) o;
        return Double.compare(that.distance, distance) == 0 && object.equals(that.object);
    }

    @Override
    public int hashCode() {
        return Objects.hash(object, distance);
    }
}
