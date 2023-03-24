package org.schakalacka.java.raytracing.geometry.objects;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.Constants;
import org.schakalacka.java.raytracing.geometry.tracing.Ray;
import org.schakalacka.java.raytracing.math.Tuple;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CylinderTest {

    @Test
    void rayMissesCylinder() {
        var c = new Cylinder();
        var ray = new Ray(Tuple.point(1,0,0), Tuple.vector(0,1,0).normalize());

        assertEquals(0, c.localIntersect(ray).size());

        ray = new Ray(Tuple.point(0,0,0), Tuple.vector(0,1,0).normalize());
        assertEquals(0, c.localIntersect(ray).size());

        ray = new Ray(Tuple.point(0,0,-5), Tuple.vector(1,1,1).normalize());
        assertEquals(0, c.localIntersect(ray).size());
    }

    @Test
    void rayHitsCylinder() {
        var c = new Cylinder();
        var ray = new Ray(Tuple.point(1,0,-5), Tuple.vector(0,0,1).normalize());
        var intersections = c.localIntersect(ray);
        assertEquals(2, intersections.size());
        assertEquals(5, intersections.get(0).getDistance());
        assertEquals(5, intersections.get(1).getDistance());

        ray = new Ray(Tuple.point(0,0,-5), Tuple.vector(0,0,1).normalize());
        intersections = c.localIntersect(ray);
        assertEquals(2, intersections.size());
        assertEquals(4, intersections.get(0).getDistance());
        assertEquals(6, intersections.get(1).getDistance());


        ray = new Ray(Tuple.point(0.5f,0,-5), Tuple.vector(0.1f,1,1).normalize());
        intersections = c.localIntersect(ray);
        assertEquals(2, intersections.size());
        assertEquals(6.80798f, intersections.get(0).getDistance(), Constants.EQUALS_EPSILON);
        assertEquals(7.08872f, intersections.get(1).getDistance(), Constants.EQUALS_EPSILON);

    }

    @Test
    void findNormal() {
        var c = new Cylinder();
        var normal = c.localNormalVectorAt(Tuple.point(1,0,0));
        assertEquals(Tuple.vector(1,0,0), normal);

        normal = c.localNormalVectorAt(Tuple.point(0,5,-1));
        assertEquals(Tuple.vector(0,0,-1), normal);

        normal = c.localNormalVectorAt(Tuple.point(0,-2,1));
        assertEquals(Tuple.vector(0,0,1), normal);

        normal = c.localNormalVectorAt(Tuple.point(-1,1,0));
        assertEquals(Tuple.vector(-1,0,0), normal);
    }

    @Test
    void defaultMinimumAndMaximum() {
        var c = new Cylinder();
        assertEquals(Float.NEGATIVE_INFINITY, c.getMinimum());
        assertEquals(Float.POSITIVE_INFINITY, c.getMaximum());
    }

    @Test
    void intersectConstrainedCylinder() {
        var c = new Cylinder(1,2);
        var ray = new Ray(Tuple.point(0,1.5f,-5), Tuple.vector(0.1f,1,1).normalize());
        var intersections = c.localIntersect(ray);
        assertEquals(0, intersections.size());

        ray = new Ray(Tuple.point(0,3,-5), Tuple.vector(0,0,1).normalize());
        intersections = c.localIntersect(ray);
        assertEquals(0, intersections.size());

        ray = new Ray(Tuple.point(0,0,-5), Tuple.vector(0,0,1).normalize());
        intersections = c.localIntersect(ray);
        assertEquals(0, intersections.size());

        ray = new Ray(Tuple.point(0,2,-5), Tuple.vector(0,0,1).normalize());
        intersections = c.localIntersect(ray);
        assertEquals(0, intersections.size());

        ray = new Ray(Tuple.point(0,1,-5), Tuple.vector(0,0,1).normalize());
        intersections = c.localIntersect(ray);
        assertEquals(0, intersections.size());

        ray = new Ray(Tuple.point(0,1.5f,-2), Tuple.vector(0,0,1).normalize());
        intersections = c.localIntersect(ray);
        assertEquals(2, intersections.size());
    }

    @Test
    void cylinderDefaultNotClosed() {
        var c = new Cylinder();
        assertFalse(c.isClosed());
    }

    @Test
    void intersectClosedCylinder() {
        var c = new Cylinder(1,2);
        c.setClosed(true);
        var ray = new Ray(Tuple.point(0,3,0), Tuple.vector(0,-1,0).normalize());
        var intersections = c.localIntersect(ray);
        assertEquals(2, intersections.size());

        ray = new Ray(Tuple.point(0,3,-2), Tuple.vector(0,-1,2).normalize());
        intersections = c.localIntersect(ray);
        assertEquals(2, intersections.size());

        ray = new Ray(Tuple.point(0,4,-2), Tuple.vector(0,-1,1).normalize());
        intersections = c.localIntersect(ray);
        assertEquals(2, intersections.size());

        ray = new Ray(Tuple.point(0,0,-2), Tuple.vector(0,1,2).normalize());
        intersections = c.localIntersect(ray);
        assertEquals(2, intersections.size());

        ray = new Ray(Tuple.point(0,-1,-2), Tuple.vector(0,1,1).normalize());
        intersections = c.localIntersect(ray);
        assertEquals(2, intersections.size());
    }

    @Test
    void normalAtCaps() {
        var c = new Cylinder(1,2);
        c.setClosed(true);
        var normal = c.localNormalVectorAt(Tuple.point(0,1,0));
        assertEquals(Tuple.vector(0,-1,0), normal);

        normal = c.localNormalVectorAt(Tuple.point(0.5,1,0));
        assertEquals(Tuple.vector(0,-1,0), normal);

        normal = c.localNormalVectorAt(Tuple.point(0,1,0.5f));
        assertEquals(Tuple.vector(0,-1,0), normal);

        normal = c.localNormalVectorAt(Tuple.point(0,2,0));
        assertEquals(Tuple.vector(0,1,0), normal);

        normal = c.localNormalVectorAt(Tuple.point(0.5,2,0));
        assertEquals(Tuple.vector(0,1,0), normal);

        normal = c.localNormalVectorAt(Tuple.point(0,2,0.5));
        assertEquals(Tuple.vector(0,1,0), normal);
    }

    @Test
    void boundsForNontruncatedCylinder() {
        var c = new Cylinder();
        var bounds = c.getBounds();

        assertEquals(Tuple.point(-1,Double.NEGATIVE_INFINITY, -1), bounds.lower());
        assertEquals(Tuple.point(1,Double.POSITIVE_INFINITY, 1), bounds.upper());
    }

    @Test
    void boundsForUpperBoundCylinder() {
        var c = new Cylinder(Double.NEGATIVE_INFINITY, 2);

        assertEquals(Tuple.point(-1,Double.NEGATIVE_INFINITY, -1), c.getBounds().lower());
        assertEquals(Tuple.point(1,2, 1), c.getBounds().upper());
    }

    @Test
    void boundsForLowerBoundCylinder() {
        var c = new Cylinder(-2, Double.POSITIVE_INFINITY);

        assertEquals(Tuple.point(-1,-2, -1), c.getBounds().lower());
        assertEquals(Tuple.point(1,Double.POSITIVE_INFINITY, 1), c.getBounds().upper());
    }

    @Test
    void boundsForBoundCylinder() {
        var c = new Cylinder(-2, 2);

        assertEquals(Tuple.point(-1,-2, -1), c.getBounds().lower());
        assertEquals(Tuple.point(1,2, 1), c.getBounds().upper());
    }

}