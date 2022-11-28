package org.schakalacka.java.raytracing.world;

import org.schakalacka.java.raytracing.geometry.algebra.MatrixProvider;
import org.schakalacka.java.raytracing.geometry.algebra.Tuple;
import org.schakalacka.java.raytracing.geometry.objects.Shape;
import org.schakalacka.java.raytracing.geometry.objects.Sphere;
import org.schakalacka.java.raytracing.geometry.tracing.Intersection;
import org.schakalacka.java.raytracing.geometry.tracing.IntersectionTracker;
import org.schakalacka.java.raytracing.geometry.tracing.Precalc;
import org.schakalacka.java.raytracing.geometry.tracing.Ray;
import org.schakalacka.java.raytracing.scene.Color;
import org.schakalacka.java.raytracing.scene.Material;
import org.schakalacka.java.raytracing.scene.PointLight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class World {


    private final List<Shape> objects = new ArrayList<>();
    private PointLight lightSource;


    public World() {
        this(null);
    }

    private World(PointLight lightSource, Shape... objects) {
        this.lightSource = lightSource;
        this.objects.addAll(Arrays.stream(objects).toList());
    }

    public static World getDefault() {
        PointLight lightSource = new PointLight(Tuple.point(-10, 10, -10), new Color(1, 1, 1));
        Sphere sphere1 = new Sphere();
        sphere1.setMaterial(Material.newMaterial().color(new Color(0.8, 1, 0.6)).diffuse(0.7).specular(0.2).create());

        Sphere sphere2 = new Sphere();
        sphere2.setTransformationMatrix(MatrixProvider.scaling(0.5, 0.5, 0.5));

        return new World(lightSource, sphere1, sphere2);
    }

    public PointLight getLightSource() {
        return lightSource;
    }

    public void setLightSource(PointLight lightSource) {
        this.lightSource = lightSource;
    }

    public List<Shape> getObjects() {
        return objects;
    }

    public List<Intersection> intersect(Ray ray) {
        return objects.stream().map(go -> go.intersect(ray)).flatMap(List::stream).sorted(Comparator.comparingDouble(Intersection::getDistance)).collect(Collectors.toList());

    }

    public Color color_at(Ray ray) {
        Intersection hit = IntersectionTracker.getHit(intersect(ray));
        if (hit == null) {
            return Color.BLACK;
        } else {
            return shade_hit(new Precalc(hit, ray));
        }
    }

    public Color shade_hit(Precalc precalc) {
        boolean shadowed = isShadowed(precalc.getOverPoint());
        // TODO add iteration over multiple light sources here
        return precalc.getObject().material().lighting(
                this.lightSource,
                precalc.getObject(),
                precalc.getOverPoint(),
                precalc.getEyeVector(),
                precalc.getNormalVector(),
                shadowed
        );
    }

    public void addObjects(Shape... objects) {
        this.objects.addAll(Arrays.asList(objects));
    }

    public boolean isShadowed(Tuple point) {
        var vectorPointToLight = lightSource.position().sub(point);
        var distancePointToLight = vectorPointToLight.magnitude();
        var directionPointToLight = vectorPointToLight.normalize();

        Ray r = new Ray(point, directionPointToLight);

        // TODO find an optimisation here for intersections. Is there any?
        List<Intersection> intersections = intersect(r);
        Intersection hit = IntersectionTracker.getHit(intersections);

        // we have a hit when it occurs on the way from the point to the light-source.
        return hit != null && hit.getDistance() < distancePointToLight;
    }

}
