package org.schakalacka.java.raytracing.math;

import org.schakalacka.java.raytracing.Counter;

public class MatrixProvider {

    public static MATRIX_TYPE MT = MATRIX_TYPE.EJML;

    public static Matrix get(double[][] ref) {
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
    public static Matrix translation(double x, double y, double z) {
        Counter.translate++;
        return MT.provider.translation(x, y, z);
    }

    public static Matrix scaling(double x, double y, double z) {
        Counter.scale++;
        return MT.provider.scaling(x, y, z);
    }

    /***
     *
     * @return a left-handed rotation matrix along the X axis
     */
    public static Matrix rotationX(double radians) {
        Counter.rotX++;
        return MT.provider.rotationX(radians);
    }

    /***
     *
     * @return a left-handed rotation matrix along the Y axis
     */
    public static Matrix rotationY(double radians) {
        Counter.rotY++;
        return MT.provider.rotationY(radians);
    }

    public static Matrix rotationZ(double radians) {
        Counter.rotZ++;
        return MT.provider.rotationZ(radians);
    }

    public static Matrix shearing(double xy, double xz, double yx, double yz, double zx, double zy) {
        Counter.shear++;
        return MT.provider.shearing(xy, xz, yx, yz, zx, zy);
    }

}
