package org.schakalacka.java.raytracing.geometry.objects;

import org.schakalacka.java.raytracing.geometry.Matrix;
import org.schakalacka.java.raytracing.geometry.Tuple;
import org.schakalacka.java.raytracing.geometry.tracing.Intersection;
import org.schakalacka.java.raytracing.geometry.tracing.Ray;
import org.schakalacka.java.raytracing.scene.Material;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Sphere implements GeometryObject {

    private final Tuple position = Tuple.point(0, 0, 0);
    private Matrix transformationMatrix = Matrix.get(4, true);
    private Material material = Material.newMaterial().create();


    public Tuple position() {
        return position;
    }

    public double radius() {
        return 1;
    }


    @Override
    public Tuple normal(Tuple point) {
        // convert worldpoint to objectpoint
        var objectPoint = transformationMatrix.inverse().mulT(point);

        // TODO do we need normalisation here? We'll do it on the result again
        Tuple objectNormal = objectPoint.sub(position).normalize();

        // we come from world-space, we need to leave as world space. Convert the new normal into world-space
        Matrix subMatrix3 = transformationMatrix.subM(3, 3);

        return subMatrix3.inverse().transpose().mulT(objectNormal).normalize();
    }

    @Override
    public List<Intersection> intersect(Ray ray) {
        // yes, magical. This seems to be about World Space vs Object Space conversion? Maybe not?
        // at least I understand the following analogy:
        //      transform sphere relative to ray ==== inverse-transform ray relative to sphere
        ray = ray.transform(transformationMatrix.inverse());

        List<Intersection> result = new ArrayList<>();

        var vectorSphereToRay = ray.origin().sub(Tuple.point(0, 0, 0));
        var a = ray.direction().dot(ray.direction());
        var b = 2 * ray.direction().dot(vectorSphereToRay);
        var c = vectorSphereToRay.dot(vectorSphereToRay) - 1;

        var discriminant = b * b - 4 * a * c;

        if (discriminant >= 0) {
            result.add(new Intersection(this, (-b - Math.sqrt(discriminant)) / (2 * a)));
            result.add(new Intersection(this, (-b + Math.sqrt(discriminant)) / (2 * a)));
            result.sort(Comparator.comparingDouble(Intersection::getDistance));
        }

        return result;
    }

    public Matrix getTransformationMatrix() {
        return transformationMatrix;
    }

    public void setTransformationMatrix(Matrix matrix) {
        this.transformationMatrix = matrix;
    }

    @Override
    public void setMaterial(Material material) {
        this.material = material;
    }

    @Override
    public Material material() {
        return this.material;
    }
}
