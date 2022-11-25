package org.schakalacka.java.raytracing.geometry.algebra;

public class MatrixProvider {

    public enum MATRIX_TYPE {
        NAIVE,
        EJML
    }


    public static MATRIX_TYPE MT = MATRIX_TYPE.EJML;

    public static Matrix get(double[][] ref) {
        switch (MT) {
            case NAIVE -> {
                return NaiveMatrixProvider.get(ref);
            }
            case EJML -> {
                return EjmlMatrixProvider.get(ref);
            }
            default -> throw new RuntimeException("Unsupported MatrixProvider defined [ " + MT + "]");
        }
    }

    public static Matrix get(int size) {
        switch (MT) {
            case NAIVE -> {
                return NaiveMatrixProvider.get(size);
            }
            case EJML -> {
                return EjmlMatrixProvider.get(size);
            }
            default -> throw new RuntimeException("Unsupported MatrixProvider defined [ " + MT + "]");
        }
    }

    public static Matrix get(int size, boolean isIdentity) {
        switch (MT) {
            case NAIVE -> {
                return NaiveMatrixProvider.get(size, isIdentity);
            }
            case EJML -> {
                return EjmlMatrixProvider.get(size, isIdentity);
            }
            default -> throw new RuntimeException("Unsupported MatrixProvider defined [ " + MT + "]");
        }
    }

    /***
     * create a translation matrix. It is a 4x4 identity matrix, where the last colum is populated with the 3 values.
     */
    public static Matrix translation(double x, double y, double z) {
        switch (MT) {
            case NAIVE -> {
                return NaiveMatrixProvider.translation(x, y, z);
            }
            case EJML -> {
                return EjmlMatrixProvider.translation(x, y, z);
            }
            default -> throw new RuntimeException("Unsupported MatrixProvider defined [ " + MT + "]");
        }    }

    public static Matrix scaling(double x, double y, double z) {
        switch (MT) {
            case NAIVE -> {
                return NaiveMatrixProvider.scaling(x, y, z);
            }
            case EJML -> {
                return EjmlMatrixProvider.scaling(x, y, z);
            }
            default -> throw new RuntimeException("Unsupported MatrixProvider defined [ " + MT + "]");
        }    }

    /***
     *
     * @return a left-handed rotation matrix along the X axis
     */
    public static Matrix rotationX(double radians) {
        switch (MT) {
            case NAIVE -> {
                return NaiveMatrixProvider.rotationX(radians);
            }
            case EJML -> {
                return EjmlMatrixProvider.rotationX(radians);
            }
            default -> throw new RuntimeException("Unsupported MatrixProvider defined [ " + MT + "]");
        }    }

    /***
     *
     * @return a left-handed rotation matrix along the Y axis
     */
    public static Matrix rotationY(double radians) {
        switch (MT) {
            case NAIVE -> {
                return NaiveMatrixProvider.rotationY(radians);
            }
            case EJML -> {
                return EjmlMatrixProvider.rotationY(radians);
            }
            default -> throw new RuntimeException("Unsupported MatrixProvider defined [ " + MT + "]");
        }
    }

    public static Matrix rotationZ(double radians) {
        switch (MT) {
            case NAIVE -> {
                return NaiveMatrixProvider.rotationZ(radians);
            }
            case EJML -> {
                return EjmlMatrixProvider.rotationZ(radians);
            }
            default -> throw new RuntimeException("Unsupported MatrixProvider defined [ " + MT + "]");
        }
    }

    public static Matrix shearing(double xy, double xz, double yx, double yz, double zx, double zy) {
        switch (MT) {
            case NAIVE -> {
                return NaiveMatrixProvider.shearing(xy, xz, yx, yz, zx, zy);
            }
            case EJML -> {
                return EjmlMatrixProvider.shearing(xy, xz, yx, yz, zx, zy);
            }
            default -> throw new RuntimeException("Unsupported MatrixProvider defined [ " + MT + "]");
        }
    }

}
