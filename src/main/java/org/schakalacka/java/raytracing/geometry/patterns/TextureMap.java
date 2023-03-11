package org.schakalacka.java.raytracing.geometry.patterns;

import org.schakalacka.java.raytracing.math.Tuple;
import org.schakalacka.java.raytracing.math.UV;
import org.schakalacka.java.raytracing.math.UVMapping;
import org.schakalacka.java.raytracing.scene.Color;

public class TextureMap extends Pattern {

    private final UVPattern uvPattern;
    private final UVMapping uvMap;

    public TextureMap(UVPattern uvPattern, UVMapping uvMap) {

        this.uvPattern = uvPattern;
        this.uvMap = uvMap;
    }


    @Override
    public Color patternAt(Tuple point) {
        UV uv = uvMap.sphericalMap(point);
        return uvPattern.uv_patternAt(uv.u(), uv.v());
    }
}