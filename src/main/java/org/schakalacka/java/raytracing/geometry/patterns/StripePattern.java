package org.schakalacka.java.raytracing.geometry.patterns;

import org.schakalacka.java.raytracing.math.Tuple;
import org.schakalacka.java.raytracing.scene.Color;

public class StripePattern extends Pattern {

    private final Color a;
    private final Color b;

    public StripePattern(Color a, Color b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public Color patternAt(Tuple point) {
        if (Math.floor(point.x()) % 2 == 0) {
            return a;
        } else {
            return b;
        }
    }

    public Color a() {
        return a;
    }

    public Color b() {
        return b;
    }

}
