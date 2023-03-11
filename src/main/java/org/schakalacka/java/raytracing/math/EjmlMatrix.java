package org.schakalacka.java.raytracing.math;

import org.ejml.data.FMatrixRMaj;
import org.ejml.dense.row.CommonOps_FDRM;
import org.ejml.dense.row.mult.MatrixVectorMult_FDRM;
import org.schakalacka.java.raytracing.Constants;
import org.schakalacka.java.raytracing.Counter;

import java.util.Arrays;

public class EjmlMatrix implements Matrix {

    final FMatrixRMaj wrappedMatrix;

    EjmlMatrix(FMatrixRMaj wrappedMatrix) {
        this.wrappedMatrix = wrappedMatrix;
    }

    @Override
    public float get(int row, int col) {
        return wrappedMatrix.get(row, col);
    }

    @Override
    public void set(int row, int col, float val) {
        wrappedMatrix.set(row, col, val);
    }

    @Override
    public EjmlMatrix mulM(Matrix that) {
        Counter.mulM++;

        if (that instanceof EjmlMatrix) {
            FMatrixRMaj result = new FMatrixRMaj();
            result = CommonOps_FDRM.mult(this.wrappedMatrix, ((EjmlMatrix) that).wrappedMatrix, result);
            return new EjmlMatrix(result);
        } else {
            throw new IllegalArgumentException("Can only multiply with other EjmlMatrix");
        }
    }

    @Override
    public Tuple mulT(Tuple vector) {
        Counter.mulT++;
        FMatrixRMaj resultVector = new FMatrixRMaj(this.wrappedMatrix.numCols);

        // ejml requires the vector and matrix to have the same size, i.e. if the vector has length N, the matrix also needs to be of size NxN
        // so let's rework this here. Consider only cases of 4x4, 3x3 matrices our vectors are always length 4
        final float[] vectorArray;
        if (this.wrappedMatrix.getNumCols() == 3) {
            vectorArray = Arrays.copyOfRange(vector.getArray(), 0, 3);
        } else {
            vectorArray = vector.getArray();
        }

        MatrixVectorMult_FDRM.mult(wrappedMatrix, new FMatrixRMaj(vectorArray), resultVector);

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
    public EjmlMatrix transpose() {
        Counter.transpose++;
        FMatrixRMaj transpose = CommonOps_FDRM.transpose(this.wrappedMatrix, null);
        return new EjmlMatrix(transpose);
    }

    @Override
    public float determinant() {
        Counter.determinant++; return CommonOps_FDRM.det(this.wrappedMatrix);
    }

    @Override
    public EjmlMatrix subM(int r, int c) {
        Counter.subM++;
        FMatrixRMaj extract = CommonOps_FDRM.extract(this.wrappedMatrix, 0, c, 0, r);
        return new EjmlMatrix(extract);
    }

    @Override
    public float minor(int r, int c) {
        Counter.minor++;
        throw new UnsupportedOperationException("EjmlMatrix doesn't directly support minor calculation");
    }

    @Override
    public float cofactor(int r, int c) {
        Counter.cofactor++;
        throw new UnsupportedOperationException("EjmlMatrix doesn't directly support cofactor calculation");
    }

    @Override
    public boolean isInvertible() {
        return determinant() != 0;
    }

    @Override
    public EjmlMatrix inverse() {
        Counter.inverse++;
        if (!isInvertible()) {
            throw new ArithmeticException("Matrix not invertible");
        }

        FMatrixRMaj result = new FMatrixRMaj(this.wrappedMatrix.numCols, this.wrappedMatrix.numCols);
        CommonOps_FDRM.invert(this.wrappedMatrix, result);
        return new EjmlMatrix(result);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EjmlMatrix that = (EjmlMatrix) o;

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
        return "EjmlMatrix{" +
                "wrappedMatrix=" + wrappedMatrix +
                '}';
    }
}
