package org.schakalacka.java.raytracing.world;

import org.schakalacka.java.raytracing.geometry.Matrix;
import org.schakalacka.java.raytracing.geometry.Tuple;

public class ViewTransformation {
    public static Matrix transform(Tuple from, Tuple to, Tuple up) {
        validate(from, to, up);


        var forwardVector = to.sub(from).normalize();
        var leftVector = forwardVector.cross(up.normalize());

        var trueUpVector = leftVector.cross(forwardVector);

        var orientationMatrix = Matrix.get(new double[][]{
                {leftVector.x(), leftVector.y(), leftVector.z(), 0},
                {trueUpVector.x(), trueUpVector.y(), trueUpVector.z(), 0},
                {-forwardVector.x(), -forwardVector.y(), -forwardVector.z(), 0},
                {0, 0, 0, 1},
        });

        var translationMatrix = Matrix.translation(-from.x(), -from.y(), -from.z());

        return orientationMatrix.mulM(translationMatrix);
    }

    private static void validate(Tuple from, Tuple to, Tuple up) {
        if (!from.isPoint()) {
            throw new IllegalArgumentException("from needs to be a point");
        }

        if (!to.isPoint()) {
            throw new IllegalArgumentException("to needs to be a point");
        }

        if (!up.isVector()) {
            throw new IllegalArgumentException("up needs to be a vector");
        }
    }

}
