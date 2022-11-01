package org.schakalacka.java.raytracing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
                {3, 5, 0},
                {2, -1, -7},
                {6, -1, 5},
        });

        var determinant = matrix.subM(1, 0).determinant();
        var minor = matrix.minor(1, 0);

        assertEquals(25, determinant);
        assertEquals(25, minor);
    }

    @Test
    void cofactor3() {
        var matrix = new Matrix(new double[][]{
                {3, 5, 0},
                {2, -1, -7},
                {6, -1, 5},
        });

        var minor00 = matrix.minor(0, 0);
        var cofactor00 = matrix.cofactor(0, 0);
        var minor10 = matrix.minor(1, 0);
        var cofactor10 = matrix.cofactor(1, 0);

        assertEquals(-12, minor00);
        assertEquals(-12, cofactor00);
        assertEquals(25, minor10);
        assertEquals(-25, cofactor10);
    }

    @Test
    void determinant3() {
        var matrix = new Matrix(new double[][]{
                {1, 2, 6},
                {-5, 8, -4},
                {2, 6, 4},
        });

        var cofactor00 = matrix.cofactor(0, 0);
        var cofactor01 = matrix.cofactor(0, 1);
        var cofactor02 = matrix.cofactor(0, 2);
        var determinant = matrix.determinant();

        assertEquals(56, cofactor00);
        assertEquals(12, cofactor01);
        assertEquals(-46, cofactor02);
        assertEquals(-196, determinant);

    }

    @Test
    void determinant4() {
        var matrix = new Matrix(new double[][]{
                {-2, -8, 3, 5},
                {-3, 1, 7, 3},
                {1, 2, -9, 6},
                {-6, 7, 7, -9},
        });

        var cofactor00 = matrix.cofactor(0, 0);
        var cofactor01 = matrix.cofactor(0, 1);
        var cofactor02 = matrix.cofactor(0, 2);
        var cofactor03 = matrix.cofactor(0, 3);
        var determinant = matrix.determinant();

        assertEquals(690, cofactor00);
        assertEquals(447, cofactor01);
        assertEquals(210, cofactor02);
        assertEquals(51, cofactor03);
        assertEquals(-4071, determinant);
    }

    @Test
    void isInvertible() {
        var matrix = new Matrix(new double[][]{
                {6, 4, 4, 4},
                {5, 5, 7, 6},
                {4, -9, 3, -7},
                {9, 1, 7, -6},
        });

        var determinant = matrix.determinant();

        assertEquals(-2120, determinant);
        assertTrue(matrix.isInvertible());
    }

    @Test
    void isNotInvertible() {
        var matrix = new Matrix(new double[][]{
                {-4, 2, -2, -3},
                {9, 6, 2, 6},
                {0, -5, 1, -5},
                {0, 0, 0, 0},
        });

        var determinant = matrix.determinant();

        assertEquals(0, determinant);
        assertFalse(matrix.isInvertible());
    }

    @Test
    void invert4() {
        var matrix = new Matrix(new double[][]{
                {-5, 2, 6, -8},
                {1, -5, 1, 8},
                {7, 7, -6, -7},
                {1, -3, 7, 4},
        });

        var inverse = matrix.inverse();
        var determinant = matrix.determinant();
        var cofactor23 = matrix.cofactor(2, 3);
        var inverse32 = inverse.get(3, 2);
        var cofactor32 = matrix.cofactor(3, 2);
        var inverse23 = inverse.get(2, 3);


        // this is the full precision matrix. Keep it with the EPSILON for a while...
        // new Matrix(new double[][]{
//                {0.21804511278195488, 0.45112781954887216, 0.24060150375939848, -0.045112781954887216},
//                {-0.8082706766917294, -1.4567669172932332, -0.44360902255639095, 0.5206766917293233},
//                {-0.07894736842105263, -0.2236842105263158, -0.05263157894736842, 0.19736842105263158},
//                {-0.5225563909774437, -0.8139097744360902, -0.3007518796992481, 0.30639097744360905}
//        });

        var expectedInverse = new Matrix(new double[][]{
                {0.21805, 0.45113, 0.24060, -0.04511},
                {-0.80827, -1.45677, -0.44361, 0.52068},
                {-0.07895, -0.22368, -0.05263, 0.19737},
                {-0.52256, -0.81391, -0.30075, 0.30639},

        });

        assertEquals(532, determinant);
        assertEquals(-160, cofactor23);
        assertEquals((double) -160 / 532, inverse32);
        assertEquals(105, cofactor32);
        assertEquals((double) 105 / 532, inverse23);
        assertEquals(expectedInverse, inverse);
    }

    @Test
    void invertAnother() {
        var matrix1 = new Matrix(new double[][]{
                {8, -5, 9, 2},
                {7, 5, 6, 1},
                {-6, 0, 9, 6},
                {-3, 0, -9, -4},
        });
        var matrix1ExpectedInvert = new Matrix(new double[][]{
                {-0.15385, -0.15385, -0.28205, -0.53846},
                {-0.07692, 0.12308, 0.02564, 0.03077},
                {0.35897, 0.35897, 0.43590, 0.92308},
                {-0.69231, -0.69231, -0.76923, -1.92308},
        });
        var matrix2 = new Matrix(new double[][]{
                {9, 3, 0, 9},
                {-5, -2, -6, -3},
                {-4, 9, 6, 4},
                {-7, 6, 6, 2},
        });
        var matrix2ExpectedInvert = new Matrix(new double[][]{
                {-0.04074, -0.07778, 0.14444, -0.22222},
                {-0.07778, 0.03333, 0.36667, -0.33333},
                {-0.02901, -0.14630, -0.10926, 0.12963},
                {0.17778, 0.06667, -0.26667, 0.33333},
        });

        var inverse1 = matrix1.inverse();
        var inverse2 = matrix2.inverse();

        assertEquals(matrix1ExpectedInvert, inverse1);
        assertEquals(matrix2ExpectedInvert, inverse2);
    }

    @Test
    void multiplyInverse() {
        var matrix1 = new Matrix(new double[][]{
                {3,-9,7,3},
                {3,-8,2,-9},
                {-4,4,4,1},
                {-6,5,-1,1},
        });
        var matrix2 = new Matrix(new double[][]{
                {8,2,2,2},
                {3,-1,7,0},
                {7,0,5,4},
                {6,-2,0,5},
        });

        var multiplied = matrix1.mulM(matrix2);

        assertEquals(matrix1, multiplied.mulM(matrix2.inverse()));
    }

}