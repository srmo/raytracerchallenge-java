package org.schakalacka.java.raytracing.geometry.patterns;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.geometry.algebra.Tuple;
import org.schakalacka.java.raytracing.scene.Color;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CheckerPatternTest {

    @Test
    void checkersRepeatInX() {
        var pattern = new CheckerPattern(Color.WHITE, Color.BLACK);

        assertEquals(Color.WHITE, pattern.patternAt(Tuple.point(0, 0, 0)));
        assertEquals(Color.WHITE, pattern.patternAt(Tuple.point(0.99, 0, 0)));
        assertEquals(Color.BLACK, pattern.patternAt(Tuple.point(1.01, 0, 0)));
    }

    @Test
    void checkersRepeatInY() {
        var pattern = new CheckerPattern(Color.WHITE, Color.BLACK);

        assertEquals(Color.WHITE, pattern.patternAt(Tuple.point(0, 0, 0)));
        assertEquals(Color.WHITE, pattern.patternAt(Tuple.point(0, 0.99, 0)));
        assertEquals(Color.BLACK, pattern.patternAt(Tuple.point(0, 1.01, 0)));
    }

    @Test
    void checkersRepeatInZ() {
        var pattern = new CheckerPattern(Color.WHITE, Color.BLACK);

        assertEquals(Color.WHITE, pattern.patternAt(Tuple.point(0, 0, 0)));
        assertEquals(Color.WHITE, pattern.patternAt(Tuple.point(0, 0, 0.99)));
        assertEquals(Color.BLACK, pattern.patternAt(Tuple.point(0, 0, 1.01)));
    }

}