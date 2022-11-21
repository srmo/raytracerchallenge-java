package org.schakalacka.java.raytracing.geometry;

import org.schakalacka.java.raytracing.Constants;

import java.util.Arrays;

public class Matrix {

    private final int size;

    /***
     * row-column. mapped 2d-array to 1d
     */
    private final double[] matrix;

    private Matrix(int size) {
        this.size = size;
        this.matrix = new double[size * size];
    }

    public static org.schakalacka.java.raytracing.geometry.Matrix get(double[][] ref) {
        org.schakalacka.java.raytracing.geometry.Matrix result = org.schakalacka.java.raytracing.geometry.Matrix.get(ref.length);

        for (int row = 0; row < ref.length; row++) {
            for (int col = 0; col < ref.length; col++) {
                result.set(row, col, ref[row][col]);
            }
        }
        return result;
    }

    public static org.schakalacka.java.raytracing.geometry.Matrix get(int size) {
        return org.schakalacka.java.raytracing.geometry.Matrix.get(size, false);
    }

    public static org.schakalacka.java.raytracing.geometry.Matrix get(int size, boolean isIdentity) {
        org.schakalacka.java.raytracing.geometry.Matrix result = new org.schakalacka.java.raytracing.geometry.Matrix(size);

        if (isIdentity) {
            for (int i = 0; i < size; i++) {
                result.set(i, i, 1);
            }
        }
        return result;
    }

    /***
     * create a translation matrix. It is a 4x4 identity matrix, where the last colum is populated with the 3 values.
     */
    public static org.schakalacka.java.raytracing.geometry.Matrix translation(double x, double y, double z) {
        org.schakalacka.java.raytracing.geometry.Matrix result = org.schakalacka.java.raytracing.geometry.Matrix.get(4, true);
        result.set(0, 3, x);
        result.set(1, 3, y);
        result.set(2, 3, z);

        return result;
//                {1, 0, 0, x},
//                {0, 1, 0, y},
//                {0, 0, 1, z},
//                {0, 0, 0, 1},
    }

    public static org.schakalacka.java.raytracing.geometry.Matrix scaling(double x, double y, double z) {
        org.schakalacka.java.raytracing.geometry.Matrix result = org.schakalacka.java.raytracing.geometry.Matrix.get(4, false);
        result.set(0, 0, x);
        result.set(1, 1, y);
        result.set(2, 2, z);
        result.set(3, 3, 1);

        return result;
//                {x, 0, 0, 0},
//                {0, y, 0, 0},
//                {0, 0, z, 0},
//                {0, 0, 0, 1},
    }

    /***
     *
     * @return a left-handed rotation matrix along the X axis
     */
    public static org.schakalacka.java.raytracing.geometry.Matrix rotationX(double radians) {
        org.schakalacka.java.raytracing.geometry.Matrix result = org.schakalacka.java.raytracing.geometry.Matrix.get(4, true);
        result.set(1, 1, Math.cos(radians));
        result.set(1, 2, -Math.sin(radians));
        result.set(2, 1, Math.sin(radians));
        result.set(2, 2, Math.cos(radians));

        return result;

//                {1, 0, 0, 0},
//                {0, Math.cos(radians), -Math.sin(radians), 0},
//                {0, Math.sin(radians), Math.cos(radians), 0},
//                {0, 0, 0, 1},
    }

    /***
     *
     * @return a left-handed rotation matrix along the Y axis
     */
    public static org.schakalacka.java.raytracing.geometry.Matrix rotationY(double radians) {
        org.schakalacka.java.raytracing.geometry.Matrix result = org.schakalacka.java.raytracing.geometry.Matrix.get(4, true);
        result.set(0, 0, Math.cos(radians));
        result.set(0, 1, 0);
        result.set(0, 2, Math.sin(radians));
        result.set(0, 3, 0);
        result.set(1, 0, 0);
        result.set(1, 1, 1);
        result.set(1, 2, 0);
        result.set(1, 3, 0);
        result.set(2, 0, -Math.sin(radians));
        result.set(2, 1, 0);
        result.set(2, 2, Math.cos(radians));
        result.set(2, 3, 0);
        result.set(3, 0, 0);
        result.set(3, 1, 0);
        result.set(3, 2, 0);
        result.set(3, 3, 1);

        return result;

//                {Math.cos(radians), 0, Math.sin(radians), 0},
//                {0, 1, 0, 0},
//                {-Math.sin(radians), 0, Math.cos(radians), 0},
//                {0, 0, 0, 1},
    }

    public static org.schakalacka.java.raytracing.geometry.Matrix rotationZ(double radians) {
        org.schakalacka.java.raytracing.geometry.Matrix result = org.schakalacka.java.raytracing.geometry.Matrix.get(4, true);
        result.set(0, 0, Math.cos(radians));
        result.set(0, 1, -Math.sin(radians));
        result.set(1, 0, Math.sin(radians));
        result.set(1, 1, Math.cos(radians));

        return result;


//                {Math.cos(radians), -Math.sin(radians), 0, 0},
//                {Math.sin(radians), Math.cos(radians), 0, 0},
//                {0, 0, 1, 0},
//                {0, 0, 0, 1},
    }

    public static org.schakalacka.java.raytracing.geometry.Matrix shearing(double xy, double xz, double yx, double yz, double zx, double zy) {
        org.schakalacka.java.raytracing.geometry.Matrix result = org.schakalacka.java.raytracing.geometry.Matrix.get(4, true);
        result.set(0, 1, xy);
        result.set(0, 2, xz);
        result.set(1, 0, yx);
        result.set(1, 2, yz);
        result.set(2, 0, zx);
        result.set(2, 1, zy);

        return result;

//                {1, xy, xz, 0},
//                {yx, 1, yz, 0},
//                {zx, zy, 1, 0},
//                {0, 0, 0, 1},
    }

    public double get(int row, int col) {
        return matrix[row * size + col];
    }

    private void set(int row, int col, double val) {
        this.matrix[row * size + col] = val;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        org.schakalacka.java.raytracing.geometry.Matrix that = (org.schakalacka.java.raytracing.geometry.Matrix) o;

        int length = this.matrix.length;
        if (that.matrix.length != length)
            return false;

        for (int i = 0; i < length; i++) {
            double e1 = this.matrix[i];
            double e2 = that.matrix[i];

            if (e1 == e2)
                continue;

            if (Math.abs(e1 - e2) >= Constants.EPSILON)
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
    public org.schakalacka.java.raytracing.geometry.Matrix mulM(org.schakalacka.java.raytracing.geometry.Matrix that) {
        org.schakalacka.java.raytracing.geometry.Matrix result = new org.schakalacka.java.raytracing.geometry.Matrix(this.size);

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

    public Tuple mulT(Tuple that) {

        double x = 0;
        for (int i = 0; i < this.size; i++) {
            x += this.get(0, i) * that.get(i);
        }
        double y = 0;
        for (int i = 0; i < this.size; i++) {
            y += this.get(1, i) * that.get(i);
        }
        double z = 0;
        for (int i = 0; i < this.size; i++) {
            z += this.get(2, i) * that.get(i);
        }
        double w = 0;
        if (this.size <= 3) {
            w = that.w();
        } else {
            for (int i = 0; i < this.size; i++) {
                w += this.get(3, i) * that.get(i);
            }
        }

        return Tuple.tuple(x, y, z, w);
    }

    public org.schakalacka.java.raytracing.geometry.Matrix transpose() {
        final org.schakalacka.java.raytracing.geometry.Matrix m = new org.schakalacka.java.raytracing.geometry.Matrix(this.size);

        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                m.set(j, i, this.get(i, j));
            }
        }

        return m;
    }

    public double determinant() {
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

    public org.schakalacka.java.raytracing.geometry.Matrix subM(int r, int c) {
        final org.schakalacka.java.raytracing.geometry.Matrix m = new org.schakalacka.java.raytracing.geometry.Matrix(this.size - 1);

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

    public double minor(int r, int c) {
        return this.subM(r, c).determinant();
    }

    /***
     * A cofactor is a minor, potentially negated. If the target sums up to an odd number, negate the minor.
     */
    public double cofactor(int r, int c) {
        int factor = (r + c) % 2 == 0 ? 1 : -1;
        return minor(r, c) * factor;
    }

    public boolean isInvertible() {
        return determinant() != 0;
    }

    public org.schakalacka.java.raytracing.geometry.Matrix inverse() {
        if (!isInvertible()) {
            throw new ArithmeticException("Matrix not invertible");
        }

        org.schakalacka.java.raytracing.geometry.Matrix newMatrix = org.schakalacka.java.raytracing.geometry.Matrix.get(this.size);
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
