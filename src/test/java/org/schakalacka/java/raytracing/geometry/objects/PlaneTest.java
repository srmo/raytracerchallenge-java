package org.schakalacka.java.raytracing.geometry.objects;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.Constants;
import org.schakalacka.java.raytracing.geometry.tracing.Ray;
import org.schakalacka.java.raytracing.math.Tuple;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlaneTest {

    @Test
    void normalIsConstant() {
        var plane = new Plane();

        var n1 = plane.localNormalVectorAt(Tuple.point(0, 0, 0));
        var n2 = plane.localNormalVectorAt(Tuple.point(10, 0, -10));
        var n3 = plane.localNormalVectorAt(Tuple.point(-5, 0, 150));

        var expectedNormal = Tuple.vector(0, 1, 0);

        assertEquals(expectedNormal, n1);
        assertEquals(expectedNormal, n2);
        assertEquals(expectedNormal, n3);
    }

    @Test
    void intersectParallelRay() {
        var plane = new Plane();
        var ray = new Ray(Tuple.point(0, 10, 0), Tuple.vector(0, 0, 1));

        var intersections = plane.localIntersect(ray);

        assertEquals(0, intersections.size());
    }

    @Test
    void intersectCoplanar() {
        var plane = new Plane();
        var ray = new Ray(Tuple.point(0, 0, 0), Tuple.vector(0, 0, 1));

        var intersections = plane.localIntersect(ray);

        assertEquals(0, intersections.size());
    }

    @Test
    void intersectPlaneFromAbove() {
        var plane = new Plane();
        var ray = new Ray(Tuple.point(0, 1, 0), Tuple.vector(0, -1, 0));

        var intersections = plane.localIntersect(ray);

        assertEquals(1, intersections.size());
        assertEquals(1, intersections.get(0).getDistance());
        assertEquals(plane, intersections.get(0).getIntersectedObject());
    }

    @Test
    void intersectPlaneFromBelow() {
        var plane = new Plane();
        var ray = new Ray(Tuple.point(0, -1, 0), Tuple.vector(0, 1, 0));

        var intersections = plane.localIntersect(ray);

        assertEquals(1, intersections.size());
        assertEquals(1, intersections.get(0).getDistance());
        assertEquals(plane, intersections.get(0).getIntersectedObject());
    }

    @Test
    void bounds() {
        var plane = new Plane();
        var bounds = plane.getBounds();
        assertEquals(Tuple.point(Constants.NEGATIVE_INFINITY, 0, Constants.NEGATIVE_INFINITY), bounds.lower());
        assertEquals(Tuple.point(Constants.POSITIVE_INFINITY, 0, Constants.POSITIVE_INFINITY), bounds.upper());
    }
}