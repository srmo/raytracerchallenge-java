package org.schakalacka.java.raytracing.geometry.algebra;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NaiveMatrixProviderTest {

    @Test
    void identity2() {
        Matrix m = NaiveMatrixProvider.get(2, true);

        assertEquals(1, m.get(0, 0));
        assertEquals(0, m.get(0, 1));
        assertEquals(0, m.get(1, 0));
        assertEquals(1, m.get(1, 1));
    }

    @Test
    void identity3() {
        Matrix m = NaiveMatrixProvider.get(3, true);

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
        Matrix m = NaiveMatrixProvider.get(4, true);

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

        var matrix = NaiveMatrixProvider.get(ref);

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

        var matrix = NaiveMatrixProvider.get(ref);

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

        var matrix = NaiveMatrixProvider.get(ref);

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

        var matrix1 = NaiveMatrixProvider.get(ref);
        var matrix2 = NaiveMatrixProvider.get(ref2);

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

        var matrix1 = NaiveMatrixProvider.get(ref);
        var matrix2 = NaiveMatrixProvider.get(ref2);

        assertNotEquals(matrix1, matrix2);
    }


    @Test
    void mul4() {
        var expectedMatrix = NaiveMatrixProvider.get(new double[][]{
                {20, 22, 50, 48},
                {44, 54, 114, 108},
                {40, 58, 110, 102},
                {16, 26, 46, 42},
        });
        var matrix1 = NaiveMatrixProvider.get(new double[][]{
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 8, 7, 6},
                {5, 4, 3, 2},
        });

        var matrix2 = NaiveMatrixProvider.get(new double[][]{
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
        var expectedMatrix = NaiveMatrixProvider.get(new double[][]{
                {16, 14, 22},
                {36, 38, 58},
                {34, 46, 68},
        });
        var matrix1 = NaiveMatrixProvider.get(new double[][]{
                {1, 2, 3},
                {5, 6, 7},
                {9, 8, 7},
        });

        var matrix2 = NaiveMatrixProvider.get(new double[][]{
                {-2, 1, 2},
                {3, 2, 1},
                {4, 3, 6},
        });

        var result = matrix1.mulM(matrix2);

        assertEquals(expectedMatrix, result);
    }

    @Test
    void mul2() {
        var expectedMatrix = NaiveMatrixProvider.get(new double[][]{
                {4, 5},
                {8, 17},
        });
        var matrix1 = NaiveMatrixProvider.get(new double[][]{
                {1, 2},
                {5, 6},
        });

        var matrix2 = NaiveMatrixProvider.get(new double[][]{
                {-2, 1},
                {3, 2},
        });

        var result = matrix1.mulM(matrix2);

        assertEquals(expectedMatrix, result);
    }

    @Test
    void mulTuple4() {
        var expectedTuple = Tuple.tuple(18, 24, 33, 1);
        var matrix1 = NaiveMatrixProvider.get(new double[][]{
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
        var identity = NaiveMatrixProvider.get(4, true);
        var matrix1 = NaiveMatrixProvider.get(new double[][]{
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
        var expected = NaiveMatrixProvider.get(new double[][]{
                {0, 9, 1, 0},
                {9, 8, 8, 0},
                {3, 0, 5, 5},
                {0, 8, 3, 8},
        });
        var matrix1 = NaiveMatrixProvider.get(new double[][]{
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
        var expected = NaiveMatrixProvider.get(new double[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1},
        });
        var matrix1 = NaiveMatrixProvider.get(new double[][]{
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
        var matrix = NaiveMatrixProvider.get(new double[][]{
                {1, 5},
                {-3, 2}
        });

        var determinant = matrix.determinant();

        assertEquals(17, determinant);
    }

    @Test
    void submatrix3() {
        var expected = NaiveMatrixProvider.get(new double[][]{
                {1, 5},
                {-3, 2},
        });
        var matrix = NaiveMatrixProvider.get(new double[][]{
                {1, 5, 0},
                {-3, 2, 7},
                {0, 6, -3},
        });

        var result = matrix.subM(2, 2);

        assertEquals(expected, result);
    }

    @Test
    void submatrix4() {
        var expected = NaiveMatrixProvider.get(new double[][]{
                {-6, 1, 1},
                {-8, 5, 8},
                {-1, 0, 8}
       });
        var matrix = NaiveMatrixProvider.get(new double[][]{
                {-6, 1, 1, 6},
                {-8, 5, 8, 6},
                {-1, 0, 8, 2},
                {-7, 1, -1, 1},
        });

        var result = matrix.subM(3, 3);

        assertEquals(expected, result);
    }

    @Test
    void minor3() {
        var matrix = NaiveMatrixProvider.get(new double[][]{
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
        var matrix = NaiveMatrixProvider.get(new double[][]{
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
        var matrix = NaiveMatrixProvider.get(new double[][]{
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
        var matrix = NaiveMatrixProvider.get(new double[][]{
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
        var matrix = NaiveMatrixProvider.get(new double[][]{
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
        var matrix = NaiveMatrixProvider.get(new double[][]{
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
        var matrix = NaiveMatrixProvider.get(new double[][]{
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


        var expectedInverse = NaiveMatrixProvider.get(new double[][]{
                {0.21804511278195488, 0.45112781954887216, 0.24060150375939848, -0.045112781954887216},
                {-0.8082706766917294, -1.4567669172932332, -0.44360902255639095, 0.5206766917293233},
                {-0.07894736842105263, -0.2236842105263158, -0.05263157894736842, 0.19736842105263158},
                {-0.5225563909774437, -0.8139097744360902, -0.3007518796992481, 0.30639097744360905}
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
        var matrix1 = NaiveMatrixProvider.get(new double[][]{
                {8, -5, 9, 2},
                {7, 5, 6, 1},
                {-6, 0, 9, 6},
                {-3, 0, -9, -4},
        });
        var matrix1ExpectedInvert = NaiveMatrixProvider.get(new double[][]{
                {-0.15384615, -0.15384615, -0.28205128, -0.53846153},
                {-0.07692307, 0.12307692, 0.02564102, 0.03076923},
                {0.35897435, 0.35897435, 0.43589743, 0.92307692},
                {-0.69230769, -0.69230769, -0.76923076, -1.92307692}
        });
        var matrix2 = NaiveMatrixProvider.get(new double[][]{
                {9, 3, 0, 9},
                {-5, -2, -6, -3},
                {-4, 9, 6, 4},
                {-7, 6, 6, 2},
        });
        var matrix2ExpectedInvert = NaiveMatrixProvider.get(new double[][]{
                {-0.04074074, -0.07777777, 0.14444444, -0.22222222},
                {-0.07777777, 0.03333333, 0.36666666, -0.33333333},
                {-0.02901234, -0.14629629, -0.10925925, 0.12962962},
                {0.17777777, 0.06666666, -0.26666666, 0.33333}
        });

        var inverse1 = matrix1.inverse();
        var inverse2 = matrix2.inverse();

        assertEquals(matrix1ExpectedInvert, inverse1);
        assertEquals(matrix2ExpectedInvert, inverse2);
    }

    @Test
    void multiplyInverse() {
        var matrix1 = NaiveMatrixProvider.get(new double[][]{
                {3, -9, 7, 3},
                {3, -8, 2, -9},
                {-4, 4, 4, 1},
                {-6, 5, -1, 1},
        });
        var matrix2 = NaiveMatrixProvider.get(new double[][]{
                {8, 2, 2, 2},
                {3, -1, 7, 0},
                {7, 0, 5, 4},
                {6, -2, 0, 5},
        });

        var multiplied = matrix1.mulM(matrix2);

        assertEquals(matrix1, multiplied.mulM(matrix2.inverse()));
    }

    @Test
    void translation() {
        var translation = NaiveMatrixProvider.translation(-1, 2, 3);

        var expectedMatrix = NaiveMatrixProvider.get(new double[][]{
                {1, 0, 0, -1},
                {0, 1, 0, 2},
                {0, 0, 1, 3},
                {0, 0, 0, 1}
        });

        assertEquals(expectedMatrix, translation);
    }

    @Test
    void translatePoint() {
        var translation = NaiveMatrixProvider.translation(5, -3, 2);
        var point = Tuple.point(-3, 4, 5);

        var translatedPoint = translation.mulT(point);

        assertTrue(translatedPoint.isPoint());
        assertEquals(2, translatedPoint.x());
        assertEquals(1, translatedPoint.y());
        assertEquals(7, translatedPoint.z());
    }

    @Test
    void translatePointWithInverse() {
        var translation = NaiveMatrixProvider.translation(5, -3, 2);
        var point = Tuple.point(-3, 4, 5);

        var translatedPoint = translation.inverse().mulT(point);

        assertTrue(translatedPoint.isPoint());
        assertEquals(-8, translatedPoint.x());
        assertEquals(7, translatedPoint.y());
        assertEquals(3, translatedPoint.z());
    }

    @Test
    void translateVector() {
        // translating a vector doesn't change the vector
        var translation = NaiveMatrixProvider.translation(5, -3, 2);
        var vector = Tuple.vector(-3, 4, 5);

        var translatedVector = translation.mulT(vector);

        assertTrue(translatedVector.isVector());
        assertEquals(-3, translatedVector.x());
        assertEquals(4, translatedVector.y());
        assertEquals(5, translatedVector.z());
    }

    @Test
    void scalePoint() {
        var scaling = NaiveMatrixProvider.scaling(2, 3, 4);
        var point = Tuple.point(-4, 6, 8);

        var scaledPoint = scaling.mulT(point);

        assertTrue(scaledPoint.isPoint());
        assertEquals(-8, scaledPoint.x());
        assertEquals(18, scaledPoint.y());
        assertEquals(32, scaledPoint.z());
    }

    @Test
    void scaleVector() {
        var scaling = NaiveMatrixProvider.scaling(2, 3, 4);
        var vector = Tuple.vector(-4, 6, 8);

        var scaledVector = scaling.mulT(vector);

        assertTrue(scaledVector.isVector());
        assertEquals(-8, scaledVector.x());
        assertEquals(18, scaledVector.y());
        assertEquals(32, scaledVector.z());
    }

    @Test
    void scaleVectorWithInverse() {
        var scaling = NaiveMatrixProvider.scaling(2, 3, 4);
        var vector = Tuple.vector(-4, 6, 8);

        var scaledVector = scaling.inverse().mulT(vector);

        assertTrue(scaledVector.isVector());
        assertEquals(-2, scaledVector.x());
        assertEquals(2, scaledVector.y());
        assertEquals(2, scaledVector.z());
    }

    @Test
    void scalePointWithInverse() {
        // this is essentially reflecting the point on the x-axis
        var scaling = NaiveMatrixProvider.scaling(-1, 1, 1);
        var point = Tuple.point(2, 3, 4);

        var scaledPoint = scaling.inverse().mulT(point);

        assertTrue(scaledPoint.isPoint());
        assertEquals(-2, scaledPoint.x());
        assertEquals(3, scaledPoint.y());
        assertEquals(4, scaledPoint.z());
    }

    @Test
    void rotatePointX() {
        var point = Tuple.point(0, 1, 0);
        var rotationHalfQuarter = NaiveMatrixProvider.rotationX(Math.toRadians(45)); // same as Math.PI / 4
        var rotationFUllQuarter = NaiveMatrixProvider.rotationX(Math.toRadians(90)); // same as Math.PI / 2

        var pointHalfQuarter = rotationHalfQuarter.mulT(point);
        var pointFullQuarter = rotationFUllQuarter.mulT(point);

        assertTrue(pointHalfQuarter.isPoint());
        assertEquals(Tuple.point(0, Math.sqrt(2) / 2, Math.sqrt(2) / 2), pointHalfQuarter);

        assertTrue(pointFullQuarter.isPoint());
        assertEquals(Tuple.point(0, 0, 1), pointFullQuarter);
    }

    @Test
    void rotatePointXInverse() {
        var point = Tuple.point(0, 1, 0);
        var rotationHalfQuarter = NaiveMatrixProvider.rotationX(Math.toRadians(45)); // same as Math.PI / 4

        var pointHalfQuarter = rotationHalfQuarter.inverse().mulT(point);

        assertTrue(pointHalfQuarter.isPoint());
        assertEquals(Tuple.point(0, Math.sqrt(2) / 2, -(Math.sqrt(2) / 2)), pointHalfQuarter);

    }

    @Test
    void rotatePointY() {
        var point = Tuple.point(0, 0, 1);
        var rotationHalfQuarter = NaiveMatrixProvider.rotationY(Math.toRadians(45)); // same as Math.PI / 4
        var rotationFUllQuarter = NaiveMatrixProvider.rotationY(Math.toRadians(90)); // same as Math.PI / 2

        var pointHalfQuarter = rotationHalfQuarter.mulT(point);
        var pointFullQuarter = rotationFUllQuarter.mulT(point);

        assertTrue(pointHalfQuarter.isPoint());
        assertEquals(Tuple.point(Math.sqrt(2) / 2, 0, Math.sqrt(2) / 2), pointHalfQuarter);

        assertTrue(pointFullQuarter.isPoint());
        assertEquals(Tuple.point(1, 0, 0), pointFullQuarter);
    }

    @Test
    void rotatePointYInverse() {
        var point = Tuple.point(0, 0, 1);
        var rotationHalfQuarter = NaiveMatrixProvider.rotationY(Math.toRadians(45)); // same as Math.PI / 4

        var pointHalfQuarter = rotationHalfQuarter.inverse().mulT(point);

        assertTrue(pointHalfQuarter.isPoint());
        assertEquals(Tuple.point(-(Math.sqrt(2)) / 2, 0, (Math.sqrt(2)) / 2), pointHalfQuarter);
    }

    @Test
    void rotatePointZ() {
        var point = Tuple.point(0, 1, 0);
        var rotationHalfQuarter = NaiveMatrixProvider.rotationZ(Math.toRadians(45)); // same as Math.PI / 4
        var rotationFUllQuarter = NaiveMatrixProvider.rotationZ(Math.toRadians(90)); // same as Math.PI / 2

        var pointHalfQuarter = rotationHalfQuarter.mulT(point);
        var pointFullQuarter = rotationFUllQuarter.mulT(point);

        assertTrue(pointHalfQuarter.isPoint());
        assertEquals(Tuple.point(-(Math.sqrt(2) / 2), Math.sqrt(2) / 2, 0), pointHalfQuarter);

        assertTrue(pointFullQuarter.isPoint());
        assertEquals(Tuple.point(-1, 0, 0), pointFullQuarter);
    }

    @Test
    void rotatePointZInverse() {
        var point = Tuple.point(0, 1, 0);
        var rotationHalfQuarter = NaiveMatrixProvider.rotationZ(Math.toRadians(45)); // same as Math.PI / 4

        var pointHalfQuarter = rotationHalfQuarter.inverse().mulT(point);

        assertTrue(pointHalfQuarter.isPoint());
        assertEquals(Tuple.point(Math.sqrt(2) / 2, Math.sqrt(2) / 2, 0), pointHalfQuarter);
    }

    @Test
    void shearPoint() {
        var point = Tuple.point(2, 3, 4);

        var shearingXY = NaiveMatrixProvider.shearing(1, 0, 0, 0, 0, 0);
        var shearingXZ = NaiveMatrixProvider.shearing(0, 1, 0, 0, 0, 0);
        var shearingYX = NaiveMatrixProvider.shearing(0, 0, 1, 0, 0, 0);
        var shearingYZ = NaiveMatrixProvider.shearing(0, 0, 0, 1, 0, 0);
        var shearingZX = NaiveMatrixProvider.shearing(0, 0, 0, 0, 1, 0);
        var shearingZY = NaiveMatrixProvider.shearing(0, 0, 0, 0, 0, 1);


        assertEquals(Tuple.point(5, 3, 4), shearingXY.mulT(point));
        assertEquals(Tuple.point(6, 3, 4), shearingXZ.mulT(point));
        assertEquals(Tuple.point(2, 5, 4), shearingYX.mulT(point));
        assertEquals(Tuple.point(2, 7, 4), shearingYZ.mulT(point));
        assertEquals(Tuple.point(2, 3, 6), shearingZX.mulT(point));
        assertEquals(Tuple.point(2, 3, 7), shearingZY.mulT(point));
    }

    @Test
    void transformationSequenceAndChaining() {
        var point = Tuple.point(1, 0, 1);
        var rotationX = NaiveMatrixProvider.rotationX(Math.PI / 2);
        var scaling = NaiveMatrixProvider.scaling(5, 5, 5);
        var translation = NaiveMatrixProvider.translation(10, 5, 7);

        var p1 = rotationX.mulT(point);
        assertEquals(Tuple.point(1, -1, 0), p1);

        var p2 = scaling.mulT(p1);
        assertEquals(Tuple.point(5, -5, 0), p2);

        var p3 = translation.mulT(p2);
        assertEquals(Tuple.point(15, 0, 7), p3);

        // chaining works in reverse order
        var chain = translation.mulM(scaling).mulM(rotationX);

        var chainedPoint = chain.mulT(point);
        assertEquals(Tuple.point(15, 0, 7), chainedPoint);
    }
}