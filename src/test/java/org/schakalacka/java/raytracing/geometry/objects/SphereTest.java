package org.schakalacka.java.raytracing.geometry.objects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.geometry.algebra.Tuple;
import org.schakalacka.java.raytracing.geometry.tracing.Ray;

import static org.junit.jupiter.api.Assertions.*;

public class SphereTest {

    @Test
    void sphereIsGeometryObject() {
        assertInstanceOf(Shape.class, new Sphere());
    }

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

        var intersection = sphere.localIntersect(ray);

        assertEquals(2, intersection.size());
        assertEquals(4, intersection.get(0).getDistance());
        assertEquals(6, intersection.get(1).getDistance());
    }

    @Test
    void intersectTangent() {
        var ray = new Ray(Tuple.point(0, 1, -5), Tuple.vector(0, 0, 1));
        var sphere = new Sphere();

        var intersection = sphere.localIntersect(ray);

        assertEquals(2, intersection.size());
        assertEquals(5, intersection.get(0).getDistance());
        assertEquals(5, intersection.get(1).getDistance());
    }

    @Test
    void intersectNone() {
        var ray = new Ray(Tuple.point(0, 2, -5), Tuple.vector(0, 0, 1));
        var sphere = new Sphere();

        var intersection = sphere.localIntersect(ray);

        assertEquals(0, intersection.size());
    }

    @Test
    void intersectOriginInSphere() {
        var ray = new Ray(Tuple.point(0, 0, 0), Tuple.vector(0, 0, 1));
        var sphere = new Sphere();

        var intersection = sphere.localIntersect(ray);

        assertEquals(2, intersection.size());
        assertEquals(-1, intersection.get(0).getDistance());
        assertEquals(1, intersection.get(1).getDistance());
    }

    @Test
    void intersectOriginBehindSphere() {
        var ray = new Ray(Tuple.point(0, 0, 5), Tuple.vector(0, 0, 1));
        var sphere = new Sphere();

        var intersection = sphere.localIntersect(ray);

        assertEquals(2, intersection.size());
        assertEquals(-6, intersection.get(0).getDistance());
        assertEquals(-4, intersection.get(1).getDistance());
    }

    @Test
    void normalOnX() {
        var sphere = new Sphere();
        var normal = sphere.localNormalVectorAt(Tuple.point(1, 0, 0));

        assertEquals(Tuple.vector(1, 0, 0), normal);
    }

    @Test
    void normalOnY() {
        var sphere = new Sphere();
        var normal = sphere.localNormalVectorAt(Tuple.point(0, 1, 0));

        assertEquals(Tuple.vector(0, 1, 0), normal);
    }

    @Test
    void normalOnZ() {
        var sphere = new Sphere();
        var normal = sphere.localNormalVectorAt(Tuple.point(0, 0, 1));

        assertEquals(Tuple.vector(0, 0, 1), normal);
    }

    @Test
    void normalNotOnAxis() {
        var sphere = new Sphere();
        var normal = sphere.localNormalVectorAt(Tuple.point(Math.sqrt(3) / 3, Math.sqrt(3) / 3, Math.sqrt(3) / 3));

        assertEquals(Tuple.vector(Math.sqrt(3) / 3, Math.sqrt(3) / 3, Math.sqrt(3) / 3), normal);
    }

    @Test
    void normalIsAlreadyNormalized() {
        var sphere = new Sphere();
        var normal = sphere.localNormalVectorAt(Tuple.point(Math.sqrt(3) / 3, Math.sqrt(3) / 3, Math.sqrt(3) / 3));

        var normalized = normal.normalize();
        assertNotSame(normal, normalized);
        assertEquals(normal, normalized);
    }
}