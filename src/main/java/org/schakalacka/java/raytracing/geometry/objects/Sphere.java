package org.schakalacka.java.raytracing.geometry.objects;

import org.schakalacka.java.raytracing.geometry.algebra.Matrix;
import org.schakalacka.java.raytracing.geometry.algebra.MatrixProvider;
import org.schakalacka.java.raytracing.geometry.algebra.Tuple;
import org.schakalacka.java.raytracing.geometry.tracing.Intersection;
import org.schakalacka.java.raytracing.geometry.tracing.Ray;
import org.schakalacka.java.raytracing.scene.Material;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Sphere implements GeometryObject {

    private final Tuple position = Tuple.point(0, 0, 0);
    private Matrix transformationMatrix = MatrixProvider.get(4, true);
    private Matrix subMatrix3 = transformationMatrix.subM(3, 3);
    private Material material = Material.newMaterial().create();


    public Tuple position() {
        return position;
    }

    public double radius() {
        return 1;
    }


    @Override
    public Tuple normalVectorAt(Tuple point) {
        // convert world-point to object-point
        var objectPoint = transformationMatrix.inverse().mulT(point);

        Tuple objectNormal = objectPoint.sub(position);
        Tuple worldNormal = subMatrix3.inverse().transpose().mulT(objectNormal);

        return worldNormal.normalize();

//        return point.sub(position).normalize();
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
        this.subMatrix3 = transformationMatrix.subM(3, 3);
    }

    @Override
    public void setMaterial(Material material) {
        this.material = material;
    }

    @Override
    public Material material() {
        return this.material;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        org.schakalacka.java.raytracing.geometry.objects.Sphere sphere = (org.schakalacka.java.raytracing.geometry.objects.Sphere) o;
        return Objects.equals(position, sphere.position) && Objects.equals(transformationMatrix, sphere.transformationMatrix) && Objects.equals(material, sphere.material);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, transformationMatrix, material);
    }
}
