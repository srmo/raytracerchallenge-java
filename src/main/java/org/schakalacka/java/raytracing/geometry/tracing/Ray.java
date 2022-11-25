package org.schakalacka.java.raytracing.geometry.tracing;

import org.schakalacka.java.raytracing.geometry.algebra.Matrix;
import org.schakalacka.java.raytracing.geometry.algebra.Tuple;

public record Ray(Tuple origin, Tuple direction) {

    public Tuple position(double ticks) {
        return origin.add(direction.mul(ticks));
    }

    public Ray transform(Matrix translation) {
        var newOrigin = translation.mulT(origin);
        var newDirection = translation.mulT(direction);
        return new Ray(newOrigin, newDirection);
    }
}
