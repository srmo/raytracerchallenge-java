package org.schakalacka.java.raytracing;

import java.util.Arrays;

/**
 * <p>&copy; 2022 <a href="http://www.ponton.de" target="_blank">PONTON GmbH</a></p>
 * <p>
 * [description of the class]
 *
 * @author <a href="mueller@ponton.de">Stephan Müller</a>
 * @since 2022-10-30
 * [potential @see links]
 */
public class Matrix {

    private final double[][] matrix;

    public Matrix(int size) {
        this.matrix = new double[size][size];
    }

    public Matrix(int size, boolean isIdentity) {
        this(size);
        if (isIdentity) {
            for (int i = 0; i < size; i++) {
                matrix[i][i] = 1;
            }
        }
    }

    public Matrix(double[][] ref) {
        this.matrix = Arrays.stream(ref).map(double[]::clone).toArray(double[][]::new);
    }

    public double get(int r, int c) {
        return matrix[r][c];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matrix matrix1 = (Matrix) o;
        return Arrays.deepEquals(matrix, matrix1.matrix);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(matrix);
    }

    @Override
    public String toString() {
        return "Matrix{" +
                "matrix=" + Arrays.deepToString(matrix) +
                '}';
    }

    /***
     * for each target cell, multiply this.row with that.column
     * i.e. for a 4-Matrix, result[2][3] is the sum of the products over this[2][0-3] and that[0-3][3]
     */
    public Matrix mulM(Matrix that) {
        double[][] result = new double[this.matrix.length][this.matrix.length];

        for (int r = 0; r < matrix.length; r++) {
            for (int c = 0; c < matrix.length; c++) {
                double newVal = 0;
                for (int i = 0; i < matrix.length; i++) {
                    newVal += this.matrix[r][i] * that.matrix[i][c];
                    result[r][c] = newVal;
                }
            }
        }

        return new Matrix(result);
    }

    public Tuple mulT(Tuple that) {

        float x = 0;
        for (int i = 0; i < matrix.length; i++) {
            x += this.matrix[0][i] * that.get(i);
        }
        float y = 0;
        for (int i = 0; i < matrix.length; i++) {
            y += this.matrix[1][i] * that.get(i);
        }
        float z = 0;
        for (int i = 0; i < matrix.length; i++) {
            z += this.matrix[2][i] * that.get(i);
        }
        float w = 0;
        for (int i = 0; i < matrix.length; i++) {
            w += this.matrix[3][i] * that.get(i);
        }

        return Tuple.tuple(x, y, z, w);
    }
}
