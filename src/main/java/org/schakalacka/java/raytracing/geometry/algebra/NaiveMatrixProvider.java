package org.schakalacka.java.raytracing.geometry.algebra;

class NaiveMatrixProvider {
    
    
    static Matrix get(double[][] ref) {
       
        Matrix result = NaiveMatrixProvider.get(ref.length);

        for (int row = 0; row < ref.length; row++) {
            for (int col = 0; col < ref.length; col++) {
                result.set(row, col, ref[row][col]);
            }
        }
        return result;
    }

    static Matrix get(int size) {
        return NaiveMatrixProvider.get(size, false);
    }

    static Matrix get(int size, boolean isIdentity) {
        Matrix result = new NaiveMatrix(size);

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
    static Matrix translation(double x, double y, double z) {
        Matrix result = NaiveMatrixProvider.get(4, true);
        result.set(0, 3, x);
        result.set(1, 3, y);
        result.set(2, 3, z);

        return result;
//                {1, 0, 0, x},
//                {0, 1, 0, y},
//                {0, 0, 1, z},
//                {0, 0, 0, 1},
    }

    static Matrix scaling(double x, double y, double z) {
        Matrix result = NaiveMatrixProvider.get(4, false);
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
    static Matrix rotationX(double radians) {
        Matrix result = NaiveMatrixProvider.get(4, true);
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
    static Matrix rotationY(double radians) {
        Matrix result = NaiveMatrixProvider.get(4, true);
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

    static Matrix rotationZ(double radians) {
        Matrix result = NaiveMatrixProvider.get(4, true);
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

    static Matrix shearing(double xy, double xz, double yx, double yz, double zx, double zy) {
        Matrix result = NaiveMatrixProvider.get(4, true);
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

}
