package org.schakalacka.java.raytracing.geometry.algebra;

import org.schakalacka.java.raytracing.Constants;
import org.schakalacka.java.raytracing.Counter;

import java.util.Arrays;

public class NaiveMatrix implements Matrix {

    private final int size;

    /***
     * row-column. mapped 2d-array to 1d
     */
    private final float[] matrix;

    NaiveMatrix(int size) {
        this.size = size;
        this.matrix = new float[size * size];
    }


    @Override
    public double get(int row, int col) {
        return matrix[row * size + col];
    }

    @Override
    public void set(int row, int col, double val) {
        this.matrix[row * size + col] = (float) val;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NaiveMatrix that = (NaiveMatrix) o;

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
        return "Matrix{" +
                "matrix=" + Arrays.toString(matrix) +
                '}';
    }

    /***
     * for each target cell, multiply this.row with that.column
     * i.e. for a 4-Matrix, result[2][3] is the sum of the products over this[2][0-3] and that[0-3][3]
     */
    @Override
    public Matrix mulM(Matrix that) {
        Counter.mulM++;
        NaiveMatrix result = new NaiveMatrix(this.size);

        for (int r = 0; r < this.size; r++) {
            for (int c = 0; c < this.size; c++) {
                double newVal = 0;

                for (int i = 0; i < this.size; i++) {
                    newVal += this.get(r, i) * that.get(i, c);
                    result.set(r, c, newVal);
                }
            }
        }

        return result;
    }

    @Override
    public Tuple mulT(Tuple that) {
        Counter.mulT++;
        float x = 0;
        for (int i = 0; i < this.size; i++) {
            x += this.get(0, i) * that.get(i);
        }
        float y = 0;
        for (int i = 0; i < this.size; i++) {
            y += this.get(1, i) * that.get(i);
        }
        double z = 0;
        for (int i = 0; i < this.size; i++) {
            z += this.get(2, i) * that.get(i);
        }
        float w = 0;
        if (this.size <= 3) {
            w = (float) that.w();
        } else {
            for (int i = 0; i < this.size; i++) {
                w += this.get(3, i) * that.get(i);
            }
        }

        return Tuple.tuple(x, y, z, w);
    }

    @Override
    public Matrix transpose() {
        Counter.transpose++;
        final NaiveMatrix m = new NaiveMatrix(this.size);

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
    public Matrix subM(int r, int c) {
        Counter.subM++;
        final NaiveMatrix m = new NaiveMatrix(this.size - 1);

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

    /***
     * A cofactor is a minor, potentially negated. If the target sums up to an odd number, negate the minor.
     */
    @Override
    public double cofactor(int r, int c) {
        Counter.cofactor++;
        int factor = (r + c) % 2 == 0 ? 1 : -1;
        return minor(r, c) * factor;
    }

    @Override
    public boolean isInvertible() {
        Counter.isInvertible++;
        return determinant() != 0;
    }

    @Override
    public Matrix inverse() {
        Counter.inverse++;
        if (!isInvertible()) {
            throw new ArithmeticException("Matrix not invertible");
        }

        Matrix newMatrix = new NaiveMatrix(this.size);
        double determinant = determinant();
        for (int row = 0; row < this.size; row++) {
            for (int col = 0; col < this.size; col++) {
                double cofactor = cofactor(row, col);
                newMatrix.set(col, row, cofactor / determinant);
            }
        }

        return newMatrix;
    }


}
