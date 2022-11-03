package org.schakalacka.java.raytracing.geometry.tracing;

import org.schakalacka.java.raytracing.geometry.Tuple;

public record Ray(Tuple origin, Tuple direction) {

    public Tuple position(double ticks) {
        return origin.add(direction.mul((float) ticks));
    }
}
