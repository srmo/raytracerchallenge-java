package org.schakalacka.java.raytracing.geometry.patterns;

import org.schakalacka.java.raytracing.math.Matrix;
import org.schakalacka.java.raytracing.math.MatrixProvider;
import org.schakalacka.java.raytracing.math.Tuple;
import org.schakalacka.java.raytracing.geometry.objects.Shape;
import org.schakalacka.java.raytracing.scene.Color;

public abstract class Pattern {

    private Matrix transformationMatrix = MatrixProvider.get(4,true);

    public abstract Color patternAt(Tuple point);

    public final Color patternAtShape(Shape shape, Tuple worldPoint) {
        Tuple objectPoint = shape.worldToObject(worldPoint);
        Tuple patternPoint = this.getTransformationMatrix().inverse().mulT(objectPoint);
        return patternAt(patternPoint);
    }

    public Matrix getTransformationMatrix() {
        return transformationMatrix;
    }

    public void setTransformationMatrix(Matrix transformationMatrix) {
        this.transformationMatrix = transformationMatrix;
    }

}