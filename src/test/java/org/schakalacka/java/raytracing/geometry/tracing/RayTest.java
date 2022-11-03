package org.schakalacka.java.raytracing.geometry.tracing;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.geometry.Tuple;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RayTest {

    @Test
    void createRay() {
        var ray = new Ray(Tuple.point(1, 2, 3), Tuple.vector(4, 5, 6));

        assertEquals(Tuple.point(1, 2, 3), ray.origin());
        assertEquals(Tuple.vector(4, 5, 6), ray.direction());
    }

    @Test
    void positionOverTime() {
        var ray = new Ray(Tuple.point(2,3,4), Tuple.vector(1,0,0));

        assertEquals(Tuple.point(2,3,4), ray.position(0));
        assertEquals(Tuple.point(3,3,4), ray.position(1));
        assertEquals(Tuple.point(1,3,4), ray.position(-1));
        assertEquals(Tuple.point(4.5f,3,4), ray.position(2.5));
    }


}