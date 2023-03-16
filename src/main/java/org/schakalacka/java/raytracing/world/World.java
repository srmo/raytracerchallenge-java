package org.schakalacka.java.raytracing.world;


import org.schakalacka.java.raytracing.Constants;
import org.schakalacka.java.raytracing.geometry.objects.Shape;
import org.schakalacka.java.raytracing.geometry.objects.Sphere;
import org.schakalacka.java.raytracing.geometry.tracing.*;
import org.schakalacka.java.raytracing.math.MatrixProvider;
import org.schakalacka.java.raytracing.math.Tuple;
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
        sphere2.setTransformationMatrix(MatrixProvider.scaling(0.5f, 0.5f, 0.5f));

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
        return objects.stream().map(worldObject -> worldObject.intersect(ray)).flatMap(List::stream).sorted(Comparator.comparingDouble(Intersection::getDistance)).collect(Collectors.toList());

    }

    public Color color_at(Ray ray) {
        return this.color_at(ray, Constants.DEFAULT_REFLECTION_DEPTH);
    }

    public Color color_at(Ray ray, int remainingBounces) {
        Intersection hit = IntersectionTracker.getHit(intersect(ray));
        if (hit == null) {
            return Color.BLACK;
        } else {
            return shade_hit(new Precalc(hit, ray), remainingBounces);
        }
    }

    public Color shade_hit(Precalc precalc) {
        return shade_hit(precalc, Constants.DEFAULT_REFLECTION_DEPTH);
    }

    public Color shade_hit(Precalc precalc, int remainingBounces) {

        boolean shadowed = getShadowResult(precalc.getOverPoint()).isShadowed();

        // TODO add iteration over multiple light sources here
        Color surfaceColor = precalc.getObject().material().lighting(
                this.lightSource,
                precalc.getObject(),
                precalc.getOverPoint(),
                precalc.getEyeVector(),
                precalc.getNormalVector(),
                shadowed
        );


        Color reflectedColor = reflectedColor(precalc, remainingBounces);
        Color refractedColor = refractedColor(precalc, remainingBounces);

        boolean needsReflectance = precalc.getObject().material().reflectivity() > 0 && precalc.getObject().material().transparency() > 0;
        if (needsReflectance) {
            var reflectance = precalc.schlick();
            return surfaceColor.add(reflectedColor.mulS(reflectance)).add(refractedColor.mulS(1 - reflectance));
        } else {
            return surfaceColor.add(reflectedColor).add(refractedColor);
        }

    }

    public void addObjects(Shape... objects) {
        this.objects.addAll(Arrays.asList(objects));
    }

    public ShadowResult getShadowResult(Tuple point) {

        var vectorPointToLight = lightSource.position().sub(point);
        var distancePointToLight = vectorPointToLight.magnitude();
        var directionPointToLight = vectorPointToLight.normalize();

        Ray r = new Ray(point, directionPointToLight);

        // TODO find an optimisation here for intersections. Is there any?
        List<Intersection> intersections = intersect(r);
        Intersection hit = IntersectionTracker.getHit(intersections);

        boolean isShadowed = hit != null && hit.getDistance() < distancePointToLight && hit.getIntersectedObject().material().createsShadow();
        return new ShadowResult(isShadowed, hit);
    }

    public Color reflectedColor(Precalc precalc, int remainingBounces) {
        if (remainingBounces < 1 || precalc.getObject().material().reflectivity() == 0) {
            return Color.BLACK;
        } else {
            // important to start reflection at the overPoint, not at the intersection point, otherwise we get a self-intersection
            Ray reflectedRay = new Ray(precalc.getOverPoint(), precalc.getReflectVector());

            // we consumed one bounce, so we need to subtract one from the remaining bounces
            Color reflectedColor = color_at(reflectedRay, remainingBounces - 1);

            return reflectedColor.mulS(precalc.getObject().material().reflectivity());
        }
    }

    public Color refractedColor(Precalc precalc, int remainingBounces) {
        if (remainingBounces < 1 || precalc.getObject().material().transparency() == 0) {
            return Color.BLACK;
        } else {
            var nRatio = precalc.getN1() / precalc.getN2();
            var cosI = precalc.getEyeVector().dot(precalc.getNormalVector());
            var sin2T = nRatio * nRatio * (1 - cosI * cosI);

            // exit early if we have total internal reflection
            if (sin2T > 1) return Color.BLACK;

            var cosT = Math.sqrt(1.0 - sin2T);
            var direction = precalc.getNormalVector().mul((float) (nRatio * cosI - cosT)).sub(precalc.getEyeVector().mul((float) nRatio));
            var refractedRay = new Ray(precalc.getUnderPoint(), direction);

            return color_at(refractedRay, remainingBounces - 1).mulS(precalc.getObject().material().transparency());
        }
    }
}
