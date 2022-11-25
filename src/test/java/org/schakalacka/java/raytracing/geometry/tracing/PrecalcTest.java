package org.schakalacka.java.raytracing.geometry.tracing;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.geometry.algebra.Tuple;
import org.schakalacka.java.raytracing.geometry.objects.Sphere;

import static org.junit.jupiter.api.Assertions.*;

class PrecalcTest {

    @Test
    void precalc() {
        var ray = new Ray(Tuple.point(0,0,-5), Tuple.vector(0,0,1));
        var sphere = new Sphere();
        var intersection = new Intersection(sphere, 4);

        var precalc = new Precalc(intersection, ray);

        assertEquals(4, precalc.getDistance());
        assertSame(sphere, precalc.getObject());
        assertEquals(Tuple.point(0,0,-1), precalc.getPoint());
        assertEquals(Tuple.vector(0,0,-1), precalc.getEyeVector());
        assertEquals(Tuple.vector(0,0,-1), precalc.getNormalVector());
    }



    @Test
    void hitOutside() {
        var ray = new Ray(Tuple.point(0,0,-5), Tuple.vector(0,0,1));
        var sphere = new Sphere();
        var intersection = new Intersection(sphere, 4);

        var precalc = new Precalc(intersection, ray);
        assertFalse(precalc.inside());

    }

    @Test
    void hitInside() {
        var ray = new Ray(Tuple.point(0,0,0), Tuple.vector(0,0,1));
        var sphere = new Sphere();
        var intersection = new Intersection(sphere, 4);

        var precalc = new Precalc(intersection, ray);
        assertTrue(precalc.inside());

    }

}