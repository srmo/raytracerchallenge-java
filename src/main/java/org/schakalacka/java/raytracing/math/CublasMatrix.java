package org.schakalacka.java.raytracing.math;

import org.schakalacka.java.raytracing.Constants;
import org.schakalacka.java.raytracing.Counter;
import org.schakalacka.java.raytracing.math.cublas.RayTracingCublas;

import java.util.Arrays;

/***
 * We use column-major here, as CUBLAS/JCuda uses column-major
 */
public class CublasMatrix implements Matrix {

    /***
     * column-row. mapped 2d-array to 1d
     */
    final double[] matrix;

    private final int size;

    public CublasMatrix(double[] matrix, int size) {
        this.size = size;
        this.matrix = matrix;
    }

    public CublasMatrix(float[] matrix, int size) {
        this(convertToDoubleArray(matrix), size);
    }

    public CublasMatrix(int size) {
        this.size = size;
        this.matrix = new double[size * size];
    }

    @Override
    public double get(int row, int col) {
        return matrix[col * size + row];
    }

    @Override
    public void set(int row, int column, double val) {
        this.matrix[column * size + row] =  val;
    }

    @Override
    public Matrix mulM(Matrix that) {
        Counter.mulM++;

        return RayTracingCublas.mulM(convertToFloatArray(this.matrix), convertToFloatArray(((CublasMatrix) that).matrix), size);
    }

    private static float[] convertToFloatArray(double[] array) {
        float[] floatArray = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            floatArray[i] = (float) array[i];
        }
        return floatArray;
    }

    private static double[] convertToDoubleArray(float[] array) {
        double[] doubleArray = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            doubleArray[i] =  array[i];
        }
        return doubleArray;
    }

    @Override
    public Tuple mulT(Tuple that) {
        Counter.mulT++;
        return RayTracingCublas.mulT(convertToFloatArray(this.matrix), that, size);
    }

    @Override
    public CublasMatrix transpose() {
        Counter.transpose++;
        final CublasMatrix m = new CublasMatrix(this.size);

        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                m.set(j, i, this.get(i, j));
            }
        }

        return m;
    }

    @Override
    public double determinant() {
        Counter.determinant++;
        double determinant = 0;
        if (this.size == 2) {
            determinant = this.get(0, 0) * this.get(1, 1) - this.get(0, 1) * this.get(1, 0);
        } else {
            for (int col = 0; col < this.size; col++) {
                determinant += this.get(0, col) * cofactor(0, col);
            }
        }
        return determinant;
    }

    @Override
    public CublasMatrix subM(int r, int c) {
        Counter.subM++;
        final CublasMatrix m = new CublasMatrix(this.size - 1);

        int targetRow = 0;
        int targetCol = 0;
        for (int row = 0; row < this.size; row++) {
            if (row != r) {
                for (int col = 0; col < this.size; col++) {
                    if (col != c) {
                        m.set(targetRow, targetCol, this.get(row, col));
                        targetCol++;
                    }
                }
                targetRow++;
                targetCol = 0;
            }
        }
        return m;
    }

    @Override
    public double minor(int r, int c) {
        Counter.minor++;
        return this.subM(r, c).determinant();
    }

    @Override
    public double cofactor(int r, int c) {
        Counter.cofactor++;
        int factor = (r + c) % 2 == 0 ? 1 : -1;
        return minor(r, c) * factor;
    }

    @Override
    public boolean isInvertible() {
        return determinant() != 0;
    }

    @Override
    public CublasMatrix inverse() {
        Counter.inverse++;
        if (!isInvertible()) {
            throw new ArithmeticException("Matrix not invertible");
        }

        CublasMatrix newMatrix = new CublasMatrix(this.size);
        double determinant = determinant();
        for (int row = 0; row < this.size; row++) {
            for (int col = 0; col < this.size; col++) {
                double cofactor = cofactor(row, col);
                newMatrix.set(col, row,  (cofactor / determinant));
            }
        }

        return newMatrix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CublasMatrix that = (CublasMatrix) o;

        int length = this.matrix.length;
        if (that.matrix.length != length)
            return false;

        for (int i = 0; i < length; i++) {
            double e1 = this.matrix[i];
            double e2 = that.matrix[i];

            if (e1 == e2)
                continue;

            if (Math.abs(e1 - e2) >= Constants.EQUALS_EPSILON)
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(matrix);
    }

    @Override
    public String toString() {
        return "CublasMatrix{" +
                "matrix=" + Arrays.toString(matrix) +
                '}';
    }
}
