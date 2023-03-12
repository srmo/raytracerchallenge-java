package org.schakalacka.java.raytracing.scene;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.math.Tuple;
import org.schakalacka.java.raytracing.geometry.objects.Sphere;
import org.schakalacka.java.raytracing.geometry.patterns.StripePattern;

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
        assertEquals(0, material.reflectivity());
        assertEquals(0, material.transparency());
        assertEquals(1.0, material.refractiveIndex());
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
        var eyeVector = Tuple.vector(0, (float) (Math.sqrt(2) / 2), (float) (-Math.sqrt(2) / 2));
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
        var eyeVector = Tuple.vector(0, (float) (-Math.sqrt(2) / 2), (float) (-Math.sqrt(2) / 2));
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

        Color lighting = defaultMaterial.lighting(light, null, defaultPosition, eyeVector, normalVector, true);

        assertEquals(new Color(0.1, 0.1, 0.1), lighting);
    }

    @Test
    void lightingWithPattern() {
        var material = Material.newMaterial().ambient(1).diffuse(0).specular(0).pattern(new StripePattern(new Color(1, 1, 1), new Color(0, 0, 0))).create();

        var eyeVector = Tuple.vector(0, 0, -1);
        var normalVector = Tuple.vector(0, 0, -1);
        var light = new PointLight(Tuple.point(0, 0, -10), new Color(1, 1, 1));

        var c1 = material.lighting(light, new Sphere(), Tuple.point(0.9f, 0, 0), eyeVector, normalVector, false);
        var c2 = material.lighting(light, new Sphere(), Tuple.point(1.1f, 0, 0), eyeVector, normalVector, false);

        assertEquals(new Color(1, 1, 1), c1);
        assertEquals(new Color(0, 0, 0), c2);


    }
}