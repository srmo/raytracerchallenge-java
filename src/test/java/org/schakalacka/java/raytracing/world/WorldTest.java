package org.schakalacka.java.raytracing.world;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.math.MatrixProvider;
import org.schakalacka.java.raytracing.math.Tuple;
import org.schakalacka.java.raytracing.geometry.objects.Sphere;
import org.schakalacka.java.raytracing.geometry.tracing.Intersection;
import org.schakalacka.java.raytracing.geometry.tracing.Precalc;
import org.schakalacka.java.raytracing.geometry.tracing.Ray;
import org.schakalacka.java.raytracing.scene.Color;
import org.schakalacka.java.raytracing.scene.Material;
import org.schakalacka.java.raytracing.scene.PointLight;

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
        world.setLightSource(new PointLight(Tuple.point(0,0,-10), new Color(1,1,1)));
        var sphere1 = new Sphere();
        var sphere2 = new Sphere();
        sphere2.setTransformationMatrix(MatrixProvider.translation(0,0,10));

        world.addObjects(sphere1, sphere2);

        var ray = new Ray(Tuple.point(0,0,5), Tuple.vector(0,0,1));
        var intersection = new Intersection(sphere2, 4);

        var precalcs = new Precalc(intersection, ray);
        var shadeHit = world.shade_hit(precalcs);

        assertEquals(new Color(0.1,0.1,0.1), shadeHit);
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

        assertFalse(world.isShadowed(point));
    }

    @Test
    void isShadowWhenObjectBetweenPointAndLight() {
        var world = World.getDefault();
        var point = Tuple.point(10, -10, 10);

        assertTrue(world.isShadowed(point));
    }

    @Test
    void noShadowWhenObjectBehindPointAndLight() {
        var world = World.getDefault();
        var point = Tuple.point(-20, 20, -20);

        assertFalse(world.isShadowed(point));
    }

    @Test
    void noShadowWhenObjectBehindPoint() {
        var world = World.getDefault();
        var point = Tuple.point(-2, 2, -2);

        assertFalse(world.isShadowed(point));
    }


}