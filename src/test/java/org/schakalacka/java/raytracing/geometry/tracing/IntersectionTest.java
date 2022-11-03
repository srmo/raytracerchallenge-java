package org.schakalacka.java.raytracing.geometry.tracing;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.geometry.objects.Sphere;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class IntersectionTest {

    @Test
    void intersection() {
        var sphere = new Sphere();
        var intersection = new Intersection(sphere, 6);

        // need real object equality
        assertSame(sphere, intersection.getIntersectedObject());
        assertEquals(6, intersection.getDistance());
    }


}