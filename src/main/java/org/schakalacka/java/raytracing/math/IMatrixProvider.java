package org.schakalacka.java.raytracing.math;

public interface IMatrixProvider {
    Matrix get(float[][] ref);

    Matrix get(int size);

    Matrix get(int size, boolean isIdentity);

    Matrix translation(float x, float y, float z);

    Matrix scaling(float x, float y, float z);

    Matrix rotationX(float radians);

    Matrix rotationY(float radians);

    Matrix rotationZ(float radians);

    Matrix shearing(float xy, float xz, float yx, float yz, float zx, float zy);
}
