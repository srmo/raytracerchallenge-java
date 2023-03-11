package org.schakalacka.java.raytracing.geometry.objects;

import org.schakalacka.java.raytracing.math.Matrix;
import org.schakalacka.java.raytracing.math.MatrixProvider;
import org.schakalacka.java.raytracing.math.Tuple;
import org.schakalacka.java.raytracing.geometry.tracing.Intersection;
import org.schakalacka.java.raytracing.geometry.tracing.Ray;
import org.schakalacka.java.raytracing.scene.Material;

import java.util.List;

public abstract class Shape {

    protected Matrix transformationMatrix = MatrixProvider.get(4, true);
    protected Matrix subMatrix3 = transformationMatrix.subM(3, 3);
    protected Material material = Material.newMaterial().create();

    public abstract List<Intersection> localIntersect(Ray ray);

    public List<Intersection> intersect(Ray ray) {
        return this.localIntersect(ray.transform(transformationMatrix.inverse()));
    }

    public abstract Tuple localNormalVectorAt(Tuple point);

    public Tuple normalVectorAt(Tuple point) {
        // convert world-point to object-point
        var objectPoint = transformationMatrix.inverse().mulT(point);

        Tuple objectNormal = localNormalVectorAt(objectPoint);
        Tuple worldNormal = subMatrix3.inverse().transpose().mulT(objectNormal);

        return worldNormal.normalize();

    }

    public Matrix getTransformationMatrix() {
        return transformationMatrix;
    }

    public void setTransformationMatrix(Matrix matrix) {
        this.transformationMatrix = matrix;
        this.subMatrix3 = transformationMatrix.subM(3, 3);
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Material material() {
        return this.material;
    }
}
