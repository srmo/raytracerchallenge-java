package org.schakalacka.java.raytracing.geometry.objects;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.Constants;
import org.schakalacka.java.raytracing.geometry.tracing.Ray;
import org.schakalacka.java.raytracing.math.Tuple;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConeTest {

    @Test
    void rayMissesCone() {
        // simulate a cone with a radius of 1 and a height of 1,
        // centered on the origin and pointing along the y-axis
        // this allows reuse of the cylinder test cases. We are basically using just the upper cone half,
        // i.e. the one with the tip pointing down
        var c = new Cone(0,1);

        var ray = new Ray(Tuple.point(1,0,0), Tuple.vector(0,1,0).normalize());

        assertEquals(0, c.localIntersect(ray).size());

        ray = new Ray(Tuple.point(0,0,0), Tuple.vector(0,1,0).normalize());
        assertEquals(0, c.localIntersect(ray).size());

        ray = new Ray(Tuple.point(0,0,-5), Tuple.vector(1,1,1).normalize());
        assertEquals(0, c.localIntersect(ray).size());
    }

    @Test
    void rayHitsCone() {
        var c = new Cone();
        var ray = new Ray(Tuple.point(0,0,-5), Tuple.vector(0,0,1).normalize());
        var intersections = c.localIntersect(ray);
        assertEquals(2, intersections.size());
        assertEquals(5, intersections.get(0).getDistance());
        assertEquals(5, intersections.get(1).getDistance());

        ray = new Ray(Tuple.point(0,0,-5), Tuple.vector(1,1,1).normalize());
        intersections = c.localIntersect(ray);
        assertEquals(2, intersections.size());
        assertEquals(8.66025f, intersections.get(0).getDistance(), Constants.EQUALS_EPSILON);
        assertEquals(8.66025f, intersections.get(1).getDistance(), Constants.EQUALS_EPSILON);

        ray = new Ray(Tuple.point(1,1,-5), Tuple.vector(-0.5f,-1,1).normalize());
        intersections = c.localIntersect(ray);
        assertEquals(2, intersections.size());
        assertEquals(4.55006f, intersections.get(0).getDistance(), Constants.EQUALS_EPSILON);
        assertEquals(49.44994f, intersections.get(1).getDistance(), Constants.EQUALS_EPSILON);


    }

    @Test
    void intersectParallelToOneConeHalf() {
        var c = new Cone();
        var ray = new Ray(Tuple.point(0,0,-1), Tuple.vector(0,1,1).normalize());

        var intersections = c.localIntersect(ray);
        assertEquals(1, intersections.size());
        assertEquals(0.35355f, intersections.get(0).getDistance(), Constants.EQUALS_EPSILON);
    }

    @Test
    void intersectCaps() {
        var c = new Cone(-0.5,0.5);
        c.setClosed(true);

        var ray = new Ray(Tuple.point(0,0,-5), Tuple.vector(0,1,0).normalize());
        var intersections = c.localIntersect(ray);
        assertEquals(0, intersections.size());

        ray = new Ray(Tuple.point(0,0,-0.25), Tuple.vector(0,1,1).normalize());
        intersections = c.localIntersect(ray);
        assertEquals(2, intersections.size());

        ray = new Ray(Tuple.point(0,0,-0.25), Tuple.vector(0,1,0).normalize());
        intersections = c.localIntersect(ray);
        assertEquals(4, intersections.size());

    }

    @Test
    void normal() {
        var c = new Cone();
        var n = c.localNormalVectorAt(Tuple.point(0,0,0));
        assertEquals(Tuple.vector(0,0,0), n);

        n = c.localNormalVectorAt(Tuple.point(1,1,1));
        assertEquals(Tuple.vector(1,-Math.sqrt(2),1), n);

        n = c.localNormalVectorAt(Tuple.point(-1,-1,0));
        assertEquals(Tuple.vector(-1,1,0), n);

    }


}