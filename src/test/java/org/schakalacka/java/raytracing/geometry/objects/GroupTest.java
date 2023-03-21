package org.schakalacka.java.raytracing.geometry.objects;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.geometry.tracing.Ray;
import org.schakalacka.java.raytracing.math.MatrixProvider;
import org.schakalacka.java.raytracing.math.Tuple;

import static org.junit.jupiter.api.Assertions.*;

class GroupTest {

    @Test
    void emptyGroup() {
        var g = new Group();
        assertEquals(MatrixProvider.get(4, true), g.getTransformationMatrix());
        assertEquals(0, g.getChildren().size());
    }

    @Test
    void defaultParentEmpty() {
        var s = new ShapeTest.TestShape();

        assertNull(s.getParent());
    }

    @Test
    void addChildToGroup() {
        var g = new Group();
        var s = new ShapeTest.TestShape();
        g.addChild(s);

        assertTrue(g.getChildren().contains(s));
        assertEquals(g, s.getParent());
    }

    @Test
    void intersectRayWithEmptyGroup() {
        var g = new Group();
        var r = new Ray(Tuple.point(0, 0, 0), Tuple.vector(0, 0, 1));

        var intersections = g.localIntersect(r);

        assertEquals(0, intersections.size());
    }

    @Test
    void intersectRayWithPopulatedGroup() {
        var g = new Group();
        var s1 = new Sphere();
        var s2 = new Sphere();
        s2.setTransformationMatrix(MatrixProvider.translation(0, 0, -3));

        var s3 = new Sphere();
        s3.setTransformationMatrix(MatrixProvider.translation(5, 0, 0));

        g.addChild(s1).addChild(s2).addChild(s3);

        var r = new Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1));
        var intersections = g.localIntersect(r);

        assertEquals(4, intersections.size());
        assertEquals(s2, intersections.get(0).getIntersectedObject());
        assertEquals(s2, intersections.get(1).getIntersectedObject());
        assertEquals(s1, intersections.get(2).getIntersectedObject());
        assertEquals(s1, intersections.get(3).getIntersectedObject());
    }

    @Test
    void intersectRayWithGroupTransformation() {
        var g = new Group();
        g.setTransformationMatrix(MatrixProvider.scaling(2, 2, 2));

        var s = new Sphere();
        s.setTransformationMatrix(MatrixProvider.translation(5, 0, 0));

        g.addChild(s);

        var ray = new Ray(Tuple.point(10, 0, -10), Tuple.vector(0, 0, 1));
        var intersections = g.intersect(ray);
        assertEquals(2, intersections.size());
    }


    @Test
    void findNormalOnChild() {
        var g1 = new Group();
        g1.setTransformationMatrix(MatrixProvider.rotationY(Math.PI / 2));

        var g2 = new Group();
        g2.setTransformationMatrix(MatrixProvider.scaling(1, 2, 3));

        g1.addChild(g2);

        var s = new Sphere();
        s.setTransformationMatrix(MatrixProvider.translation(5, 0, 0));

        g2.addChild(s);

        var n = s.normalVectorAt(Tuple.point(1.7321, 1.1547, -5.5774));

        assertEquals(Tuple.vector(0.2857f, 0.4286f, -0.8571f), n);
    }

}