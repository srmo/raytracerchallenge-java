package org.schakalacka.java.raytracing.geometry.patterns;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.math.Tuple;
import org.schakalacka.java.raytracing.scene.Color;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GradientPatternTest {

    @Test
    void gradientPatternInterpolatesColorsLinearly() {
        var pattern = new GradientPattern(Color.WHITE, Color.BLACK);

        var c1 = pattern.patternAt(Tuple.point(0, 0, 0));
        var c2 = pattern.patternAt(Tuple.point(0.25f, 0, 0));
        var c3 = pattern.patternAt(Tuple.point(0.5f, 0, 0));
        var c4 = pattern.patternAt(Tuple.point(0.75f, 0, 0));

        assertEquals(new Color(1, 1, 1), c1);
        assertEquals(new Color(0.75, 0.75,0.75), c2);
        assertEquals(new Color(0.5, 0.5, 0.5), c3);
        assertEquals(new Color(0.25, 0.25, 0.25), c4);
    }

}