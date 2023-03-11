package org.schakalacka.java.raytracing.math;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.Constants;

import static org.junit.jupiter.api.Assertions.*;

class RTPointTest {


    @Test
    void pointIsPoint() {
        var tuple = Tuple.point(4.3f, -4.2f, 3.1f);

        assertEquals(4.3, tuple.x(), Constants.EQUALS_EPSILON);
        assertEquals(-4.2, tuple.y(), Constants.EQUALS_EPSILON);
        assertEquals(3.1, tuple.z(), Constants.EQUALS_EPSILON);
        assertEquals(1.0, tuple.w(),  Constants.EQUALS_EPSILON);
        assertFalse(tuple.isVector());
        assertTrue(tuple.isPoint());

    }

    @Test
    void addPoints() {
        var t1 = Tuple.point(3, -2, 5);
        var t2 = Tuple.point(-2, 3, 1);

        Exception e = assertThrows(ArithmeticException.class, () -> t1.add(t2));
        assertEquals("Can't add two points", e.getMessage());
    }

    @Test
    void addPointAndVector() {
        var t1 = Tuple.point(3, -2, 5);
        var t2 = Tuple.vector(-2, 3, 1);

        var sum = t1.add(t2);

        assertEquals(1, sum.x());
        assertEquals(1, sum.y());
        assertEquals(6, sum.z());
        assertTrue(sum.isPoint());
    }

    @Test
    void subtractPoints() {
        var t1 = Tuple.point(3, -2, 5);
        var t2 = Tuple.point(-2, 3, 1);

        var result = t1.sub(t2);

        assertEquals(5, result.x());
        assertEquals(-5, result.y());
        assertEquals(4, result.z());
        assertTrue(result.isVector());
    }

    @Test
    void subtractVectorFromPoint() {
        var point = Tuple.point(3, 2, 1);
        var vector = Tuple.vector(5, 6, 7);

        var result = point.sub(vector);

        assertEquals(-2, result.x());
        assertEquals(-4, result.y());
        assertEquals(-6, result.z());
        assertTrue(result.isPoint());
    }

    @Test
    void negatePoint() {
        var tuple = Tuple.point(11, -2, 3);

        Exception e = assertThrows(ArithmeticException.class, tuple::negate);
        assertEquals("Can't negate a point", e.getMessage());
    }

    @Test
    void multiplyPoint() {
        var tuple = Tuple.point(1, -2, 3);

        Exception e = assertThrows(ArithmeticException.class, () -> tuple.mul(3.f));
        assertEquals("Can't multiply point with scalar", e.getMessage());
    }

    @Test
    void dividePoint() {
        var t1 = Tuple.point(1, -2, 3);

        Exception e = assertThrows(ArithmeticException.class, () -> t1.div(3.5f));
        assertEquals("Can't divide point by scalar", e.getMessage());
    }

    @Test
    void crossProductFailWithPoint() {
        var t1 = Tuple.point(1, 2, 3);
        var t2 = Tuple.point(3, 2, 1);

        Exception e = assertThrows(ArithmeticException.class, () -> t1.cross(t2));
        assertEquals("Point doesn't allow cross-product", e.getMessage());
    }

    @Test
    void crossProductFailWithVector() {
        var t1 = Tuple.point(1, 2, 3);
        var t2 = Tuple.vector(3, 2, 1);

        Exception e = assertThrows(ArithmeticException.class, () -> t1.cross(t2));
        assertEquals("Point doesn't allow cross-product", e.getMessage());
    }
}