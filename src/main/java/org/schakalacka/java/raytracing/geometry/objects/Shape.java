package org.schakalacka.java.raytracing.geometry.objects;

import org.schakalacka.java.raytracing.geometry.tracing.Intersection;
import org.schakalacka.java.raytracing.geometry.tracing.Ray;
import org.schakalacka.java.raytracing.math.Matrix;
import org.schakalacka.java.raytracing.math.MatrixProvider;
import org.schakalacka.java.raytracing.math.Tuple;
import org.schakalacka.java.raytracing.scene.Material;

import java.util.List;

public abstract class Shape {

    protected Matrix transformationMatrix = MatrixProvider.get(4, true);
    protected Matrix subMatrix3 = transformationMatrix.subM(3, 3);
    protected Material material = Material.newMaterial().create();
    private Group parent = null;

    public abstract Bounds getBounds();

    public abstract List<Intersection> localIntersect(Ray ray);

    public List<Intersection> intersect(Ray ray) {
        return this.localIntersect(ray.transform(transformationMatrix.inverse()));
    }

    public abstract Tuple localNormalVectorAt(Tuple point);

    public Tuple normalVectorAt(Tuple point) {
        // convert world-point to object-point
        var localPoint = worldToObject(point);
        var localNormal = localNormalVectorAt(localPoint);
        return normalToWorld(localNormal);
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

    public Group getParent() {
        return parent;
    }

    protected void setParent(Group group) {
        this.parent = group;
    }

    public Tuple worldToObject(Tuple point) {
        if (parent != null) {
            point = parent.worldToObject(point);
        }
        return this.transformationMatrix.inverse().mulT(point);
    }

    public Tuple normalToWorld(Tuple vector) {
        var normal = this.subMatrix3.inverse().transpose().mulT(vector);
        assert normal.w() == 0;
        normal = normal.normalize();
        if (parent != null) {
            normal = parent.normalToWorld(normal);
        }

        return normal;
    }
}
