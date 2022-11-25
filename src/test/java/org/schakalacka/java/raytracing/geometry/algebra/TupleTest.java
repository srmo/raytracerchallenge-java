package org.schakalacka.java.raytracing.geometry.algebra;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TupleTest {


    @Test
    void tupleIsPoint() {
        var tuple = Tuple.tuple(4.3, -4.2, 3.1, 1.0);

        assertEquals(4.3, tuple.x());
        assertEquals(-4.2, tuple.y());
        assertEquals(3.1, tuple.z());
        assertEquals(1.0, tuple.w());
        assertFalse(tuple.isVector());
        assertTrue(tuple.isPoint());

    }

    @Test
    void tupleIsVector() {
        var tuple = Tuple.tuple(4.3, -4.2, 3.1, 0.0);

        assertEquals(4.3, tuple.x());
        assertEquals(-4.2, tuple.y());
        assertEquals(3.1, tuple.z());
        assertEquals(0.0, tuple.w());
        assertTrue(tuple.isVector());
        assertFalse(tuple.isPoint());

    }

    @Test
    void addTuples() {
        var t1 = Tuple.tuple(3, -2, 5, 1);
        var t2 = Tuple.tuple(-2, 3, 1, 0);

        var sum = t1.add(t2);

        assertEquals(1, sum.x());
        assertEquals(1, sum.y());
        assertEquals(6, sum.z());
        assertEquals(1, sum.w());
    }

    @Test
    void subtractTuples() {
        var t1 = Tuple.tuple(3, -2, 5, 10);
        var t2 = Tuple.tuple(-2, 3, 1, 12);

        var result = t1.sub(t2);

        assertEquals(5, result.x());
        assertEquals(-5, result.y());
        assertEquals(4, result.z());
        assertEquals(-2, result.w());
    }

    @Test
    void negateTuple() {
        var tuple = Tuple.tuple(1, -2, 3, -4);

        var result = tuple.negate();

        assertEquals(-1, result.x());
        assertEquals(2, result.y());
        assertEquals(-3, result.z());
        assertEquals(4, result.w());
    }

    @Test
    void multiplyTuple() {
        var tuple = Tuple.tuple(1, -2, 3, -4);

        var result = tuple.mul(3.5);

        assertEquals(3.5, result.x());
        assertEquals(-7, result.y());
        assertEquals(10.5, result.z());
        assertEquals(-14, result.w());
    }

    @Test
    void divideTuple() {
        var tuple = Tuple.tuple(1, -2, 3, -4);

        var result = tuple.div(2);

        assertEquals(0.5, result.x());
        assertEquals(-1, result.y());
        assertEquals(1.5, result.z());
        assertEquals(-2, result.w());
    }

}