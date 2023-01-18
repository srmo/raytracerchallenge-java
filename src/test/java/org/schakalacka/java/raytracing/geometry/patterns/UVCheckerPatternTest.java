package org.schakalacka.java.raytracing.geometry.patterns;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.scene.Color;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UVCheckerPatternTest {

    @Test
    void checkerPatternIn2D() {
        var pattern = new UVCheckerPattern(2, 2, Color.BLACK, Color.WHITE);

        assertEquals(Color.BLACK, pattern.uv_patternAt(0.0, 0.0));
        assertEquals(Color.WHITE, pattern.uv_patternAt(0.5, 0.0));
        assertEquals(Color.WHITE, pattern.uv_patternAt(0.0, 0.5));
        assertEquals(Color.BLACK, pattern.uv_patternAt(0.5, 0.5));
        assertEquals(Color.BLACK, pattern.uv_patternAt(1.0, 1.0));
    }

    @Test
    void sphericalMapping() {
        var pattern = new UVCheckerPattern(2, 2, Color.BLACK, Color.WHITE);

        assertEquals(Color.BLACK, pattern.uv_patternAt(0.0, 0.0));
        assertEquals(Color.WHITE, pattern.uv_patternAt(0.5, 0.0));
        assertEquals(Color.WHITE, pattern.uv_patternAt(0.0, 0.5));
        assertEquals(Color.BLACK, pattern.uv_patternAt(0.5, 0.5));
        assertEquals(Color.BLACK, pattern.uv_patternAt(1.0, 1.0));
    }

}