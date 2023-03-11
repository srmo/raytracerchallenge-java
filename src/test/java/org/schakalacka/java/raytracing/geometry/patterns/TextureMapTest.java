package org.schakalacka.java.raytracing.geometry.patterns;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.math.Tuple;
import org.schakalacka.java.raytracing.math.UVMapping;
import org.schakalacka.java.raytracing.scene.Color;

import static org.junit.jupiter.api.Assertions.*;

class TextureMapTest {

    @Test
    void patternAt() {
        UVCheckerPattern uvCheckerPattern = new UVCheckerPattern(16, 8, Color.BLACK, Color.WHITE);
        var textureMap = new TextureMap(uvCheckerPattern, new UVMapping());

        assertEquals(Color.WHITE,textureMap.patternAt(Tuple.point(0.4315f,0.4670f,0.7719f)));
        assertEquals(Color.BLACK,textureMap.patternAt(Tuple.point(-0.9654f,0.2552f,-0.0534f)));
        assertEquals(Color.WHITE,textureMap.patternAt(Tuple.point(0.1039f,0.7090f,0.6975f)));
        assertEquals(Color.BLACK,textureMap.patternAt(Tuple.point(-0.4986f,-0.7856f,-0.3663f)));
        assertEquals(Color.BLACK,textureMap.patternAt(Tuple.point(-0.0317f,-0.9395f,0.3411f)));
        assertEquals(Color.BLACK,textureMap.patternAt(Tuple.point(0.4809f,-0.7721f,0.4154f)));
        assertEquals(Color.BLACK,textureMap.patternAt(Tuple.point(0.0285f,-0.9612f,-0.2745f)));
        assertEquals(Color.WHITE,textureMap.patternAt(Tuple.point(-0.5734f,-0.2162f,-0.7903f)));
        assertEquals(Color.BLACK,textureMap.patternAt(Tuple.point(0.7688f,-0.1470f,0.6223f)));
        assertEquals(Color.BLACK,textureMap.patternAt(Tuple.point(-0.7652f,0.2175f,0.6060f)));
    }

}