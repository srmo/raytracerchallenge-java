package org.schakalacka.java.raytracing.geometry.objects;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.Constants;
import org.schakalacka.java.raytracing.math.Matrix;
import org.schakalacka.java.raytracing.math.MatrixProvider;
import org.schakalacka.java.raytracing.math.Tuple;

import static org.junit.jupiter.api.Assertions.*;
import static org.schakalacka.java.raytracing.math.Tuple.point;

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

        assertEquals(new BoundingBox(point(-(2*Math.sqrt(2)), -2, -(2*Math.sqrt(2))), point(2*Math.sqrt(2), 2, 2*Math.sqrt(2))), transformedBounds);
    }

    @Test
    void transformedBoundsRotationWithInfinities() {
        var bounds = new BoundingBox(point(-1, Constants.NEGATIVE_INFINITY, -1), point(1, Constants.POSITIVE_INFINITY, 1));
        var matrix = MatrixProvider.rotationX(Math.toRadians(90));

        var transformedBounds = bounds.getTransformedBounds(matrix);

        assertEquals(new BoundingBox(point(-1,-1,Constants.NEGATIVE_INFINITY), point(1,1,Constants.POSITIVE_INFINITY)), transformedBounds);
    }

    @Test
    void finiteBoundingBoxContainsPoint() {
        var box = new BoundingBox(point(5,-2,0), point(11,4,7));
        assertTrue(box.contains(point(5,-2,0)));
        assertTrue(box.contains(point(11,4,7)));
        assertTrue(box.contains(point(8,1,3)));
        assertFalse(box.contains(point(3,0,3)));
        assertFalse(box.contains(point(8,-4,3)));
        assertFalse(box.contains(point(8,1,-1)));
        assertFalse(box.contains(point(13,1,3)));
        assertFalse(box.contains(point(8,5,3)));
        assertFalse(box.contains(point(8,1,8)));
    }

    @Test
    void infiniteBoundBoxContainsPoint() {
        var box = new BoundingBox(point(Constants.NEGATIVE_INFINITY, Constants.NEGATIVE_INFINITY, Constants.NEGATIVE_INFINITY), point(Constants.POSITIVE_INFINITY, Constants.POSITIVE_INFINITY, Constants.POSITIVE_INFINITY));
        assertTrue(box.contains(point(5,-2,0)));
        assertTrue(box.contains(point(11,4,7)));
        assertTrue(box.contains(point(8,1,3)));
        assertTrue(box.contains(point(3,0,3)));
        assertTrue(box.contains(point(8,-4,3)));
        assertTrue(box.contains(point(8,1,-1)));
        assertTrue(box.contains(point(13,1,3)));
        assertTrue(box.contains(point(8,5,3)));
        assertTrue(box.contains(point(8,1,8)));

        assertTrue(box.contains(point(Constants.NEGATIVE_INFINITY, Constants.NEGATIVE_INFINITY, Constants.NEGATIVE_INFINITY)));
        assertTrue(box.contains(point(Constants.POSITIVE_INFINITY, Constants.POSITIVE_INFINITY, Constants.POSITIVE_INFINITY)));
        assertTrue(box.contains(point(0, Constants.NEGATIVE_INFINITY, 0)));
        assertTrue(box.contains(point(0, Constants.POSITIVE_INFINITY, 0)));
    }

    @Test
    void boxWithSomeInfiniteComponentsContainsPoint() {
        var box = new BoundingBox(point(Constants.NEGATIVE_INFINITY, -2, Constants.NEGATIVE_INFINITY), point(Constants.POSITIVE_INFINITY, 4, Constants.POSITIVE_INFINITY));
        assertTrue(box.contains(point(5,-2,0)));
        assertTrue(box.contains(point(11,4,7)));
        assertTrue(box.contains(point(3,0,3)));
        assertFalse(box.contains(point(8,-4,3)));
        assertFalse(box.contains(point(8,5,Constants.POSITIVE_INFINITY)));

        assertTrue(box.contains(point(Constants.NEGATIVE_INFINITY, -2, Constants.NEGATIVE_INFINITY)));
        assertTrue(box.contains(point(Constants.POSITIVE_INFINITY, 4, Constants.POSITIVE_INFINITY)));
        assertTrue(box.contains(point(0, -2, 0)));
        assertTrue(box.contains(point(0, 4, 0)));
    }

    @Test
    void boxContainsBox() {
        var box1 = new BoundingBox(point(5,-2,0), point(11,4,7));
        var box2 = new BoundingBox(point(5,-2,0), point(11,4,7));
        var box3 = new BoundingBox(Tuple.point(6,-1,1), Tuple.point(10,3,6));
        var box4 = new BoundingBox(Tuple.point(4,-3,-1), Tuple.point(10,3,6));
        var box5 = new BoundingBox(Tuple.point(6,-1,1), Tuple.point(12,5,8));

        assertTrue(box1.contains(box2));
        assertTrue(box1.contains(box3));
        assertFalse(box1.contains(box4));
        assertFalse(box1.contains(box5));
    }

    @Test
    void finiteBoxDoesNotContainInfiniteBox() {
        var box1 = new BoundingBox(point(5,-2,0), point(11,4,7));
        var box2 = new BoundingBox(point(Constants.NEGATIVE_INFINITY, Constants.NEGATIVE_INFINITY, Constants.NEGATIVE_INFINITY), point(Constants.POSITIVE_INFINITY, Constants.POSITIVE_INFINITY, Constants.POSITIVE_INFINITY));
        var box3 = new BoundingBox(point(Constants.NEGATIVE_INFINITY, -1, Constants.NEGATIVE_INFINITY), point(5, Constants.POSITIVE_INFINITY, Constants.POSITIVE_INFINITY));

        assertFalse(box1.contains(box2));
        assertFalse(box1.contains(box3));
    }

    @Test
    void infiniteBoxContainsFiniteAndInfiniteBoxes() {
        var box1 = new BoundingBox(point(Constants.NEGATIVE_INFINITY, Constants.NEGATIVE_INFINITY, Constants.NEGATIVE_INFINITY), point(Constants.POSITIVE_INFINITY, Constants.POSITIVE_INFINITY, Constants.POSITIVE_INFINITY));
        var box2 = new BoundingBox(point(5,-2,0), point(11,4,7));
        var box3 = new BoundingBox(point(Constants.NEGATIVE_INFINITY, Constants.NEGATIVE_INFINITY, Constants.NEGATIVE_INFINITY), point(Constants.POSITIVE_INFINITY, Constants.POSITIVE_INFINITY, Constants.POSITIVE_INFINITY));
        var box4 = new BoundingBox(Tuple.point(4,Constants.NEGATIVE_INFINITY,-1), Tuple.point(Constants.POSITIVE_INFINITY,3,6));

        assertTrue(box1.contains(box2));
        assertTrue(box1.contains(box3));
        assertTrue(box1.contains(box4));
    }
}