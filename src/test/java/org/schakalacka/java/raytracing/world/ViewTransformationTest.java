package org.schakalacka.java.raytracing.world;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.geometry.MatrixProvider;
import org.schakalacka.java.raytracing.geometry.Tuple;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ViewTransformationTest {

    @Test
    void matrixForDefaultOrientation() {
        var from = Tuple.point(0, 0, 0);
        var to = Tuple.point(0, 0, -1);
        var up = Tuple.vector(0, 1, 0);

        var transformMatrix = ViewTransformation.transform(from, to, up);

        var refMatrix = MatrixProvider.get(new double[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });

        assertEquals(refMatrix, transformMatrix);
    }

    /*
    we basically turn around to look along the z axis. Which would reflect/mirror the x and z axis
    reflect/mirror -> scale by negative value
    */
    @Test
    void matrixLookingInPositiveZ() {
        var from = Tuple.point(0, 0, 0);
        var to = Tuple.point(0, 0, 1);
        var up = Tuple.vector(0, 1, 0);

        var transformMatrix = ViewTransformation.transform(from, to, up);

        var refMatrix = MatrixProvider.scaling(-1, 1, -1);

        assertEquals(refMatrix, transformMatrix);
    }

    @Test
    void transformationMovesWorld() {
        var from = Tuple.point(0, 0, 8);
        var to = Tuple.point(0, 0, 0);
        var up = Tuple.vector(0, 1, 0);

        var transformMatrix = ViewTransformation.transform(from, to, up);

        var refMatrix = MatrixProvider.translation(0, 0, -8);

        assertEquals(refMatrix, transformMatrix);
    }

    @Test
    void arbitraryViewTransformation() {
        var from = Tuple.point(1, 3, 2);
        var to = Tuple.point(4, -2, 8);
        var up = Tuple.vector(1, 1, 0);

        var transformMatrix = ViewTransformation.transform(from, to, up);

        var refMatrix = MatrixProvider.get(new double[][]{
                {-0.50709, 0.50709, 0.67612, -2.36643},
                {0.76772, 0.60609, 0.12122, -2.82843},
                {-0.35857, 0.59761, -0.71714, 0.00000},
                {0, 0, 0, 1},
        });

        assertEquals(refMatrix, transformMatrix);

    }

}