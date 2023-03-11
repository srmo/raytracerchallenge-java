package org.schakalacka.java.raytracing.math;

public interface Matrix {

    float get(int row, int col);

    void set(int i, int i1, float xy);

    Matrix mulM(Matrix that);

    Tuple mulT(Tuple that);

    Matrix transpose();

    float determinant();

    Matrix subM(int r, int c);

    float minor(int r, int c);

    float cofactor(int r, int c);

    boolean isInvertible();

    Matrix inverse();

}
