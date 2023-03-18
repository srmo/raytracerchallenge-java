package org.schakalacka.java.raytracing.math;

public interface IMatrixProvider {
    Matrix get(double[][] ref);

    Matrix get(int size);

    Matrix get(int size, boolean isIdentity);

    Matrix translation(double x, double y, double z);

    Matrix scaling(double x, double y, double z);

    Matrix rotationX(double radians);

    Matrix rotationY(double radians);

    Matrix rotationZ(double radians);

    Matrix shearing(double xy, double xz, double yx, double yz, double zx, double zy);
}
