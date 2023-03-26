package org.schakalacka.java.raytracing.geometry.objects;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.Constants;
import org.schakalacka.java.raytracing.geometry.tracing.Ray;
import org.schakalacka.java.raytracing.math.Matrix;
import org.schakalacka.java.raytracing.math.MatrixProvider;
import org.schakalacka.java.raytracing.math.Tuple;

import static org.junit.jupiter.api.Assertions.*;
import static org.schakalacka.java.raytracing.math.Tuple.point;
import static org.schakalacka.java.raytracing.math.Tuple.vector;

class BoundingBoxTest {

    @Test
    void boundsHaveUpperAndLowerBounds() {
        var bounds = new BoundingBox(point(1, 2, 3), point(4, 5, 6));
        assertEquals(point(1, 2, 3), bounds.lower());
        assertEquals(point(4, 5, 6), bounds.upper());
    }


    @Test
    void getCorners() {
        var bounds = new BoundingBox(point(-1, -1, -1), point(1, 1, 1));

        var corners = bounds.getCorners();

        assertEquals(8, corners.size());
        assertEquals(point(-1, -1, -1), corners.get(0));
        assertEquals(point(-1, -1, 1), corners.get(1));
        assertEquals(point(1, -1, 1), corners.get(2));
        assertEquals(point(1, -1, -1), corners.get(3));
        assertEquals(point(-1, 1, -1), corners.get(4));
        assertEquals(point(-1, 1, 1), corners.get(5));
        assertEquals(point(1, 1, 1), corners.get(6));
        assertEquals(point(1, 1, -1), corners.get(7));

    }

    @Test
    void getTransformedCornersWithIdentityTransform() {
        var bounds = new BoundingBox(point(-1, -1, -1), point(1, 1, 1));
        var corners = bounds.getTransformedCorners(MatrixProvider.get(4, true));

        assertEquals(8, corners.size());
        assertEquals(point(-1, -1, -1), corners.get(0));
        assertEquals(point(-1, -1, 1), corners.get(1));
        assertEquals(point(1, -1, 1), corners.get(2));
        assertEquals(point(1, -1, -1), corners.get(3));
        assertEquals(point(-1, 1, -1), corners.get(4));
        assertEquals(point(-1, 1, 1), corners.get(5));
        assertEquals(point(1, 1, 1), corners.get(6));
        assertEquals(point(1, 1, -1), corners.get(7));

    }

    @Test
    void getTransformedCornersForScaledBounds() {
        var bounds = new BoundingBox(point(-1, -1, -1), point(1, 1, 1));
        var scaleMatrix = MatrixProvider.scaling(2, 2, 2);
        var corners = bounds.getTransformedCorners(scaleMatrix);

        assertEquals(8, corners.size());
        assertEquals(point(-2, -2, -2), corners.get(0));
        assertEquals(point(-2, -2, 2), corners.get(1));
        assertEquals(point(2, -2, 2), corners.get(2));
        assertEquals(point(2, -2, -2), corners.get(3));
        assertEquals(point(-2, 2, -2), corners.get(4));
        assertEquals(point(-2, 2, 2), corners.get(5));
        assertEquals(point(2, 2, 2), corners.get(6));
        assertEquals(point(2, 2, -2), corners.get(7));

    }

    @Test
    void getTransformedCornersForRotatedBounds() {
        var bounds = new BoundingBox(point(-1, -1, -1), point(1, 1, 1));
        var rotationMatrix = MatrixProvider.rotationY(Math.toRadians(45));
        var corners = bounds.getTransformedCorners(rotationMatrix);

        assertEquals(8, corners.size());
        assertEquals(point(-1.414213562373095, -1, 0), corners.get(0));
        assertEquals(point(0, -1, 1.414213562373095), corners.get(1));
        assertEquals(point(1.414213562373095, -1, 0), corners.get(2));
        assertEquals(point(0, -1, -1.414213562373095), corners.get(3));
        assertEquals(point(-1.414213562373095, 1, 0), corners.get(4));
        assertEquals(point(0, 1, 1.414213562373095), corners.get(5));
        assertEquals(point(1.414213562373095, 1, 0), corners.get(6));
        assertEquals(point(0, 1, -1.414213562373095), corners.get(7));
    }

    @Test
    void getTransformedCornersForTranslatedBounds() {
        var bounds = new BoundingBox(point(-1, -1, -1), point(1, 1, 1));
        var translationMatrix = MatrixProvider.translation(2, 2, 2);
        var corners = bounds.getTransformedCorners(translationMatrix);

        assertEquals(8, corners.size());
        assertEquals(point(1, 1, 1), corners.get(0));
        assertEquals(point(1, 1, 3), corners.get(1));
        assertEquals(point(3, 1, 3), corners.get(2));
        assertEquals(point(3, 1, 1), corners.get(3));
        assertEquals(point(1, 3, 1), corners.get(4));
        assertEquals(point(1, 3, 3), corners.get(5));
        assertEquals(point(3, 3, 3), corners.get(6));
        assertEquals(point(3, 3, 1), corners.get(7));
    }

    @Test
    void transformedCornersWithInfiniteOrigins() {
        // cylinder is infinite in y direction
        var bounds = new Cylinder().getBounds();
        assertEquals(point(-1, Constants.NEGATIVE_INFINITY, -1), bounds.lower());
        assertEquals(point(1, Constants.POSITIVE_INFINITY, 1), bounds.upper());
        Matrix matrix = MatrixProvider.rotationX(Math.toRadians(90));

        var transformedCorners = bounds.getTransformedCorners(matrix);
        assertEquals(8, transformedCorners.size());

        // rotated cylinder is now bound in x and y direction and infinite in z
        assertEquals(point(-1, 1, Constants.NEGATIVE_INFINITY), transformedCorners.get(0));
        assertEquals(point(-1, -1, Constants.NEGATIVE_INFINITY), transformedCorners.get(1));
        assertEquals(point(1, -1, Constants.NEGATIVE_INFINITY), transformedCorners.get(2));
        assertEquals(point(1, 1, Constants.NEGATIVE_INFINITY), transformedCorners.get(3));
        assertEquals(point(-1, 1, Constants.POSITIVE_INFINITY), transformedCorners.get(4));
        assertEquals(point(-1, -1, Constants.POSITIVE_INFINITY), transformedCorners.get(5));
        assertEquals(point(1, -1, Constants.POSITIVE_INFINITY), transformedCorners.get(6));
        assertEquals(point(1, 1, Constants.POSITIVE_INFINITY), transformedCorners.get(7));
    }

    @Test
    void transformedBoundsScaling() {
        var bounds = new BoundingBox(point(-1, -1, -1), point(1, 1, 1));
        var matrix = MatrixProvider.rotationY(Math.toRadians(45)).mulM(MatrixProvider.scaling(2, 2, 2));

        var transformedBounds = bounds.getTransformedBounds(matrix);

        assertEquals(new BoundingBox(point(-(2 * Math.sqrt(2)), -2, -(2 * Math.sqrt(2))), point(2 * Math.sqrt(2), 2, 2 * Math.sqrt(2))), transformedBounds);
    }

    @Test
    void transformedBoundsRotationWithInfinities() {
        var bounds = new BoundingBox(point(-1, Constants.NEGATIVE_INFINITY, -1), point(1, Constants.POSITIVE_INFINITY, 1));
        var matrix = MatrixProvider.rotationX(Math.toRadians(90));

        var transformedBounds = bounds.getTransformedBounds(matrix);

        assertEquals(new BoundingBox(point(-1, -1, Constants.NEGATIVE_INFINITY), point(1, 1, Constants.POSITIVE_INFINITY)), transformedBounds);
    }

    @Test
    void finiteBoundingBoxContainsPoint() {
        var box = new BoundingBox(point(5, -2, 0), point(11, 4, 7));
        assertTrue(box.contains(point(5, -2, 0)));
        assertTrue(box.contains(point(11, 4, 7)));
        assertTrue(box.contains(point(8, 1, 3)));
        assertFalse(box.contains(point(3, 0, 3)));
        assertFalse(box.contains(point(8, -4, 3)));
        assertFalse(box.contains(point(8, 1, -1)));
        assertFalse(box.contains(point(13, 1, 3)));
        assertFalse(box.contains(point(8, 5, 3)));
        assertFalse(box.contains(point(8, 1, 8)));
    }

    @Test
    void infiniteBoundBoxContainsPoint() {
        var box = new BoundingBox(point(Constants.NEGATIVE_INFINITY, Constants.NEGATIVE_INFINITY, Constants.NEGATIVE_INFINITY), point(Constants.POSITIVE_INFINITY, Constants.POSITIVE_INFINITY, Constants.POSITIVE_INFINITY));
        assertTrue(box.contains(point(5, -2, 0)));
        assertTrue(box.contains(point(11, 4, 7)));
        assertTrue(box.contains(point(8, 1, 3)));
        assertTrue(box.contains(point(3, 0, 3)));
        assertTrue(box.contains(point(8, -4, 3)));
        assertTrue(box.contains(point(8, 1, -1)));
        assertTrue(box.contains(point(13, 1, 3)));
        assertTrue(box.contains(point(8, 5, 3)));
        assertTrue(box.contains(point(8, 1, 8)));

        assertTrue(box.contains(point(Constants.NEGATIVE_INFINITY, Constants.NEGATIVE_INFINITY, Constants.NEGATIVE_INFINITY)));
        assertTrue(box.contains(point(Constants.POSITIVE_INFINITY, Constants.POSITIVE_INFINITY, Constants.POSITIVE_INFINITY)));
        assertTrue(box.contains(point(0, Constants.NEGATIVE_INFINITY, 0)));
        assertTrue(box.contains(point(0, Constants.POSITIVE_INFINITY, 0)));
    }

    @Test
    void boxWithSomeInfiniteComponentsContainsPoint() {
        var box = new BoundingBox(point(Constants.NEGATIVE_INFINITY, -2, Constants.NEGATIVE_INFINITY), point(Constants.POSITIVE_INFINITY, 4, Constants.POSITIVE_INFINITY));
        assertTrue(box.contains(point(5, -2, 0)));
        assertTrue(box.contains(point(11, 4, 7)));
        assertTrue(box.contains(point(3, 0, 3)));
        assertFalse(box.contains(point(8, -4, 3)));
        assertFalse(box.contains(point(8, 5, Constants.POSITIVE_INFINITY)));

        assertTrue(box.contains(point(Constants.NEGATIVE_INFINITY, -2, Constants.NEGATIVE_INFINITY)));
        assertTrue(box.contains(point(Constants.POSITIVE_INFINITY, 4, Constants.POSITIVE_INFINITY)));
        assertTrue(box.contains(point(0, -2, 0)));
        assertTrue(box.contains(point(0, 4, 0)));
    }

    @Test
    void boxContainsBox() {
        var box1 = new BoundingBox(point(5, -2, 0), point(11, 4, 7));
        var box2 = new BoundingBox(point(5, -2, 0), point(11, 4, 7));
        var box3 = new BoundingBox(Tuple.point(6, -1, 1), Tuple.point(10, 3, 6));
        var box4 = new BoundingBox(Tuple.point(4, -3, -1), Tuple.point(10, 3, 6));
        var box5 = new BoundingBox(Tuple.point(6, -1, 1), Tuple.point(12, 5, 8));

        assertTrue(box1.contains(box2));
        assertTrue(box1.contains(box3));
        assertFalse(box1.contains(box4));
        assertFalse(box1.contains(box5));
    }

    @Test
    void finiteBoxDoesNotContainInfiniteBox() {
        var box1 = new BoundingBox(point(5, -2, 0), point(11, 4, 7));
        var box2 = new BoundingBox(point(Constants.NEGATIVE_INFINITY, Constants.NEGATIVE_INFINITY, Constants.NEGATIVE_INFINITY), point(Constants.POSITIVE_INFINITY, Constants.POSITIVE_INFINITY, Constants.POSITIVE_INFINITY));
        var box3 = new BoundingBox(point(Constants.NEGATIVE_INFINITY, -1, Constants.NEGATIVE_INFINITY), point(5, Constants.POSITIVE_INFINITY, Constants.POSITIVE_INFINITY));

        assertFalse(box1.contains(box2));
        assertFalse(box1.contains(box3));
    }

    @Test
    void infiniteBoxContainsFiniteAndInfiniteBoxes() {
        var box1 = new BoundingBox(point(Constants.NEGATIVE_INFINITY, Constants.NEGATIVE_INFINITY, Constants.NEGATIVE_INFINITY), point(Constants.POSITIVE_INFINITY, Constants.POSITIVE_INFINITY, Constants.POSITIVE_INFINITY));
        var box2 = new BoundingBox(point(5, -2, 0), point(11, 4, 7));
        var box3 = new BoundingBox(point(Constants.NEGATIVE_INFINITY, Constants.NEGATIVE_INFINITY, Constants.NEGATIVE_INFINITY), point(Constants.POSITIVE_INFINITY, Constants.POSITIVE_INFINITY, Constants.POSITIVE_INFINITY));
        var box4 = new BoundingBox(Tuple.point(4, Constants.NEGATIVE_INFINITY, -1), Tuple.point(Constants.POSITIVE_INFINITY, 3, 6));

        assertTrue(box1.contains(box2));
        assertTrue(box1.contains(box3));
        assertTrue(box1.contains(box4));
    }

    @Test
    void boxDoesNotContainBoxWithSomeInfiniteComponents() {
        var box1 = new BoundingBox(point(5, -2, 0), point(11, 4, 7));
        var box2 = new BoundingBox(point(Constants.NEGATIVE_INFINITY, -1, Constants.NEGATIVE_INFINITY), point(5, Constants.POSITIVE_INFINITY, Constants.POSITIVE_INFINITY));
        var box3 = new BoundingBox(point(5, -2, 0), point(Constants.POSITIVE_INFINITY, 4, Constants.POSITIVE_INFINITY));

        assertFalse(box1.contains(box2));
        assertFalse(box1.contains(box3));
    }

    /***
     *| origin            | direction        | result |
     *     | point(5, 0.5, 0)  | vector(-1, 0, 0) | true   |
     *     | point(-5, 0.5, 0) | vector(1, 0, 0)  | true   |
     *     | point(0.5, 5, 0)  | vector(0, -1, 0) | true   |
     *     | point(0.5, -5, 0) | vector(0, 1, 0)  | true   |
     *     | point(0.5, 0, 5)  | vector(0, 0, -1) | true   |
     *     | point(0.5, 0, -5) | vector(0, 0, 1)  | true   |
     *     | point(0, 0.5, 0)  | vector(0, 0, 1)  | true   |
     *     | point(-2, 0, 0)   | vector(2, 4, 6)  | false  |
     *     | point(0, -2, 0)   | vector(6, 2, 4)  | false  |
     *     | point(0, 0, -2)   | vector(4, 6, 2)  | false  |
     *     | point(2, 0, 2)    | vector(0, 0, -1) | false  |
     *     | point(0, 2, 2)    | vector(0, -1, 0) | false  |
     *     | point(2, 2, 0)    | vector(-1, 0, 0) | false  |
     */
    @Test
    void intersectingRayWithBoxAtOrigin() {
        var box = new BoundingBox(point(-1, -1, -1), point(1, 1, 1));
        var ray1 = new Ray(point(5, 0.5, 0), vector(-1, 0, 0).normalize());
        var ray2 = new Ray(point(-5, 0.5, 0), vector(1, 0, 0).normalize());
        var ray3 = new Ray(point(0.5, 5, 0), vector(0, -1, 0).normalize());
        var ray4 = new Ray(point(0.5, -5, 0), vector(0, 1, 0).normalize());
        var ray5 = new Ray(point(0.5, 0, 5), vector(0, 0, -1).normalize());
        var ray6 = new Ray(point(0.5, 0, -5), vector(0, 0, 1).normalize());
        var ray7 = new Ray(point(0, 0.5, 0), vector(0, 0, 1).normalize());
        var ray8 = new Ray(point(-2, 0, 0), vector(2, 4, 6).normalize());
        var ray9 = new Ray(point(0, -2, 0), vector(6, 2, 4).normalize());
        var ray10 = new Ray(point(0, 0, -2), vector(4, 6, 2).normalize());
        var ray11 = new Ray(point(2, 0, 2), vector(0, 0, -1).normalize());
        var ray12 = new Ray(point(0, 2, 2), vector(0, -1, 0).normalize());
        var ray13 = new Ray(point(2, 2, 0), vector(-1, 0, 0).normalize());

        assertTrue(box.intersects(ray1));
        assertTrue(box.intersects(ray2));
        assertTrue(box.intersects(ray3));
        assertTrue(box.intersects(ray4));
        assertTrue(box.intersects(ray5));
        assertTrue(box.intersects(ray6));
        assertTrue(box.intersects(ray7));
        assertFalse(box.intersects(ray8));
        assertFalse(box.intersects(ray9));
        assertFalse(box.intersects(ray10));
        assertFalse(box.intersects(ray11));
        assertFalse(box.intersects(ray12));
        assertFalse(box.intersects(ray13));

    }

    /***
     * | origin           | direction        | result |
     *     | point(15, 1, 2)  | vector(-1, 0, 0) | true   |
     *     | point(-5, -1, 4) | vector(1, 0, 0)  | true   |
     *     | point(7, 6, 5)   | vector(0, -1, 0) | true   |
     *     | point(9, -5, 6)  | vector(0, 1, 0)  | true   |
     *     | point(8, 2, 12)  | vector(0, 0, -1) | true   |
     *     | point(6, 0, -5)  | vector(0, 0, 1)  | true   |
     *     | point(8, 1, 3.5) | vector(0, 0, 1)  | true   |
     *     | point(9, -1, -8) | vector(2, 4, 6)  | false  |
     *     | point(8, 3, -4)  | vector(6, 2, 4)  | false  |
     *     | point(9, -1, -2) | vector(4, 6, 2)  | false  |
     *     | point(4, 0, 9)   | vector(0, 0, -1) | false  |
     *     | point(8, 6, -1)  | vector(0, -1, 0) | false  |
     *     | point(12, 5, 4)  | vector(-1, 0, 0) | false  |
     */
    @Test
    void intersectingRayWithNonCubicBox() {
        var box = new BoundingBox(point(5,-2,0), point(11,4,7));
        var ray1 = new Ray(point(15, 1, 2), vector(-1, 0, 0).normalize());
        var ray2 = new Ray(point(-5, -1, 4), vector(1, 0, 0).normalize());
        var ray3 = new Ray(point(7, 6, 5), vector(0, -1, 0).normalize());
        var ray4 = new Ray(point(9, -5, 6), vector(0, 1, 0).normalize());
        var ray5 = new Ray(point(8, 2, 12), vector(0, 0, -1).normalize());
        var ray6 = new Ray(point(6, 0, -5), vector(0, 0, 1).normalize());
        var ray7 = new Ray(point(8, 1, 3.5), vector(0, 0, 1).normalize());
        var ray8 = new Ray(point(9, -1, -8), vector(2, 4, 6).normalize());
        var ray9 = new Ray(point(8, 3, -4), vector(6, 2, 4).normalize());
        var ray10 = new Ray(point(9, -1, -2), vector(4, 6, 2).normalize());
        var ray11 = new Ray(point(4, 0, 9), vector(0, 0, -1).normalize());
        var ray12 = new Ray(point(8, 6, -1), vector(0, -1, 0).normalize());
        var ray13 = new Ray(point(12, 5, 4), vector(-1, 0, 0).normalize());

        assertTrue(box.intersects(ray1));
        assertTrue(box.intersects(ray2));
        assertTrue(box.intersects(ray3));
        assertTrue(box.intersects(ray4));
        assertTrue(box.intersects(ray5));
        assertTrue(box.intersects(ray6));
        assertTrue(box.intersects(ray7));
        assertFalse(box.intersects(ray8));
        assertFalse(box.intersects(ray9));
        assertFalse(box.intersects(ray10));
        assertFalse(box.intersects(ray11));
        assertFalse(box.intersects(ray12));
        assertFalse(box.intersects(ray13));



    }
}