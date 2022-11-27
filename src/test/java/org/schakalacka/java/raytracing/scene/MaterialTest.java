package org.schakalacka.java.raytracing.scene;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.geometry.algebra.Tuple;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MaterialTest {

    private final Tuple defaultPosition = Tuple.point(0, 0, 0);
    private final Material defaultMaterial = Material.newMaterial().create();

    @Test
    void defaultMaterial() {
        var material = Material.newMaterial().create();

        assertEquals(new Color(1, 1, 1), material.color());
        assertEquals(0.1, material.ambient());
        assertEquals(0.9, material.diffuse());
        assertEquals(0.9, material.specular());
        assertEquals(200.0, material.shininess());
    }

    @Test
    void lightingEyeBetweenLightAndSurface() {
        var eyeVector = Tuple.vector(0, 0, -1);
        var normalVector = Tuple.vector(0, 0, -1);
        var light = new PointLight(Tuple.point(0, 0, -10), new Color(1, 1, 1));

        var result = defaultMaterial.lighting(light, defaultPosition, eyeVector, normalVector);

        assertEquals(new Color(1.9, 1.9, 1.9), result);
    }

    @Test
    void lightingEyeBetweenLightAndSurfaceEyeOffset45() {
        var eyeVector = Tuple.vector(0, Math.sqrt(2) / 2, -Math.sqrt(2) / 2);
        var normalVector = Tuple.vector(0, 0, -1);
        var light = new PointLight(Tuple.point(0, 0, -10), new Color(1, 1, 1));

        var result = defaultMaterial.lighting(light, defaultPosition, eyeVector, normalVector);

        assertEquals(new Color(1, 1, 1), result);
    }

    @Test
    void lightingEyeBetweenLightAndSurfaceLightOffset45() {
        var eyeVector = Tuple.vector(0, 0, -1);
        var normalVector = Tuple.vector(0, 0, -1);
        var light = new PointLight(Tuple.point(0, 10, -10), new Color(1, 1, 1));

        var result = defaultMaterial.lighting(light, defaultPosition, eyeVector, normalVector);

        assertEquals(new Color(0.736396, 0.736396, 0.736396), result);
    }

    @Test
    void lightingEyeBetweenLightAndSurfaceLightAndEyeOffset45() {
        var eyeVector = Tuple.vector(0, -Math.sqrt(2) / 2, -Math.sqrt(2) / 2);
        var normalVector = Tuple.vector(0, 0, -1);
        var light = new PointLight(Tuple.point(0, 10, -10), new Color(1, 1, 1));

        var result = defaultMaterial.lighting(light, defaultPosition, eyeVector, normalVector);

        assertEquals(new Color(1.636396, 1.636396, 1.636396), result);
    }

    @Test
    void lightingLightBehindSurface() {
        var eyeVector = Tuple.vector(0, 0, -1);
        var normalVector = Tuple.vector(0, 0, -1);
        var light = new PointLight(Tuple.point(0, 0, 10), new Color(1, 1, 1));

        var result = defaultMaterial.lighting(light, defaultPosition, eyeVector, normalVector);

        assertEquals(new Color(0.1, 0.1, 0.1), result);
    }

    @Test
    void lightingSurfaceInShadow() {
        var eyeVector = Tuple.vector(0, 0, -1);
        var normalVector = Tuple.vector(0, 0, -1);
        var light = new PointLight(Tuple.point(0, 0, -10), new Color(1, 1, 1));

        Color lighting = defaultMaterial.lighting(light, defaultPosition, eyeVector, normalVector, true);

        assertEquals(new Color(0.1, 0.1, 0.1), lighting);
    }

}