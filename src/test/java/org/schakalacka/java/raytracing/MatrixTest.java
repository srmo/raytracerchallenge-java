package org.schakalacka.java.raytracing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * <p>&copy; 2022 <a href="http://www.ponton.de" target="_blank">PONTON GmbH</a></p>
 * <p>
 * [description of the class]
 *
 * @author <a href="mueller@ponton.de">Stephan Müller</a>
 * @since 2022-10-30
 * [potential @see links]
 */
class MatrixTest {

    @Test
    void identity2() {
        Matrix m = new Matrix(2, true);

        assertEquals(1, m.get(0, 0));
        assertEquals(0, m.get(0, 1));
        assertEquals(0, m.get(1, 0));
        assertEquals(1, m.get(1, 1));
    }

    @Test
    void identity3() {
        Matrix m = new Matrix(3, true);

        assertEquals(1, m.get(0, 0));
        assertEquals(0, m.get(0, 1));
        assertEquals(0, m.get(0, 2));
        assertEquals(0, m.get(1, 0));
        assertEquals(1, m.get(1, 1));
        assertEquals(0, m.get(1, 2));
        assertEquals(0, m.get(2, 0));
        assertEquals(0, m.get(2, 1));
        assertEquals(1, m.get(2, 2));

    }

    @Test
    void identity4() {
        Matrix m = new Matrix(4, true);

        assertEquals(1, m.get(0, 0));
        assertEquals(0, m.get(0, 1));
        assertEquals(0, m.get(0, 2));
        assertEquals(0, m.get(0, 3));
        assertEquals(0, m.get(1, 0));
        assertEquals(1, m.get(1, 1));
        assertEquals(0, m.get(1, 2));
        assertEquals(0, m.get(1, 3));
        assertEquals(0, m.get(2, 0));
        assertEquals(0, m.get(2, 1));
        assertEquals(1, m.get(2, 2));
        assertEquals(0, m.get(2, 3));
        assertEquals(0, m.get(3, 0));
        assertEquals(0, m.get(3, 1));
        assertEquals(0, m.get(3, 2));
        assertEquals(1, m.get(3, 3));
    }

    @Test
    void createMatrix4() {
        var ref = new double[][]{
                {1, 2, 3, 4},
                {5.5, 6.5, 7.5, 8.5},
                {9, 10, 11, 12},
                {13.5, 14.5, 15.5, 16.5}
        };

        var matrix = new Matrix(ref);

        assertEquals(1, matrix.get(0, 0));
        assertEquals(2, matrix.get(0, 1));
        assertEquals(3, matrix.get(0, 2));
        assertEquals(4, matrix.get(0, 3));
        assertEquals(5.5, matrix.get(1, 0));
        assertEquals(6.5, matrix.get(1, 1));
        assertEquals(7.5, matrix.get(1, 2));
        assertEquals(8.5, matrix.get(1, 3));
        assertEquals(9, matrix.get(2, 0));
        assertEquals(10, matrix.get(2, 1));
        assertEquals(11, matrix.get(2, 2));
        assertEquals(12, matrix.get(2, 3));
        assertEquals(13.5, matrix.get(3, 0));
        assertEquals(14.5, matrix.get(3, 1));
        assertEquals(15.5, matrix.get(3, 2));
        assertEquals(16.5, matrix.get(3, 3));


        // double check that the matrix doesn't ref the initial object
        ref[0][2] = 5;
        assertEquals(3, matrix.get(0, 2));

    }

    @Test
    void createMatrix2() {
        var ref = new double[][]{
                {-3, 5},
                {1, -2},
        };

        var matrix = new Matrix(ref);

        assertEquals(-3, matrix.get(0, 0));
        assertEquals(5, matrix.get(0, 1));
        assertEquals(1, matrix.get(1, 0));
        assertEquals(-2, matrix.get(1, 1));


        // double check that the matrix doesn't ref the initial object
        ref[0][1] = -999;
        assertEquals(5, matrix.get(0, 1));

    }

    @Test
    void createMatrix3() {
        var ref = new double[][]{
                {-3, 5, 0},
                {1, -2, -7},
                {0, 1, 1}
        };

        var matrix = new Matrix(ref);

        assertEquals(-3, matrix.get(0, 0));
        assertEquals(5, matrix.get(0, 1));
        assertEquals(0, matrix.get(0, 2));
        assertEquals(1, matrix.get(1, 0));
        assertEquals(-2, matrix.get(1, 1));
        assertEquals(-7, matrix.get(1, 2));
        assertEquals(0, matrix.get(2, 0));
        assertEquals(1, matrix.get(2, 1));
        assertEquals(1, matrix.get(2, 2));


        // double check that the matrix doesn't ref the initial object
        ref[0][1] = -999;
        assertEquals(5, matrix.get(0, 1));
    }

    @Test
    void matrixCompareEqual() {
        var ref = new double[][]{
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 8, 7, 6},
                {5, 4, 3, 2},
        };

        var ref2 = new double[][]{
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 8, 7, 6},
                {5, 4, 3, 2},
        };

        var matrix1 = new Matrix(ref);
        var matrix2 = new Matrix(ref2);

        assertEquals(matrix1, matrix2);
    }

    @Test
    void matrixCompareNotEqual() {
        var ref = new double[][]{
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 8, 7, 6},
                {5, 4, 3, 2},
        };

        var ref2 = new double[][]{
                {2, 3, 4, 5},
                {6, 7, 8, 9},
                {8, 7, 6, 5},
                {4, 3, 2, 1},
        };

        var matrix1 = new Matrix(ref);
        var matrix2 = new Matrix(ref2);

        assertNotEquals(matrix1, matrix2);
    }


    @Test
    void mul4() {
        var expectedMatrix = new Matrix(new double[][]{
                {20, 22, 50, 48},
                {44, 54, 114, 108},
                {40, 58, 110, 102},
                {16, 26, 46, 42},
        });
        var matrix1 = new Matrix(new double[][]{
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 8, 7, 6},
                {5, 4, 3, 2},
        });

        var matrix2 = new Matrix(new double[][]{
                {-2, 1, 2, 3},
                {3, 2, 1, -1},
                {4, 3, 6, 5},
                {1, 2, 7, 8},
        });

        var result = matrix1.mulM(matrix2);

        assertEquals(expectedMatrix, result);

    }

    @Test
    void mul3() {
        var expectedMatrix = new Matrix(new double[][]{
                {16, 14, 22},
                {36, 38, 58},
                {34, 46, 68},
        });
        var matrix1 = new Matrix(new double[][]{
                {1, 2, 3},
                {5, 6, 7},
                {9, 8, 7},
        });

        var matrix2 = new Matrix(new double[][]{
                {-2, 1, 2},
                {3, 2, 1},
                {4, 3, 6},
        });

        var result = matrix1.mulM(matrix2);

        assertEquals(expectedMatrix, result);
    }

    @Test
    void mul2() {
        var expectedMatrix = new Matrix(new double[][]{
                {4, 5},
                {8, 17},
        });
        var matrix1 = new Matrix(new double[][]{
                {1, 2},
                {5, 6},
        });

        var matrix2 = new Matrix(new double[][]{
                {-2, 1},
                {3, 2},
        });

        var result = matrix1.mulM(matrix2);

        assertEquals(expectedMatrix, result);
    }

    @Test
    void mulTuple() {
        var expectedTuple = Tuple.tuple(18, 24, 33, 1);
        var matrix1 = new Matrix(new double[][]{
                {1, 2, 3, 4},
                {2, 4, 4, 2},
                {8, 6, 4, 1},
                {0, 0, 0, 1},
        });

        var tuple = Tuple.tuple(1, 2, 3, 1);

        var result = matrix1.mulT(tuple);

        assertEquals(expectedTuple, result);
    }

    @Test
    void mulIdentity() {
        var identity = new Matrix(4, true);
        var matrix1 = new Matrix(new double[][]{
                {1, 2, 3, 4},
                {2, 4, 4, 2},
                {8, 6, 4, 1},
                {0, 0, 0, 1},
        });

        var result = matrix1.mulM(identity);

        assertEquals(matrix1, result);
    }

    @Test
    void transpose() {
        var expected = new Matrix(new double[][]{
                {0, 9, 1, 0},
                {9, 8, 8, 0},
                {3, 0, 5, 5},
                {0, 8, 3, 8},
        });
        var matrix1 = new Matrix(new double[][]{
                {0, 9, 3, 0},
                {9, 8, 0, 8},
                {1, 8, 5, 3},
                {0, 0, 5, 8},
        });

        var result = matrix1.transpose();

        assertEquals(expected, result);
    }

    @Test
    void transposeIdentity() {
        var expected = new Matrix(new double[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1},
        });
        var matrix1 = new Matrix(new double[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1},
        });

        var result = matrix1.transpose();

        assertEquals(expected, result);
    }

    @Test
    void determinant2() {
        var matrix = new Matrix(new double[][]{
                {1, 5},
                {-3, 2}
        });

        var determinant = matrix.determinant();

        assertEquals(17, determinant);
    }

    @Test
    void submatrix3() {
        var expected = new Matrix(new double[][]{
                {-3, 2},
                {0, 6},
        });
        var matrix = new Matrix(new double[][]{
                {1, 5, 0},
                {-3, 2, 7},
                {0, 6, -3},
        });

        var result = matrix.subM(0, 2);

        assertEquals(expected, result);
    }

    @Test
    void submatrix4() {
        var expected = new Matrix(new double[][]{
                {-6, 1, 6},
                {-8, 8, 6},
                {-7, -1, 1},
        });
        var matrix = new Matrix(new double[][]{
                {-6, 1, 1, 6},
                {-8, 5, 8, 6},
                {-1, 0, 8, 2},
                {-7, 1, -1, 1},
        });

        var result = matrix.subM(2, 1);

        assertEquals(expected, result);
    }

    @Test
    void minor3() {
        var matrix = new Matrix(new double[][]{
                {3,5,0},
                {2,-1,-7},
                {6,-1,5},
        });

        var result = matrix.minor(1,0);

        assertEquals(25, result);
    }

}