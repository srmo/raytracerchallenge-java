package org.schakalacka.java.raytracing.geometry.objects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.geometry.Tuple;
import org.schakalacka.java.raytracing.geometry.tracing.Ray;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SphereTest {

    @Test
    void unitSphere() {
        var sphere = new Sphere();

        Assertions.assertEquals(Tuple.point(0, 0, 0), sphere.position());
        assertEquals(1, sphere.radius());
    }

    @Test
    void intersect2Forward() {
        var ray = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1));
        var sphere = new Sphere();

        var intersection = sphere.intersect(ray);

        assertEquals(2, intersection.length);
        assertEquals(4, intersection[0].getDistance());
        assertEquals(6, intersection[1].getDistance());
    }

    @Test
    void intersectTangent() {
        var ray = new Ray(Tuple.point(0, 1, -5), Tuple.vector(0, 0, 1));
        var sphere = new Sphere();

        var intersection = sphere.intersect(ray);

        assertEquals(2, intersection.length);
        assertEquals(5, intersection[0].getDistance());
        assertEquals(5, intersection[1].getDistance());
    }

    @Test
    void intersectNone() {
        var ray = new Ray(Tuple.point(0, 2, -5), Tuple.vector(0, 0, 1));
        var sphere = new Sphere();

        var intersection = sphere.intersect(ray);

        assertEquals(0, intersection.length);
    }

    @Test
    void intersectOriginInSphere() {
        var ray = new Ray(Tuple.point(0, 0, 0), Tuple.vector(0, 0, 1));
        var sphere = new Sphere();

        var intersection = sphere.intersect(ray);

        assertEquals(2, intersection.length);
        assertEquals(-1, intersection[0].getDistance());
        assertEquals(1, intersection[1].getDistance());
    }

    @Test
    void intersectOriginBehindSphere() {
        var ray = new Ray(Tuple.point(0, 0, 5), Tuple.vector(0, 0, 1));
        var sphere = new Sphere();

        var intersection = sphere.intersect(ray);

        assertEquals(2, intersection.length);
        assertEquals(-6, intersection[0].getDistance());
        assertEquals(-4, intersection[1].getDistance());
    }

}