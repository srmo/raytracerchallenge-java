package org.schakalacka.java.raytracing.math;

import org.ejml.data.FMatrixRMaj;
import org.ejml.simple.SimpleMatrix;

class EjmlMatrixProvider implements IMatrixProvider {


    public EjmlMatrix get(float[][] ref) {
        FMatrixRMaj matrix = new FMatrixRMaj(ref);
        return new EjmlMatrix(matrix);
    }

    public EjmlMatrix get(int size) {
        return this.get(size, false);
    }

    public EjmlMatrix get(int size, boolean isIdentity) {
        final SimpleMatrix simpleMatrix;

        if (isIdentity) {
            simpleMatrix = SimpleMatrix.identity(size);
        } else {
            simpleMatrix = new SimpleMatrix(size, size);
        }

        return new EjmlMatrix(simpleMatrix.getFDRM());
    }

    /***
     * create a translation matrix. It is a 4x4 identity matrix, where the last colum is populated with the 3 values.
     */
    public EjmlMatrix translation(float x, float y, float z) {
        SimpleMatrix simpleMatrix = SimpleMatrix.identity(4);
        simpleMatrix.setColumn(3, 0, x, y, z);
        return new EjmlMatrix(simpleMatrix.getFDRM());
    }

    public EjmlMatrix scaling(float x, float y, float z) {
        SimpleMatrix simpleMatrix = new SimpleMatrix(new double[][]{
                {x, 0, 0, 0},
                {0, y, 0, 0},
                {0, 0, z, 0},
                {0, 0, 0, 1}
        });

        return new EjmlMatrix(simpleMatrix.getFDRM());
    }

    /***
     *
     * @return a left-handed rotation matrix along the X axis
     */
    public EjmlMatrix rotationX(float radians) {
        SimpleMatrix simpleMatrix = new SimpleMatrix(new double[][]{
                {1, 0, 0, 0},
                {0, Math.cos(radians), -Math.sin(radians), 0},
                {0, Math.sin(radians), Math.cos(radians), 0},
                {0, 0, 0, 1},
        });

        return new EjmlMatrix(simpleMatrix.getFDRM());
    }

    /***
     *
     * @return a left-handed rotation matrix along the Y axis
     */
    public EjmlMatrix rotationY(float radians) {
        SimpleMatrix simpleMatrix = new SimpleMatrix(new double[][]{
                {Math.cos(radians), 0, Math.sin(radians), 0},
                {0, 1, 0, 0},
                {-Math.sin(radians), 0, Math.cos(radians), 0},
                {0, 0, 0, 1},
        });

        return new EjmlMatrix(simpleMatrix.getFDRM());
    }

    public EjmlMatrix rotationZ(float radians) {
        SimpleMatrix simpleMatrix = new SimpleMatrix(new double[][]{
                {Math.cos(radians), -Math.sin(radians), 0, 0},
                {Math.sin(radians), Math.cos(radians), 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1},
        });

        return new EjmlMatrix(simpleMatrix.getFDRM());
    }

    public EjmlMatrix shearing(float xy, float xz, float yx, float yz, float zx, float zy) {
        SimpleMatrix simpleMatrix = new SimpleMatrix(new double[][]{
                {1, xy, xz, 0},
                {yx, 1, yz, 0},
                {zx, zy, 1, 0},
                {0, 0, 0, 1},
        });

        return new EjmlMatrix(simpleMatrix.getFDRM());

    }

}
