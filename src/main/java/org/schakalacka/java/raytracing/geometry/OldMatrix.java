package org.schakalacka.java.raytracing.geometry;

import java.util.Arrays;

@Deprecated
@SuppressWarnings("unused")
public class OldMatrix {

    //    public static final double EPSILON = 0.000000000001;
    public static final double EPSILON = 0.00000001;

    private double[][] matrix;

    private OldMatrix() {
    }

    public static OldMatrix get(double[][] ref) {
        OldMatrix result = OldMatrix.get(ref.length);
        result.matrix = Arrays.stream(ref).map(double[]::clone).toArray(double[][]::new);
        return result;
    }

    public static OldMatrix get(int size) {
        return OldMatrix.get(size, false);
    }

    public static OldMatrix get(int size, boolean isIdentity) {
        if (isIdentity) {
            OldMatrix result = new OldMatrix();
            result.matrix = new double[size][size];
            for (int i = 0; i < size; i++) {
                result.matrix[i][i] = 1;
            }
            return result;

        } else {
            OldMatrix result = new OldMatrix();
            result.matrix = new double[size][size];
            result.matrix.clone();
            return result;
        }
    }

    /***
     * create a translation matrix. It is a 4x4 identity matrix, where the last colum is populated with the 3 values.
     */
    public static OldMatrix translation(double x, double y, double z) {
        return OldMatrix.get(new double[][]{
                {1, 0, 0, x},
                {0, 1, 0, y},
                {0, 0, 1, z},
                {0, 0, 0, 1},
        });
    }

    public static OldMatrix scaling(double x, double y, double z) {
        return OldMatrix.get(new double[][]{
                {x, 0, 0, 0},
                {0, y, 0, 0},
                {0, 0, z, 0},
                {0, 0, 0, 1},
        });
    }

    /***
     *
     * @return a left-handed rotation matrix along the X axis
     */
    public static OldMatrix rotationX(double radians) {
        return OldMatrix.get(new double[][]{
                {1, 0, 0, 0},
                {0, Math.cos(radians), -Math.sin(radians), 0},
                {0, Math.sin(radians), Math.cos(radians), 0},
                {0, 0, 0, 1},
        });
    }

    /***
     *
     * @return a left-handed rotation matrix along the Y axis
     */
    public static OldMatrix rotationY(double radians) {
        return OldMatrix.get(new double[][]{
                {Math.cos(radians), 0, Math.sin(radians), 0},
                {0, 1, 0, 0},
                {-Math.sin(radians), 0, Math.cos(radians), 0},
                {0, 0, 0, 1},
        });
    }

    public static OldMatrix rotationZ(double radians) {
        return OldMatrix.get(new double[][]{
                {Math.cos(radians), -Math.sin(radians), 0, 0},
                {Math.sin(radians), Math.cos(radians), 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1},
        });
    }

    public static OldMatrix shearing(double xy, double xz, double yx, double yz, double zx, double zy) {
        return OldMatrix.get(new double[][]{
                {1, xy, xz, 0},
                {yx, 1, yz, 0},
                {zx, zy, 1, 0},
                {0, 0, 0, 1},
        });

    }

    public double get(int r, int c) {
        return matrix[r][c];
    }

    private void set(int row, int col, double val) {
        this.matrix[row][col] = val;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OldMatrix that = (OldMatrix) o;

        int length = this.matrix.length;
        if (that.matrix.length != length)
            return false;

        for (int i = 0; i < length; i++) {
            double[] e1 = this.matrix[i];
            double[] e2 = that.matrix[i];

            if (e1 == e2)
                continue;
            if (e1 == null)
                return false;
            if (e1.length != e2.length)
                return false;

            // Figure out whether the two elements are equal
            for (int j = 0; j < e1.length; j++) {
                double val1 = e1[j];
                double val2 = e2[j];
                if ((val1 - val2) >= EPSILON)
                    return false;
            }
        }
        return true;

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
    public OldMatrix mulM(OldMatrix that) {
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

        return OldMatrix.get(result);
    }

    public Tuple mulT(Tuple that) {

        double x = 0;
        for (int i = 0; i < matrix.length; i++) {
            x += this.matrix[0][i] * that.get(i);
        }
        double y = 0;
        for (int i = 0; i < matrix.length; i++) {
            y += this.matrix[1][i] * that.get(i);
        }
        double z = 0;
        for (int i = 0; i < matrix.length; i++) {
            z += this.matrix[2][i] * that.get(i);
        }
        double w = 0;
        if (this.matrix.length <= 3) {
            w = that.w();
        } else {
            for (int i = 0; i < matrix.length; i++) {
                w += this.matrix[3][i] * that.get(i);
            }
        }

        return Tuple.tuple(x, y, z, w);
    }

    public OldMatrix transpose() {
        final OldMatrix m = OldMatrix.get(matrix.length);

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                m.matrix[j][i] = this.matrix[i][j];
            }
        }

        return m;
    }

    public double determinant() {
        double determinant = 0;
        if (matrix.length == 2) {
            determinant = matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        } else {
            for (int col = 0; col < matrix.length; col++) {
                determinant += matrix[0][col] * cofactor(0, col);
            }
        }
        return determinant;
    }

    public OldMatrix subM(int r, int c) {
        final OldMatrix m = OldMatrix.get(this.matrix.length - 1);

        int targetRow = 0;
        int targetCol = 0;
        for (int row = 0; row < this.matrix.length; row++) {
            if (row != r) {
                for (int col = 0; col < this.matrix.length; col++) {
                    if (col != c) {
                        m.matrix[targetRow][targetCol] = this.matrix[row][col];
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

    public OldMatrix inverse() {
        if (!isInvertible()) {
            throw new ArithmeticException("Matrix not invertible");
        }

        OldMatrix newMatrix = OldMatrix.get(this.matrix.length);
        double determinant = determinant();
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix.length; col++) {
                double cofactor = cofactor(row, col);
                newMatrix.set(col, row, cofactor / determinant);
            }
        }

        return newMatrix;
    }


}
