package org.schakalacka.java.raytracing.geometry.objects;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.math.MatrixProvider;
import org.schakalacka.java.raytracing.math.Tuple;
import org.schakalacka.java.raytracing.geometry.tracing.Intersection;
import org.schakalacka.java.raytracing.geometry.tracing.Ray;
import org.schakalacka.java.raytracing.scene.Material;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

        assertEquals(Tuple.point(0, 0, -2.5f), shape.transformedRayFromLocalIntersect.origin());
        assertEquals(Tuple.vector(0, 0, 0.5f), shape.transformedRayFromLocalIntersect.direction());
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

        var normal = shape.normalVectorAt(Tuple.point(0, 1.70711f, -0.70711f));

        assertEquals(Tuple.vector(0, 0.70711f, -0.70711f), normal);
    }

    @Test
    void normalOnTransformedShape() {
        var shape = new TestShape();
        shape.setTransformationMatrix(MatrixProvider.scaling(1,0.5f,1).mulM(MatrixProvider.rotationZ( (Math.PI/5))));

        var normal = shape.normalVectorAt(Tuple.point(0,  (Math.sqrt(2)/2),  (-Math.sqrt(2)/2)));

        assertEquals(Tuple.vector(0,0.97014f,-0.24254f), normal);
    }

    /***
     * I'll follow the book here, b/c I find the idea interesting to use test-object specific fields that my tests can inspect
     */
    static class TestShape extends Shape {
        Ray transformedRayFromLocalIntersect;


        @Override
        public BoundingBox getBounds() {
            return new BoundingBox(Tuple.point(-1,-1,-1), Tuple.point(1,1,1));
        }

        @Override
        public List<Intersection> localIntersect(Ray ray) {
            this.transformedRayFromLocalIntersect = ray;
            return List.of();
        }

        @Override
        public Tuple localNormalVectorAt(Tuple point) {
            return Tuple.vector(point.x(), point.y(), point.z());
        }

    }

    @Test
    void defaultParentIsNull() {
        var s = new TestShape();
        assertNull(s.getParent());
    }

    @Test
    void convertPointFromWorldToObjectSpace() {
        var g1 = new Group();
        g1.setTransformationMatrix(MatrixProvider.rotationY(Math.PI/2));
        var g2 = new Group();
        g2.setTransformationMatrix(MatrixProvider.scaling(2,2,2));
        g1.addChild(g2);
        var s = new Sphere();
        s.setTransformationMatrix(MatrixProvider.translation(5,0,0));
        g2.addChild(s);

        var p = s.worldToObject(Tuple.point(-2,0,-10));

        assertEquals(Tuple.point(0,0,-1), p);
    }

    @Test
    void convertNormalFromObjectToWorldSpace() {
        var g1 = new Group();
        g1.setTransformationMatrix(MatrixProvider.rotationY(Math.PI/2));
        var g2 = new Group();
        g2.setTransformationMatrix(MatrixProvider.scaling(1,2,3));
        g1.addChild(g2);
        var s = new Sphere();
        s.setTransformationMatrix(MatrixProvider.translation(5,0,0));
        g2.addChild(s);

        var n = s.normalToWorld(Tuple.vector(Math.sqrt(3)/3, Math.sqrt(3)/3, Math.sqrt(3)/3));

        assertEquals(Tuple.vector(0.2857f, 0.4286f, -0.8571f), n);
    }

    @Test
    void shapeHasBounds() {
        var s = new TestShape();
        var expectedBounds = new BoundingBox(Tuple.point(-1,-1,-1), Tuple.point(1,1,1));
        var bounds = s.getBounds();

        assertEquals(expectedBounds, bounds);
    }

}