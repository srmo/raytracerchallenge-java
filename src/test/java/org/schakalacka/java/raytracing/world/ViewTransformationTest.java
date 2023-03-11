package org.schakalacka.java.raytracing.world;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.math.MatrixProvider;
import org.schakalacka.java.raytracing.math.Tuple;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ViewTransformationTest {

    @Test
    void matrixForDefaultOrientation() {
        var from = Tuple.point(0, 0, 0);
        var to = Tuple.point(0, 0, -1);
        var up = Tuple.vector(0, 1, 0);

        var transformMatrix = ViewTransformation.transform(from, to, up);

        var refMatrix = MatrixProvider.get(new float[][]{
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

        var refMatrix = MatrixProvider.get(new float[][]{
                {(float) -0.50709, 0.50709f, 0.67612f, (float) -2.36643},
                {0.76772f, 0.60609f, 0.12122f, (float) -2.82843},
                {(float) -0.35857, 0.59761f, (float) -0.71714, 0.00000F},
                {0, 0, 0, 1},
        });

        assertEquals(refMatrix, transformMatrix);

    }

}