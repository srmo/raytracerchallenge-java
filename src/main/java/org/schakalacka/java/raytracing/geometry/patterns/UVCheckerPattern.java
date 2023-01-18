package org.schakalacka.java.raytracing.geometry.patterns;

import org.schakalacka.java.raytracing.scene.Color;

import static java.lang.Math.floor;

public class UVCheckerPattern implements UVPattern {

    private final int width;
    private final int height;
    private final Color a;
    private final Color b;

    public UVCheckerPattern(int width, int height, Color a, Color b) {
        this.width = width;
        this.height = height;
        this.a = a;
        this.b = b;
    }

    public Color uv_patternAt(double u, double v) {
        var u2 = floor(u * width);
        var v2 = floor(v * height);

        if ((u2+v2) % 2 == 0) {
            return a;
        } else {
            return b;
        }
    }
}
