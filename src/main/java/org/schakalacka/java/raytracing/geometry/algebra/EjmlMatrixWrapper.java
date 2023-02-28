package org.schakalacka.java.raytracing.geometry.algebra;

import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.dense.row.mult.MatrixVectorMult_DDRM;
import org.schakalacka.java.raytracing.Constants;
import org.schakalacka.java.raytracing.Counter;

import java.util.Arrays;

public class EjmlMatrixWrapper implements Matrix {

    private final DMatrixRMaj wrappedMatrix;

    EjmlMatrixWrapper(DMatrixRMaj wrappedMatrix) {
        this.wrappedMatrix = wrappedMatrix;
    }

    @Override
    public double get(int row, int col) {
        return wrappedMatrix.get(row, col);
    }

    @Override
    public void set(int row, int col, double val) {
        wrappedMatrix.set(row, col, val);
    }

    @Override
    public Matrix mulM(Matrix that) {
        Counter.mulM++;

        DMatrixRMaj result = new DMatrixRMaj();
        DMatrixRMaj mult = CommonOps_DDRM.mult(this.wrappedMatrix, ((EjmlMatrixWrapper) that).wrappedMatrix, result);
        return new EjmlMatrixWrapper(mult);
    }

    @Override
    public Tuple mulT(Tuple vector) {
        Counter.mulT++;
        DMatrixRMaj resultVector = new DMatrixRMaj(this.wrappedMatrix.numCols);

        // ejml requires the vector and matrix to have the same size, i.e. if the vector has length N, the matrix also needs to be of size NxN
        // so let's rework this here. Consider only cases of 4x4, 3x3 matrices our vectors are always length 4
        final double[] vectorArray;
        if (this.wrappedMatrix.getNumCols() == 3) {
            vectorArray = Arrays.copyOfRange(vector.getArray(), 0, 3);
        } else {
            vectorArray = vector.getArray();
        }

        MatrixVectorMult_DDRM.mult(wrappedMatrix, new DMatrixRMaj(vectorArray), resultVector);

        // also, if the matrix is 3x3, the resulting vector from ejml only has 3 elements.
        // again, our Tuples have 4 values, so adjust for that here. we need to keep the original w value of the vector.
        // sadly, I'm using DMatrixRowMaj as Vector representation here, so it is row based and that's why we query "numRows"
        // I couldn't figure out what the right way is to have some kind of "plain" vector
        if (resultVector.numRows == 3) {
            return Tuple.tuple(resultVector.get(0), resultVector.get(1), resultVector.get(2), vector.w());
        } else {
            return Tuple.tuple(resultVector.get(0), resultVector.get(1), resultVector.get(2), resultVector.get(3));
        }
    }

    @Override
    public Matrix transpose() {
        Counter.transpose++;
        DMatrixRMaj transpose = CommonOps_DDRM.transpose(this.wrappedMatrix, null);
        return new EjmlMatrixWrapper(transpose);
    }

    @Override
    public double determinant() {
        Counter.determinant++; return CommonOps_DDRM.det(this.wrappedMatrix);
    }

    @Override
    public Matrix subM(int r, int c) {
        Counter.subM++;
        DMatrixRMaj extract = CommonOps_DDRM.extract(this.wrappedMatrix, 0, c, 0, r);
        return new EjmlMatrixWrapper(extract);
    }

    @Override
    public double minor(int r, int c) {
        Counter.minor++;
        throw new UnsupportedOperationException("EjmlMatrixWrapper doesn't directly support minor calculation");
    }

    @Override
    public double cofactor(int r, int c) {
        Counter.cofactor++;
        throw new UnsupportedOperationException("EjmlMatrixWrapper doesn't directly support cofactor calculation");
    }

    @Override
    public boolean isInvertible() {
        return determinant() != 0;
    }

    @Override
    public Matrix inverse() {
        Counter.inverse++;
        if (!isInvertible()) {
            throw new ArithmeticException("Matrix not invertible");
        }

        DMatrixRMaj result = new DMatrixRMaj(this.wrappedMatrix.numCols, this.wrappedMatrix.numCols);
        CommonOps_DDRM.invert(this.wrappedMatrix, result);
        return new EjmlMatrixWrapper(result);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EjmlMatrixWrapper that = (EjmlMatrixWrapper) o;

        int length = this.wrappedMatrix.getNumElements();
        if (that.wrappedMatrix.getNumElements() != that.wrappedMatrix.getNumElements())
            return false;

        for (int i = 0; i < length; i++) {
            double e1 = this.wrappedMatrix.get(i);
            double e2 = that.wrappedMatrix.get(i);

            if (e1 == e2)
                continue;

            if (Math.abs(e1 - e2) >= Constants.EQUALS_EPSILON)
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(wrappedMatrix.data);
    }

    @Override
    public String toString() {
        return "EjmlMatrixWrapper{" +
                "wrappedMatrix=" + wrappedMatrix +
                '}';
    }
}
