package org.schakalacka.java.raytracing.math;

import org.schakalacka.java.raytracing.Counter;

public class MatrixProvider {

    public static MATRIX_TYPE MT = MATRIX_TYPE.CUBLAS;

    public static Matrix get(float[][] ref) {
        return MT.provider.get(ref);
    }

    public static Matrix get(int size) {
        return MT.provider.get(size);
    }

    public static Matrix get(int size, boolean isIdentity) {
        return MT.provider.get(size, isIdentity);
    }

    /***
     * create a translation matrix. It is a 4x4 identity matrix, where the last colum is populated with the 3 values.
     */
    public static Matrix translation(float x, float y, float z) {
        Counter.translate++;
        return MT.provider.translation(x, y, z);
    }

    public static Matrix scaling(float x, float y, float z) {
        Counter.scale++;
        return MT.provider.scaling(x, y, z);
    }

    /***
     *
     * @return a left-handed rotation matrix along the X axis
     */
    public static Matrix rotationX(float radians) {
        Counter.rotX++;
        return MT.provider.rotationX(radians);
    }

    /***
     *
     * @return a left-handed rotation matrix along the Y axis
     */
    public static Matrix rotationY(float radians) {
        Counter.rotY++;
        return MT.provider.rotationY(radians);
    }

    public static Matrix rotationZ(float radians) {
        Counter.rotZ++;
        return MT.provider.rotationZ(radians);
    }

    public static Matrix shearing(float xy, float xz, float yx, float yz, float zx, float zy) {
        Counter.shear++;
        return MT.provider.shearing(xy, xz, yx, yz, zx, zy);
    }

}
