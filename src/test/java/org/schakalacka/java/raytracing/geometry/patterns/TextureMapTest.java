package org.schakalacka.java.raytracing.geometry.patterns;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.geometry.algebra.Tuple;
import org.schakalacka.java.raytracing.geometry.algebra.UVMapping;
import org.schakalacka.java.raytracing.scene.Color;

import static org.junit.jupiter.api.Assertions.*;

class TextureMapTest {

    @Test
    void patternAt() {
        UVCheckerPattern uvCheckerPattern = new UVCheckerPattern(16, 8, Color.BLACK, Color.WHITE);
        var textureMap = new TextureMap(uvCheckerPattern, new UVMapping());

        assertEquals(Color.WHITE,textureMap.patternAt(Tuple.point(0.4315,0.4670,0.7719)));
        assertEquals(Color.BLACK,textureMap.patternAt(Tuple.point(-0.9654,0.2552,-0.0534)));
        assertEquals(Color.WHITE,textureMap.patternAt(Tuple.point(0.1039,0.7090,0.6975)));
        assertEquals(Color.BLACK,textureMap.patternAt(Tuple.point(-0.4986,-0.7856,-0.3663)));
        assertEquals(Color.BLACK,textureMap.patternAt(Tuple.point(-0.0317,-0.9395,0.3411)));
        assertEquals(Color.BLACK,textureMap.patternAt(Tuple.point(0.4809,-0.7721,0.4154)));
        assertEquals(Color.BLACK,textureMap.patternAt(Tuple.point(0.0285,-0.9612,-0.2745)));
        assertEquals(Color.WHITE,textureMap.patternAt(Tuple.point(-0.5734,-0.2162,-0.7903)));
        assertEquals(Color.BLACK,textureMap.patternAt(Tuple.point(0.7688,-0.1470,0.6223)));
        assertEquals(Color.BLACK,textureMap.patternAt(Tuple.point(-0.7652,0.2175,0.6060)));
    }

}