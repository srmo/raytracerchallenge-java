package org.schakalacka.java.raytracing.geometry.objects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.geometry.Matrix;
import org.schakalacka.java.raytracing.geometry.Tuple;
import org.schakalacka.java.raytracing.geometry.tracing.Ray;
import org.schakalacka.java.raytracing.scene.Material;

import static org.junit.jupiter.api.Assertions.*;

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

        assertEquals(Matrix.get(4, true), sphereMatrix);
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

    @Test
    void normalOnX() {
        var sphere = new Sphere();
        var normal = sphere.normal(Tuple.point(1, 0, 0));

        assertEquals(Tuple.vector(1, 0, 0), normal);
    }

    @Test
    void normalOnY() {
        var sphere = new Sphere();
        var normal = sphere.normal(Tuple.point(0, 1, 0));

        assertEquals(Tuple.vector(0, 1, 0), normal);
    }

    @Test
    void normalOnZ() {
        var sphere = new Sphere();
        var normal = sphere.normal(Tuple.point(0, 0, 1));

        assertEquals(Tuple.vector(0, 0, 1), normal);
    }

    @Test
    void normalNotOnAxis() {
        var sphere = new Sphere();
        var normal = sphere.normal(Tuple.point(Math.cbrt(3) / 3, Math.cbrt(3) / 3, Math.cbrt(3) / 3));

        assertEquals(Tuple.vector(Math.cbrt(3) / 3, Math.cbrt(3) / 3, Math.cbrt(3) / 3), normal);
    }

    @Test
    void normalIsAlreadyNormalized() {
        var sphere = new Sphere();
        var normal = sphere.normal(Tuple.point(Math.cbrt(3) / 3, Math.cbrt(3) / 3, Math.cbrt(3) / 3));

        var normalized = normal.normalize();
        assertNotSame(normal, normalized);
        assertEquals(normal, normalized);
    }

    @Test
    void normalOnTranslatedSphere() {
        var sphere = new Sphere();
        sphere.setTransformationMatrix(Matrix.translation(0,1,0));

        var normal = sphere.normal(Tuple.point(0,1.70711,-0.70711));

        assertEquals(Tuple.vector(0,0.7071067,-0.7071678), normal);
    }

    @Test
    void normalOnTransformedSphere() {
        var sphere = new Sphere();

        Matrix transformationMatrix = Matrix.scaling(1,0.5,1).mulM(Matrix.rotationZ(Math.PI/5));
        sphere.setTransformationMatrix(transformationMatrix);

        var normal = sphere.normal(Tuple.point(0, Math.sqrt(2)/2, -Math.sqrt(2)/2));

        assertEquals(Tuple.vector(0,0.97014,-0.24254), normal);
    }

    @Test
    void sphereHasDefaultMaterial() {
        var sphere = new Sphere();
        var material = sphere.material();

        assertNotNull(material);
    }

    @Test
    void sphereMaterial() {
        var sphere = new Sphere();
        var material = Material.newMaterial().ambient(1.0).create();
        sphere.setMaterial(material);

        assertSame(material, sphere.material());
        assertEquals(1, sphere.material().ambient());
    }



}