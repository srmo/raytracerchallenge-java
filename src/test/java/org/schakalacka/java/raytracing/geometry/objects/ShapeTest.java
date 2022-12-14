package org.schakalacka.java.raytracing.geometry.objects;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.geometry.algebra.MatrixProvider;
import org.schakalacka.java.raytracing.geometry.algebra.Tuple;
import org.schakalacka.java.raytracing.geometry.tracing.Intersection;
import org.schakalacka.java.raytracing.geometry.tracing.Ray;
import org.schakalacka.java.raytracing.scene.Material;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class ShapeTest {

    @Test
    void defaultTransformation() {
        var shape = new TestShape();

        var matrix = shape.getTransformationMatrix();

        assertEquals(MatrixProvider.get(4, true), matrix);
    }

    @Test
    void setTransformMatrix() {
        var shape = new TestShape();
        shape.setTransformationMatrix(MatrixProvider.translation(2, 3, 4));

        assertEquals(MatrixProvider.translation(2, 3, 4), shape.getTransformationMatrix());
    }

    @Test
    void defaultMaterial() {
        var shape = new TestShape();

        assertEquals(Material.newMaterial().create(), shape.material());
    }

    @Test
    void setMaterial() {
        var shape = new TestShape();
        var material = Material.newMaterial().ambient(1).create();
        shape.setMaterial(material);

        assertEquals(Material.newMaterial().ambient(1).create(), shape.material());
        assertSame(material, shape.material());

    }

    @Test
    void intersectScaledShape() {
        var shape = new TestShape();
        var ray = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1));

        shape.setTransformationMatrix(MatrixProvider.scaling(2, 2, 2));
        shape.intersect(ray);

        assertEquals(Tuple.point(0, 0, -2.5), shape.transformedRayFromLocalIntersect.origin());
        assertEquals(Tuple.vector(0, 0, 0.5), shape.transformedRayFromLocalIntersect.direction());
    }

    @Test
    void intersectTranslatedShape() {
        var shape = new TestShape();
        var ray = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1));

        shape.setTransformationMatrix(MatrixProvider.translation(5, 0, 0));
        shape.intersect(ray);

        assertEquals(Tuple.point(-5, 0, -5), shape.transformedRayFromLocalIntersect.origin());
        assertEquals(Tuple.vector(0, 0, 1), shape.transformedRayFromLocalIntersect.direction());
    }

    @Test
    void normalOnTranslatedShape() {
        var shape = new TestShape();
        shape.setTransformationMatrix(MatrixProvider.translation(0, 1, 0));

        var normal = shape.normalVectorAt(Tuple.point(0, 1.70711, -0.70711));

        assertEquals(Tuple.vector(0, 0.70711, -0.70711), normal);
    }

    @Test
    void normalOnTransformedShape() {
        var shape = new TestShape();
        shape.setTransformationMatrix(MatrixProvider.scaling(1,0.5,1).mulM(MatrixProvider.rotationZ(Math.PI/5)));

        var normal = shape.normalVectorAt(Tuple.point(0, Math.sqrt(2)/2, -Math.sqrt(2)/2));

        assertEquals(Tuple.vector(0,0.97014,-0.24254), normal);
    }

    /***
     * I'll follow the book here, b/c I find the idea interesting to use test-object specific fields that my tests can inspect
     */
    static class TestShape extends Shape {
        Ray transformedRayFromLocalIntersect;

        @Override
        public List<Intersection> localIntersect(Ray ray) {
            this.transformedRayFromLocalIntersect = ray;
            return null;
        }

        @Override
        public Tuple localNormalVectorAt(Tuple point) {
            return Tuple.vector(point.x(), point.y(), point.z());
        }

    }


}