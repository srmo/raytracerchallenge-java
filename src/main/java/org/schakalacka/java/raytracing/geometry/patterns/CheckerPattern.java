package org.schakalacka.java.raytracing.geometry.patterns;

import org.schakalacka.java.raytracing.geometry.algebra.Tuple;
import org.schakalacka.java.raytracing.scene.Color;

public class CheckerPattern extends Pattern {

    private final Color a;
    private final Color b;

    public CheckerPattern(Color a, Color b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public Color patternAt(Tuple point) {
        var sum = Math.floor(point.x()) + Math.floor(point.y()) + Math.floor(point.z());
        if (sum % 2 == 0) {
            return a;
        } else {
            return b;
        }
    }
}
