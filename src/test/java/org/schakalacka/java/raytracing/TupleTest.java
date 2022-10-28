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
    void subtractPointFromVector() {
        var vector = Tuple.vector(3, 2, 1);
        var point = Tuple.point(5, 6, 7);

        Exception e = assertThrows(ArithmeticException.class, () -> vector.sub(point));
        assertEquals("Can't subtract Point from Vector", e.getMessage());

    }

    @Test
    void subtractTwoVectors() {
        var vector1 = Tuple.vector(3, 2, 1);
        var vector2 = Tuple.vector(5, 6, 7);

        var result = vector1.sub(vector2);

        assertEquals(-2, result.x());
        assertEquals(-4, result.y());
        assertEquals(-6, result.z());
        assertTrue(result.isVector());
    }

    @Test
    void subtractFromZeroVector() {
        var vector1 = Tuple.vector(0, 0, 0);
        var vector2 = Tuple.vector(5, 6, 7);

        var result = vector1.sub(vector2);

        assertEquals(-5, result.x());
        assertEquals(-6, result.y());
        assertEquals(-7, result.z());
        assertTrue(result.isVector());
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

        var result = tuple.mul(3.5f);

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

    @Test
    void magnitude() {
        var vector = Tuple.vector(1, 0, 0);
        var result = vector.magnitude();

        assertEquals(1, result);

        vector = Tuple.vector(0, 1, 0);
        result = vector.magnitude();

        assertEquals(1, result);

        vector = Tuple.vector(0, 0, 1);
        result = vector.magnitude();

        assertEquals(1, result);

        vector = Tuple.vector(-1, -2, -3);
        result = vector.magnitude();

        assertEquals(Math.sqrt(14), result);

    }

    @Test
    void normalize() {
        var vector = Tuple.vector(4, 0, 0);
        var result = vector.normalize();

        assertEquals(1, result.x());
        assertEquals(0, result.y());
        assertEquals(0, result.z());
        assertTrue(result.isVector());

        vector = Tuple.vector(1, 2, 3);
        result = vector.normalize();

        // this is an approximation
        var reference = Tuple.vector((float) (1 / Math.sqrt(14)), (float) (2 / Math.sqrt(14)), (float) (3 / Math.sqrt(14)));

        assertEquals(reference, result);

    }

    @Test
    void dotProduct() {
        var vector1 = Tuple.vector(1, 2, 3);
        var vector2 = Tuple.vector(2, 3, 4);

        var result = vector1.dot(vector2);

        assertEquals(20, result);

        result = vector2.dot(vector1);

        assertEquals(20, result);
    }

    @Test
    void crossProduct() {
        var vector1 = Tuple.vector(1, 2, 3);
        var vector2 = Tuple.vector(2, 3, 4);

        var result1 = vector1.cross(vector2);

        assertEquals(Tuple.vector(-1, 2, -1), result1);

        var result2 = vector2.cross(vector1);

        assertEquals(Tuple.vector(1, -2, 1), result2);

        Exception e = assertThrows(ArithmeticException.class, () -> Tuple.vector(1,1,1).cross(Tuple.point(1,1,1)));
        assertEquals("Cross product only defined for Vectors", e.getMessage());

        e = assertThrows(ArithmeticException.class, () -> Tuple.point(1,1,1).cross(Tuple.vector(1,1,1)));
        assertEquals("Cross product only defined for Vectors", e.getMessage());

        e = assertThrows(ArithmeticException.class, () -> Tuple.point(1,1,1).cross(Tuple.point(1,1,1)));
        assertEquals("Cross product only defined for Vectors", e.getMessage());

    }
}