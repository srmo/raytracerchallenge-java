package org.schakalacka.java.raytracing.geometry.objects;

import org.schakalacka.java.raytracing.geometry.Tuple;
import org.schakalacka.java.raytracing.geometry.tracing.Intersection;
import org.schakalacka.java.raytracing.geometry.tracing.Ray;
import org.schakalacka.java.raytracing.scene.Material;

import java.util.List;

public interface GeometryObject {

    List<Intersection> intersect(Ray ray);

    Tuple normalVectorAt(Tuple point);

    void setMaterial(Material material);

    Material material();
}
