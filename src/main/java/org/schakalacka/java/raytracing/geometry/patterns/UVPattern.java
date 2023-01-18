package org.schakalacka.java.raytracing.geometry.patterns;

import org.schakalacka.java.raytracing.scene.Color;

public interface UVPattern {

    Color uv_patternAt(double u, double v);
}
