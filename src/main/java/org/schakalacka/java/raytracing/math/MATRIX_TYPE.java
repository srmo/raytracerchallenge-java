package org.schakalacka.java.raytracing.math;

public enum MATRIX_TYPE {
    NAIVE(new NaiveMatrixProvider()),
    EJML(new EjmlMatrixProvider()),
    CUBLAS(new CublasMatrixProvider());

    final IMatrixProvider provider;

    MATRIX_TYPE(IMatrixProvider matrixProvider) {
        this.provider = matrixProvider;
    }
}
