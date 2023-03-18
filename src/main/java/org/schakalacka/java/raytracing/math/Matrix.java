package org.schakalacka.java.raytracing.math;

public interface Matrix {

    double get(int row, int col);

    void set(int i, int i1, double xy);

    Matrix mulM(Matrix that);

    Tuple mulT(Tuple that);

    Matrix transpose();

    double determinant();

    Matrix subM(int r, int c);

    double minor(int r, int c);

    double cofactor(int r, int c);

    boolean isInvertible();

    Matrix inverse();

}
