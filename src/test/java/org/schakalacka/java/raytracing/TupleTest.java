package org.schakalacka.java.raytracing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TupleTest {


    @Test
    void tupleIsPoint() {
        var tuple = Tuple.tuple(4.3f, -4.2f, 3.1f, 1.0f);

        assertEquals(4.3f, tuple.x());
        assertEquals(-4.2f, tuple.y());
        assertEquals(3.1f, tuple.z());
        assertEquals(1.0f, tuple.w());
        assertFalse(tuple.isVector());
        assertTrue(tuple.isPoint());

    }

    @Test
    void tupleIsVector() {
        var tuple = Tuple.tuple(4.3f, -4.2f, 3.1f, 0.0f);

        assertEquals(4.3f, tuple.x());
        assertEquals(-4.2f, tuple.y());
        assertEquals(3.1f, tuple.z());
        assertEquals(0.0f, tuple.w());
        assertTrue(tuple.isVector());
        assertFalse(tuple.isPoint());

    }

    @Test
    void pointIsTuple() {
        var point = Tuple.point(4f, -4f, 3);

        assertEquals(4f, point.x());
        assertEquals(-4f, point.y());
        assertEquals(3f, point.z());
        assertFalse(point.isVector());
        assertTrue(point.isPoint());
    }

    @Test
    void vectorIsTuple() {
        var point = Tuple.vector(4f, -4f, 3);

        assertEquals(4f, point.x());
        assertEquals(-4f, point.y());
        assertEquals(3f, point.z());
        assertTrue(point.isVector());
        assertFalse(point.isPoint());
    }

    @Test
    void addTuples() {
        var t1 = Tuple.tuple(3, -2, 5, 1);
        var t2 = Tuple.tuple(-2, 3, 1, 0);

        var sum = t1.add(t2);

        assertEquals(1f, sum.x());
        assertEquals(1f, sum.y());
        assertEquals(6f, sum.z());
        assertEquals(1f, sum.w());
    }

    @Test
    void addPoints() {
        var t1 = Tuple.point(3, -2, 5);
        var t2 = Tuple.point(-2, 3, 1);

        Exception e = assertThrows(ArithmeticException.class, () -> t1.add(t2));
        assertEquals("Can't add two Points", e.getMessage());
    }

    @Test
    void addVectors() {
        var t1 = Tuple.vector(3, -2, 5);
        var t2 = Tuple.vector(-2, 3, 1);

        var sum = t1.add(t2);
        System.out.println(sum.getClass());
        assertEquals(1f, sum.x());
        assertEquals(1f, sum.y());
        assertEquals(6f, sum.z());
        assertTrue(sum.isVector());
    }

    @Test
    void addVectorAndPoint() {
        var t1 = Tuple.vector(3, -2, 5);
        var t2 = Tuple.point(-2, 3, 1);

        var sum = t1.add(t2);
        System.out.println(sum.getClass());
        assertEquals(1f, sum.x());
        assertEquals(1f, sum.y());
        assertEquals(6f, sum.z());
        assertTrue(sum.isPoint());
    }

    @Test
    void addPointAndVector() {
        var t1 = Tuple.point(3, -2, 5);
        var t2 = Tuple.vector(-2, 3, 1);

        var sum = t1.add(t2);

        assertEquals(1f, sum.x());
        assertEquals(1f, sum.y());
        assertEquals(6f, sum.z());
        assertTrue(sum.isPoint());
    }

    @Test
    void subtractPoints() {
        var t1 = Tuple.point(3, -2, 5);
        var t2 = Tuple.point(-2, 3, 1);

        var result = t1.sub(t2);

        assertEquals(5f, result.x());
        assertEquals(-5f, result.y());
        assertEquals(4f, result.z());
        assertTrue(result.isVector());
    }

}