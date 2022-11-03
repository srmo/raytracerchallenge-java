package org.schakalacka.java.raytracing.geometry.tracing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.geometry.objects.Sphere;

import static org.junit.jupiter.api.Assertions.*;

class IntersectionTrackerTest {

    @BeforeEach
    void beforeEach() {
        IntersectionTracker.reset();
    }

    @Test
    void objectIsTracked() {
        var sphere = new Sphere();
        IntersectionTracker.register(new Intersection(sphere, 1));
        IntersectionTracker.register(new Intersection(sphere, 2));

        var result = IntersectionTracker.get(sphere);

        assertEquals(2, result.size());

        assertSame(sphere, result.get(0).getIntersectedObject());
        assertEquals(1, result.get(0).getDistance());

        assertSame(sphere, result.get(1).getIntersectedObject());
        assertEquals(2, result.get(1).getDistance());
    }

    @Test
    void getHitAllPositive() {
        var sphere = new Sphere();

        Intersection i1 = new Intersection(sphere, 1);
        Intersection i2 = new Intersection(sphere, 2);

        // make sure to register in reverse order, so we test the "sort" functionality in the tracker
        IntersectionTracker.register(i2);
        IntersectionTracker.register(i1);

        var intersections = IntersectionTracker.get(sphere);

        var hit = IntersectionTracker.getHit(intersections);

        assertNotNull(hit);
        assertEquals(i1, hit);
    }

    @Test
    void getHitSomeNegative() {
        var sphere = new Sphere();

        Intersection i1 = new Intersection(sphere, -1);
        Intersection i2 = new Intersection(sphere, 1);

        // make sure to register in reverse order, so we test the "sort" functionality in the tracker
        IntersectionTracker.register(i2);
        IntersectionTracker.register(i1);

        var intersections = IntersectionTracker.get(sphere);

        var hit = IntersectionTracker.getHit(intersections);

        assertNotNull(hit);
        assertEquals(i2, hit);
    }

    @Test
    void noHitWhenOnlyNegative() {
        var sphere = new Sphere();

        Intersection i1 = new Intersection(sphere, -2);
        Intersection i2 = new Intersection(sphere, -1);

        // make sure to register in reverse order, so we test the "sort" functionality in the tracker
        IntersectionTracker.register(i2);
        IntersectionTracker.register(i1);

        var intersections = IntersectionTracker.get(sphere);

        var hit = IntersectionTracker.getHit(intersections);

        assertNull(hit);
    }

    @Test
    void getHitMixed() {
        var sphere = new Sphere();

        Intersection i1 = new Intersection(sphere, 5);
        Intersection i2 = new Intersection(sphere, 7);
        Intersection i3 = new Intersection(sphere, -3);
        Intersection i4 = new Intersection(sphere, 2);

        // make sure to register in reverse order, so we test the "sort" functionality in the tracker
        IntersectionTracker.register(i1);
        IntersectionTracker.register(i2);
        IntersectionTracker.register(i3);
        IntersectionTracker.register(i4);

        var intersections = IntersectionTracker.get(sphere);

        var hit = IntersectionTracker.getHit(intersections);

        assertNotNull(hit);
        assertEquals(i4, hit);
    }

}