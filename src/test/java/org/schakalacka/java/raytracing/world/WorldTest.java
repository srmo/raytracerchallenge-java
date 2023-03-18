package org.schakalacka.java.raytracing.world;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.geometry.objects.Plane;
import org.schakalacka.java.raytracing.geometry.objects.Sphere;
import org.schakalacka.java.raytracing.geometry.patterns.PatternTest;
import org.schakalacka.java.raytracing.geometry.tracing.Intersection;
import org.schakalacka.java.raytracing.geometry.tracing.Precalc;
import org.schakalacka.java.raytracing.geometry.tracing.Ray;
import org.schakalacka.java.raytracing.geometry.tracing.ShadowResult;
import org.schakalacka.java.raytracing.math.MatrixProvider;
import org.schakalacka.java.raytracing.math.RTPoint;
import org.schakalacka.java.raytracing.math.RTVector;
import org.schakalacka.java.raytracing.math.Tuple;
import org.schakalacka.java.raytracing.scene.Color;
import org.schakalacka.java.raytracing.scene.Material;
import org.schakalacka.java.raytracing.scene.PointLight;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WorldTest {

    @Test
    void newWorldEmpty() {
        var world = new World();

        assertEquals(0, world.getObjects().size());
        assertNull(world.getLightSource());
    }

    @Test
    void defaultWorld() {
        var world = World.getDefault();
        var lightSource = new PointLight(Tuple.point(-10, 10, -10), new Color(1, 1, 1));
        var sphere1 = new Sphere();
        sphere1.setMaterial(Material.newMaterial().color(new Color(0.8, 1, 0.6)).diffuse(0.7).specular(0.2).create());

        var sphere2 = new Sphere();
        sphere2.setTransformationMatrix(MatrixProvider.scaling(0.5f, 0.5f, 0.5f));

        assertEquals(lightSource, world.getLightSource());
        assertTrue(world.getObjects().contains(sphere1));
        assertTrue(world.getObjects().contains(sphere2));

    }

    @Test
    void defaultWorldIntersections() {
        var world = World.getDefault();

        var intersections = world.intersect(new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1)));

        assertEquals(4, intersections.size());
        assertEquals(4, intersections.get(0).getDistance());
        assertEquals(4.5, intersections.get(1).getDistance());
        assertEquals(5.5, intersections.get(2).getDistance());
        assertEquals(6, intersections.get(3).getDistance());
    }

    @Test
    void shadeIntersectionOutside() {
        var world = World.getDefault();
        var ray = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1));
        var shape = world.getObjects().get(0);
        var intersection = new Intersection(shape, 4);
        var precalc = new Precalc(intersection, ray);

        var shaderColor = world.shade_hit(precalc);

        assertEquals(new Color(0.380661193, 0.47582649, 0.28549589), shaderColor);
    }

    @Test
    void shadeIntersectionInShader() {
        var world = new World();
        world.setLightSource(new PointLight(Tuple.point(0, 0, -10), new Color(1, 1, 1)));
        var sphere1 = new Sphere();
        var sphere2 = new Sphere();
        sphere2.setTransformationMatrix(MatrixProvider.translation(0, 0, 10));

        world.addObjects(sphere1, sphere2);

        var ray = new Ray(Tuple.point(0, 0, 5), Tuple.vector(0, 0, 1));
        var intersection = new Intersection(sphere2, 4);

        var precalcs = new Precalc(intersection, ray);
        var shadeHit = world.shade_hit(precalcs);

        assertEquals(new Color(0.1, 0.1, 0.1), shadeHit);
    }

    @Test
    void shadeIntersectionInside() {
        var world = World.getDefault();
        world.setLightSource(new PointLight(Tuple.point(0, 0.25f, 0), new Color(1, 1, 1)));

        var ray = new Ray(Tuple.point(0, 0, 0), Tuple.vector(0, 0, 1));
        var shape = world.getObjects().get(1);
        var intersection = new Intersection(shape, 0.5f);
        var precalc = new Precalc(intersection, ray);

        var shaderColor = world.shade_hit(precalc);

        assertEquals(new Color(0.904984472, 0.904984472, 0.904984472), shaderColor);
    }

    @Test
    void colorWhenRayMisses() {
        var world = World.getDefault();
        var ray = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 1, 0));

        var color = world.color_at(ray);

        assertEquals(new Color(0, 0, 0), color);
    }

    @Test
    void colorWhenRayHits() {
        var world = World.getDefault();
        var ray = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1));

        var color = world.color_at(ray);

        assertEquals(new Color(0.380661193, 0.47582649, 0.28549589), color);
    }

    @Test
    void colorWhenIntersectionBehindRay() {
        var world = World.getDefault();
        var outer = world.getObjects().get(0);
        outer.setMaterial(Material.newMaterial().ambient(1).create());

        var inner = world.getObjects().get(1);
        inner.setMaterial(Material.newMaterial().ambient(1).create());

        var ray = new Ray(Tuple.point(0, 0, .75f), Tuple.vector(0, 0, -1));

        var color = world.color_at(ray);

        assertEquals(inner.material().color(), color);
    }

    @Test
    void noShadowWhenNothingIsCollinearWithPointAndLight() {
        var world = World.getDefault();
        var point = Tuple.point(0, 10, 0);

        ShadowResult shadowResult = world.getShadowResult(point);
        assertFalse(shadowResult.isShadowed());
    }

    @Test
    void isShadowWhenObjectBetweenPointAndLight() {
        var world = World.getDefault();
        var point = Tuple.point(10, -10, 10);

        assertTrue(world.getShadowResult(point).isShadowed());
    }

    @Test
    void noShadowWhenObjectBehindPointAndLight() {
        var world = World.getDefault();
        var point = Tuple.point(-20, 20, -20);

        assertFalse(world.getShadowResult(point).isShadowed());
    }

    @Test
    void noShadowWhenObjectBehindPoint() {
        var world = World.getDefault();
        var point = Tuple.point(-2, 2, -2);

        assertFalse(world.getShadowResult(point).isShadowed());
    }

    @Test
    void reflectedColorForNonReflectiveMaterial() {
        var world = World.getDefault();
        var ray = new Ray(Tuple.point(0, 0, 0), Tuple.vector(0, 0, 1));
        var shape = world.getObjects().get(1);
        shape.setMaterial(new Material.MaterialBuilder().ambient(1).create());

        var intersection = new Intersection(shape, 1);

        var precalc = new Precalc(intersection, ray);

        var color = world.reflectedColor(precalc, 0);

        assertEquals(new Color(0, 0, 0), color);

    }


    @Test
    void reflectedColorForReflectiveMaterial() {
        var world = World.getDefault();
        var plane = new Plane();
        plane.setMaterial(new Material.MaterialBuilder().reflectivity(0.5f).create());
        plane.setTransformationMatrix(MatrixProvider.translation(0, -1, 0));
        world.addObjects(plane);

        var ray = new Ray(Tuple.point(0, 0, -3), Tuple.vector(0,  (-Math.sqrt(2) / 2),  (Math.sqrt(2) / 2)));
        var intersection = new Intersection(plane,  Math.sqrt(2));
        var precalc = new Precalc(intersection, ray);

        var color = world.reflectedColor(precalc, 1);

        assertEquals(new Color(0.190332, 0.237915, 0.142749), color);
    }

    @Test
    void reflectedColorForReflectiveMaterialWithRemainingIterations() {
        var world = World.getDefault();
        var plane = new Plane();
        plane.setMaterial(new Material.MaterialBuilder().reflectivity(0.5f).create());
        plane.setTransformationMatrix(MatrixProvider.translation(0, -1, 0));
        world.addObjects(plane);

        var ray = new Ray(Tuple.point(0, 0, -3), Tuple.vector(0,  (-Math.sqrt(2) / 2),  (Math.sqrt(2) / 2)));
        var intersection = new Intersection(plane,  Math.sqrt(2));
        var precalc = new Precalc(intersection, ray);

        var color = world.reflectedColor(precalc, 0);

        assertEquals(new Color(0, 0, 0), color);
    }

    @Test
    void shadeHitWithReflectiveMaterial() {
        var world = World.getDefault();
        var plane = new Plane();
        plane.setMaterial(new Material.MaterialBuilder().reflectivity(0.5f).create());
        plane.setTransformationMatrix(MatrixProvider.translation(0, -1, 0));
        world.addObjects(plane);

        var ray = new Ray(Tuple.point(0, 0, -3), Tuple.vector(0,  (-Math.sqrt(2) / 2),  (Math.sqrt(2) / 2)));
        var intersection = new Intersection(plane,  Math.sqrt(2));
        var precalc = new Precalc(intersection, ray);

        var color = world.shade_hit(precalc);

        assertEquals(new Color(0.87677, 0.92436, 0.82918), color);
    }

    @Test
    void avoidInfiniteRecursionOnReflection() {
        var world = new World();
        world.setLightSource(new PointLight(RTPoint.point(0, 0, 0), Color.WHITE));

        var lowerPlane = new Plane();
        lowerPlane.setMaterial(new Material.MaterialBuilder().reflectivity(1).create());
        lowerPlane.setTransformationMatrix(MatrixProvider.translation(0, -1, 0));

        var upperPlane = new Plane();
        upperPlane.setMaterial(new Material.MaterialBuilder().reflectivity(1).create());
        upperPlane.setTransformationMatrix(MatrixProvider.translation(0, 1, 0));

        world.addObjects(lowerPlane, upperPlane);

        var ray = new Ray(RTPoint.point(0, 0, 0), RTVector.vector(0, 1, 0));

        var color = world.color_at(ray);

        // this is basically just to check that no infinite recursion happens
        assertEquals(color, color);
    }

    @Test
    void reflectedColorAtRecursionLimit() {
        var world = new World();
        var plane = new Plane();
        plane.setMaterial(new Material.MaterialBuilder().reflectivity(0.5f).create());
        plane.setTransformationMatrix(MatrixProvider.translation(0, -1, 0));
        world.addObjects(plane);

        var ray = new Ray(Tuple.point(0, 0, -3), Tuple.vector(0,  (-Math.sqrt(2) / 2),  (Math.sqrt(2) / 2)));
        var intersection = new Intersection(plane,  Math.sqrt(2));
        var precalc = new Precalc(intersection, ray);

        var color = world.reflectedColor(precalc, 0);

        assertEquals(new Color(0, 0, 0), color);
    }

    @Test
    void refractedColorForIntransparentMaterial() {
        var world = World.getDefault();
        var ray = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1));
        var shape = world.getObjects().get(1);

        var intersection1 = new Intersection(shape, 4);
        var intersection2 = new Intersection(shape, 6);
        List<Intersection> intersections = Arrays.asList(intersection1, intersection2);

        var precalc = new Precalc(intersection1, ray, intersections);

        var color = world.refractedColor(precalc, 5);

        assertEquals(new Color(0, 0, 0), color);

    }


    @Test
    void refractedColorForTransparentMaterialButNoRemainingBounces() {
        var world = World.getDefault();
        var ray = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1));
        var shape = world.getObjects().get(1);
        shape.setMaterial(Material.newMaterial().transparency(1.0).refractiveIndex(1.5).create());

        var intersection1 = new Intersection(shape, 4);
        var intersection2 = new Intersection(shape, 6);
        List<Intersection> intersections = Arrays.asList(intersection1, intersection2);

        var precalc = new Precalc(intersection1, ray, intersections);

        var color = world.refractedColor(precalc, 0);

        assertEquals(new Color(0, 0, 0), color);

    }

    @Test
    void refractedColorUnderTotalInternalReflection() {
        var world = World.getDefault();
        var shape = world.getObjects().get(1);

        shape.setMaterial(Material.newMaterial().transparency(1.0).refractiveIndex(1.5).create());

        var ray = new Ray(Tuple.point(0, 0,  Math.sqrt(2) / 2), Tuple.vector(0, 1, 0));
        var intersection1 = new Intersection(shape, - Math.sqrt(2) / 2);
        var intersection2 = new Intersection(shape,  Math.sqrt(2) / 2);
        List<Intersection> intersections = Arrays.asList(intersection1, intersection2);

        var precalc = new Precalc(intersection2, ray, intersections);

        var color = world.refractedColor(precalc, 5);

        assertEquals(Color.BLACK, color);
    }

    @Test
    void refractionRay() {
        var world = World.getDefault();
        var sphere1 = world.getObjects().get(0);
        var sphere2 = world.getObjects().get(1);

        sphere1.setMaterial(Material.newMaterial().ambient(1).pattern(new PatternTest.TestPattern()).create());
        sphere2.setMaterial(Material.newMaterial().transparency(1).refractiveIndex(1.5).create());

        var ray = new Ray(Tuple.point(0, 0, 0.1f), Tuple.vector(0, 1, 0));
        var intersection1 = new Intersection(sphere1, -0.9899f);
        var intersection2 = new Intersection(sphere2, -0.4899f);
        var intersection3 = new Intersection(sphere2, 0.4899f);
        var intersection4 = new Intersection(sphere1, 0.9899f);

        var intersections = Arrays.asList(intersection1, intersection2, intersection3, intersection4);

        var precalc = new Precalc(intersections.get(2), ray, intersections);

        var color = world.refractedColor(precalc, 5);

        assertEquals(new Color(0, 0.99888, 0.04725), color);

    }

    @Test
    void shadeHitWithTransparentMaterial() {
        var world = World.getDefault();
        var floor = new Plane();
        floor.setTransformationMatrix(MatrixProvider.translation(0, -1, 0));
        floor.setMaterial(Material.newMaterial().transparency(0.5).refractiveIndex(1.5).create());
        world.addObjects(floor);

        var ball = new Sphere();
        ball.setTransformationMatrix(MatrixProvider.translation(0, -3.5f, -0.5f));
        ball.setMaterial(Material.newMaterial().color(new Color(1,0,0)).ambient(0.5).create());
        world.addObjects(ball);

        var ray = new Ray(Tuple.point(0, 0, -3), Tuple.vector(0,  (-Math.sqrt(2) / 2),  (Math.sqrt(2) / 2)));
        var intersection = new Intersection(floor,  Math.sqrt(2));
        var precalc = new Precalc(intersection, ray);

        var color = world.shade_hit(precalc, 5);

        assertEquals(new Color(0.93642, 0.68642, 0.68642), color);
    }

    @Test
    void shadeHitWithTransparentAndReflectiveMaterial() {
        var world = World.getDefault();
        var ray = new Ray(Tuple.point(0, 0, -3), Tuple.vector(0,  (-Math.sqrt(2) / 2),  (Math.sqrt(2) / 2)));

        var floor = new Plane();
        floor.setTransformationMatrix(MatrixProvider.translation(0, -1, 0));
        floor.setMaterial(Material.newMaterial().reflectivity(0.5f).transparency(0.5).refractiveIndex(1.5).create());
        world.addObjects(floor);

        var ball = new Sphere();
        ball.setTransformationMatrix(MatrixProvider.translation(0, -3.5f, -0.5f));
        ball.setMaterial(Material.newMaterial().color(new Color(1,0,0)).ambient(0.5).create());
        world.addObjects(ball);

        var intersection = new Intersection(floor,  Math.sqrt(2));
        var precalc = new Precalc(intersection, ray);

        var color = world.shade_hit(precalc, 5);

        assertEquals(new Color(0.93391, 0.69643, 0.69243), color);

    }
}