package org.schakalacka.java.raytracing.geometry.objects;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.Constants;
import org.schakalacka.java.raytracing.geometry.tracing.Ray;
import org.schakalacka.java.raytracing.math.Tuple;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

}