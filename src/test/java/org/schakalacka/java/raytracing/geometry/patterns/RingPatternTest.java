package org.schakalacka.java.raytracing.geometry.patterns;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.geometry.algebra.Tuple;
import org.schakalacka.java.raytracing.scene.Color;

import static org.junit.jupiter.api.Assertions.*;

class RingPatternTest {

    @Test
    void ringExtendsInBothXandZ() {
        var pattern = new RingPattern(Color.WHITE, Color.BLACK);

        assertEquals(Color.WHITE, pattern.patternAt(Tuple.point(0,0,0)));
        assertEquals(Color.BLACK, pattern.patternAt(Tuple.point(1,0,0)));
        assertEquals(Color.BLACK, pattern.patternAt(Tuple.point(0,0,1)));
        assertEquals(Color.BLACK, pattern.patternAt(Tuple.point(0.708,0,0.708)));
    }

}