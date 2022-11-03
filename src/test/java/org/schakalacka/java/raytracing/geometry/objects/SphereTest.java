package org.schakalacka.java.raytracing.geometry.objects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.geometry.Matrix;
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

        assertEquals(2, intersection.size());
        assertEquals(4, intersection.get(0).getDistance());
        assertEquals(6, intersection.get(1).getDistance());
    }

    @Test
    void intersectTangent() {
        var ray = new Ray(Tuple.point(0, 1, -5), Tuple.vector(0, 0, 1));
        var sphere = new Sphere();

        var intersection = sphere.intersect(ray);

        assertEquals(2, intersection.size());
        assertEquals(5, intersection.get(0).getDistance());
        assertEquals(5, intersection.get(1).getDistance());
    }

    @Test
    void intersectNone() {
        var ray = new Ray(Tuple.point(0, 2, -5), Tuple.vector(0, 0, 1));
        var sphere = new Sphere();

        var intersection = sphere.intersect(ray);

        assertEquals(0, intersection.size());
    }

    @Test
    void intersectOriginInSphere() {
        var ray = new Ray(Tuple.point(0, 0, 0), Tuple.vector(0, 0, 1));
        var sphere = new Sphere();

        var intersection = sphere.intersect(ray);

        assertEquals(2, intersection.size());
        assertEquals(-1, intersection.get(0).getDistance());
        assertEquals(1, intersection.get(1).getDistance());
    }

    @Test
    void intersectOriginBehindSphere() {
        var ray = new Ray(Tuple.point(0, 0, 5), Tuple.vector(0, 0, 1));
        var sphere = new Sphere();

        var intersection = sphere.intersect(ray);

        assertEquals(2, intersection.size());
        assertEquals(-6, intersection.get(0).getDistance());
        assertEquals(-4, intersection.get(1).getDistance());
    }

    @Test
    void defaultTransformation() {
        var sphereMatrix = new Sphere().getTransformationMatrix();

        assertEquals(Matrix.IDENTITY_MATRIX_4, sphereMatrix);
    }

    @Test
    void setTransformation() {
        var sphere = new Sphere();
        var sphereMatrix = Matrix.translation(2, 3, 4);
        sphere.setTransformationMatrix(sphereMatrix);

        var expectedSphereMatrix = Matrix.translation(2, 3, 4);

        assertEquals(expectedSphereMatrix, sphereMatrix);
    }

    @Test
    void intersectUsesScaling() {
        var ray = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1));
        var sphere = new Sphere();
        sphere.setTransformationMatrix(Matrix.scaling(2, 2, 2));

        var intersections = sphere.intersect(ray);

        assertEquals(2, intersections.size());
        assertEquals(3, intersections.get(0).getDistance());
        assertEquals(7, intersections.get(1).getDistance());
    }

}