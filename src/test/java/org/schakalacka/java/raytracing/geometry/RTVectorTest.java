package org.schakalacka.java.raytracing.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RTVectorTest {


    @Test
    void tupleIsVector() {
        var tuple = Tuple.vector(4.3, -4.2, 3.1);

        assertEquals(4.3, tuple.x());
        assertEquals(-4.2, tuple.y());
        assertEquals(3.1, tuple.z());
        assertEquals(0.0, tuple.w());
        assertTrue(tuple.isVector());
        assertFalse(tuple.isPoint());

    }

    @Test
    void addVectors() {
        var t1 = Tuple.vector(3, -2, 5);
        var t2 = Tuple.vector(-2, 3, 1);

        var sum = t1.add(t2);
        System.out.println(sum.getClass());
        assertEquals(1, sum.x());
        assertEquals(1, sum.y());
        assertEquals(6, sum.z());
        assertTrue(sum.isVector());
    }

    @Test
    void addVectorAndPoint() {
        var t1 = Tuple.vector(3, -2, 5);
        var t2 = Tuple.point(-2, 3, 1);

        var sum = t1.add(t2);
        System.out.println(sum.getClass());
        assertEquals(1, sum.x());
        assertEquals(1, sum.y());
        assertEquals(6, sum.z());
        assertTrue(sum.isPoint());
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
    void negateVector() {
        var tuple = Tuple.vector(1, -2, 3);

        var result = tuple.negate();

        assertEquals(-1, result.x());
        assertEquals(2, result.y());
        assertEquals(-3, result.z());
        assertEquals(0, result.w());
        assertTrue(result.isVector());
    }

    @Test
    void multiplyVector() {
        var tuple = Tuple.vector(1, -2, 3);

        var result = tuple.mul(3.5);

        assertEquals(3.5, result.x());
        assertEquals(-7, result.y());
        assertEquals(10.5, result.z());
        assertTrue(result.isVector());
    }

    @Test
    void divideVector() {
        var tuple = Tuple.vector(1, -2, 3);

        var result = tuple.div(2);

        assertEquals(0.5, result.x());
        assertEquals(-1, result.y());
        assertEquals(1.5, result.z());
        assertTrue(result.isVector());
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
        var reference = Tuple.vector(1 / Math.sqrt(14), 2 / Math.sqrt(14), 3 / Math.sqrt(14));

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

    }

    @Test
    void reflectAt45Degrees() {
        var vector = Tuple.vector(1,-1,0);
        var normal = Tuple.vector(0,1,0);

        var reflectedVector = vector.reflect(normal);

        assertEquals(Tuple.vector(1,1,0), reflectedVector);
    }

    @Test
    void reflectAtSlantedSurface() {
        var vector = Tuple.vector(0,-1,0);
        var normal = Tuple.vector(Math.sqrt(2)/2, Math.sqrt(2)/2, 0);

        var reflectedVector = vector.reflect(normal);

        assertEquals(Tuple.vector(1,0,0), reflectedVector);
    }

}