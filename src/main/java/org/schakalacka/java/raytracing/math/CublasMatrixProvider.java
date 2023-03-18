package org.schakalacka.java.raytracing.math;

import org.schakalacka.java.raytracing.Counter;

class CublasMatrixProvider implements IMatrixProvider {


    public CublasMatrix get(double[][] ref) {

        CublasMatrix result = this.get(ref.length);

        for (int col = 0; col < ref.length; col++) {
            for (int row = 0; row < ref.length; row++) {
                result.set(row, col, ref[row][col]);
            }
        }
        return result;
    }

    public CublasMatrix get(int size) {
        return this.get(size, false);
    }

    public CublasMatrix get(int size, boolean isIdentity) {
        CublasMatrix result = new CublasMatrix(new double[size * size], size);

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
    public CublasMatrix translation(double x, double y, double z) {
        Counter.translate++;
        CublasMatrix result = this.get(4, true);
        result.set(0, 3, x);
        result.set(1, 3, y);
        result.set(2, 3, z);

        return result;
//                {1, 0, 0, x},
//                {0, 1, 0, y},
//                {0, 0, 1, z},
//                {0, 0, 0, 1},
    }

    public CublasMatrix scaling(double x, double y, double z) {
        Counter.scale++;
        CublasMatrix result = this.get(4, false);
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
    public CublasMatrix rotationX(double radians) {
        Counter.rotX++;
        CublasMatrix result = this.get(4, true);
        result.set(1, 1,  Math.cos(radians));
        result.set(1, 2,  -Math.sin(radians));
        result.set(2, 1,  Math.sin(radians));
        result.set(2, 2,  Math.cos(radians));

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
    public CublasMatrix rotationY(double radians) {
        Counter.rotY++;
        CublasMatrix result = this.get(4, true);
        result.set(0, 0,  Math.cos(radians));
        result.set(0, 1, 0);
        result.set(0, 2,  Math.sin(radians));
        result.set(0, 3, 0);
        result.set(1, 0, 0);
        result.set(1, 1, 1);
        result.set(1, 2, 0);
        result.set(1, 3, 0);
        result.set(2, 0,  -Math.sin(radians));
        result.set(2, 1, 0);
        result.set(2, 2,  Math.cos(radians));
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

    public CublasMatrix rotationZ(double radians) {
        Counter.rotZ++;
        CublasMatrix result = this.get(4, true);
        result.set(0, 0,  Math.cos(radians));
        result.set(0, 1,  -Math.sin(radians));
        result.set(1, 0,  Math.sin(radians));
        result.set(1, 1,  Math.cos(radians));

        return result;


//                {Math.cos(radians), -Math.sin(radians), 0, 0},
//                {Math.sin(radians), Math.cos(radians), 0, 0},
//                {0, 0, 1, 0},
//                {0, 0, 0, 1},
    }

    public CublasMatrix shearing(double xy, double xz, double yx, double yz, double zx, double zy) {
        Counter.shear++;
        CublasMatrix result = this.get(4, true);
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
