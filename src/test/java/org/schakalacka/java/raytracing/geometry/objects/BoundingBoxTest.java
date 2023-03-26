package org.schakalacka.java.raytracing.geometry.objects;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.Constants;
import org.schakalacka.java.raytracing.math.Matrix;
import org.schakalacka.java.raytracing.math.MatrixProvider;
import org.schakalacka.java.raytracing.math.Tuple;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoundingBoxTest {

    @Test
    void boundsHaveUpperAndLowerBounds() {
        var bounds = new BoundingBox(Tuple.point(1, 2, 3), Tuple.point(4, 5, 6));
        assertEquals(Tuple.point(1, 2, 3), bounds.lower());
        assertEquals(Tuple.point(4, 5, 6), bounds.upper());
    }


    @Test
    void getCorners() {
        var bounds = new BoundingBox(Tuple.point(-1, -1, -1), Tuple.point(1, 1, 1));

        var corners = bounds.getCorners();

        assertEquals(8, corners.size());
        assertEquals(Tuple.point(-1, -1, -1), corners.get(0));
        assertEquals(Tuple.point(-1, -1, 1), corners.get(1));
        assertEquals(Tuple.point(1, -1, 1), corners.get(2));
        assertEquals(Tuple.point(1, -1, -1), corners.get(3));
        assertEquals(Tuple.point(-1, 1, -1), corners.get(4));
        assertEquals(Tuple.point(-1, 1, 1), corners.get(5));
        assertEquals(Tuple.point(1, 1, 1), corners.get(6));
        assertEquals(Tuple.point(1, 1, -1), corners.get(7));

    }

    @Test
    void getTransformedCornersWithIdentityTransform() {
        var bounds = new BoundingBox(Tuple.point(-1, -1, -1), Tuple.point(1, 1, 1));
        var corners = bounds.getTransformedCorners(MatrixProvider.get(4, true));

        assertEquals(8, corners.size());
        assertEquals(Tuple.point(-1, -1, -1), corners.get(0));
        assertEquals(Tuple.point(-1, -1, 1), corners.get(1));
        assertEquals(Tuple.point(1, -1, 1), corners.get(2));
        assertEquals(Tuple.point(1, -1, -1), corners.get(3));
        assertEquals(Tuple.point(-1, 1, -1), corners.get(4));
        assertEquals(Tuple.point(-1, 1, 1), corners.get(5));
        assertEquals(Tuple.point(1, 1, 1), corners.get(6));
        assertEquals(Tuple.point(1, 1, -1), corners.get(7));

    }

    @Test
    void getTransformedCornersForScaledBounds() {
        var bounds = new BoundingBox(Tuple.point(-1, -1, -1), Tuple.point(1, 1, 1));
        var scaleMatrix = MatrixProvider.scaling(2, 2, 2);
        var corners = bounds.getTransformedCorners(scaleMatrix);

        assertEquals(8, corners.size());
        assertEquals(Tuple.point(-2, -2, -2), corners.get(0));
        assertEquals(Tuple.point(-2, -2, 2), corners.get(1));
        assertEquals(Tuple.point(2, -2, 2), corners.get(2));
        assertEquals(Tuple.point(2, -2, -2), corners.get(3));
        assertEquals(Tuple.point(-2, 2, -2), corners.get(4));
        assertEquals(Tuple.point(-2, 2, 2), corners.get(5));
        assertEquals(Tuple.point(2, 2, 2), corners.get(6));
        assertEquals(Tuple.point(2, 2, -2), corners.get(7));

    }

    @Test
    void getTransformedCornersForRotatedBounds() {
        var bounds = new BoundingBox(Tuple.point(-1, -1, -1), Tuple.point(1, 1, 1));
        var rotationMatrix = MatrixProvider.rotationY(Math.toRadians(45));
        var corners = bounds.getTransformedCorners(rotationMatrix);

        assertEquals(8, corners.size());
        assertEquals(Tuple.point(-1.414213562373095, -1, 0), corners.get(0));
        assertEquals(Tuple.point(0, -1, 1.414213562373095), corners.get(1));
        assertEquals(Tuple.point(1.414213562373095, -1, 0), corners.get(2));
        assertEquals(Tuple.point(0, -1, -1.414213562373095), corners.get(3));
        assertEquals(Tuple.point(-1.414213562373095, 1, 0), corners.get(4));
        assertEquals(Tuple.point(0, 1, 1.414213562373095), corners.get(5));
        assertEquals(Tuple.point(1.414213562373095, 1, 0), corners.get(6));
        assertEquals(Tuple.point(0, 1, -1.414213562373095), corners.get(7));
    }

    @Test
    void getTransformedCornersForTranslatedBounds() {
        var bounds = new BoundingBox(Tuple.point(-1, -1, -1), Tuple.point(1, 1, 1));
        var translationMatrix = MatrixProvider.translation(2, 2, 2);
        var corners = bounds.getTransformedCorners(translationMatrix);

        assertEquals(8, corners.size());
        assertEquals(Tuple.point(1, 1, 1), corners.get(0));
        assertEquals(Tuple.point(1, 1, 3), corners.get(1));
        assertEquals(Tuple.point(3, 1, 3), corners.get(2));
        assertEquals(Tuple.point(3, 1, 1), corners.get(3));
        assertEquals(Tuple.point(1, 3, 1), corners.get(4));
        assertEquals(Tuple.point(1, 3, 3), corners.get(5));
        assertEquals(Tuple.point(3, 3, 3), corners.get(6));
        assertEquals(Tuple.point(3, 3, 1), corners.get(7));
    }

    @Test
    void transformedCornersWithInfiniteOrigins() {
        // cylinder is infinite in y direction
        var bounds = new Cylinder().getBounds();
        assertEquals(Tuple.point(-1, Constants.NEGATIVE_INFINITY, -1), bounds.lower());
        assertEquals(Tuple.point(1, Constants.POSITIVE_INFINITY, 1), bounds.upper());
        Matrix matrix = MatrixProvider.rotationX(Math.toRadians(90));

        var transformedCorners = bounds.getTransformedCorners(matrix);
        assertEquals(8, transformedCorners.size());

        // rotated cylinder is now bound in x and y direction and infinite in z
        assertEquals(Tuple.point(-1, 1, Constants.NEGATIVE_INFINITY), transformedCorners.get(0));
        assertEquals(Tuple.point(-1, -1, Constants.NEGATIVE_INFINITY), transformedCorners.get(1));
        assertEquals(Tuple.point(1, -1, Constants.NEGATIVE_INFINITY), transformedCorners.get(2));
        assertEquals(Tuple.point(1, 1, Constants.NEGATIVE_INFINITY), transformedCorners.get(3));
        assertEquals(Tuple.point(-1, 1, Constants.POSITIVE_INFINITY), transformedCorners.get(4));
        assertEquals(Tuple.point(-1, -1, Constants.POSITIVE_INFINITY), transformedCorners.get(5));
        assertEquals(Tuple.point(1, -1, Constants.POSITIVE_INFINITY), transformedCorners.get(6));
        assertEquals(Tuple.point(1, 1, Constants.POSITIVE_INFINITY), transformedCorners.get(7));
    }

    @Test
    void transformedBoundsScaling() {
        var bounds = new BoundingBox(Tuple.point(-1, -1, -1), Tuple.point(1, 1, 1));
        var matrix = MatrixProvider.rotationY(Math.toRadians(45)).mulM(MatrixProvider.scaling(2, 2, 2));

        var transformedBounds = bounds.getTransformedBounds(matrix);

        assertEquals(new BoundingBox(Tuple.point(-(2*Math.sqrt(2)), -2, -(2*Math.sqrt(2))), Tuple.point(2*Math.sqrt(2), 2, 2*Math.sqrt(2))), transformedBounds);
    }

    @Test
    void transformedBoundsRotationWithInfinities() {
        var bounds = new BoundingBox(Tuple.point(-1, Constants.NEGATIVE_INFINITY, -1), Tuple.point(1, Constants.POSITIVE_INFINITY, 1));
        var matrix = MatrixProvider.rotationX(Math.toRadians(90));

        var transformedBounds = bounds.getTransformedBounds(matrix);

        assertEquals(new BoundingBox(Tuple.point(-1,-1,Constants.NEGATIVE_INFINITY), Tuple.point(1,1,Constants.POSITIVE_INFINITY)), transformedBounds);
    }

}