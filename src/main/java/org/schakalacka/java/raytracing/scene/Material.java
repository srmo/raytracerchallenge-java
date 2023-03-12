package org.schakalacka.java.raytracing.scene;

import org.schakalacka.java.raytracing.math.Tuple;
import org.schakalacka.java.raytracing.geometry.objects.Shape;
import org.schakalacka.java.raytracing.geometry.patterns.Pattern;

public record Material(Color color, double ambient, double diffuse, double specular, double shininess, float reflectivity,
                       Pattern pattern) {

    public static MaterialBuilder newMaterial() {
        return new MaterialBuilder();
    }

    public Color lighting(PointLight light, Tuple eyePosition, Tuple eyeVector, Tuple normalVector) {
        return this.lighting(light, null, eyePosition, eyeVector, normalVector, false);
    }

    public Color lighting(PointLight light, Shape shape, Tuple eyePosition, Tuple eyeVector, Tuple normalVector, boolean isInShadow) {
        Color ambientResult;
        Color diffuseResult;
        Color specularResult;

        Color actualBaseColor = this.color;
        // when we have a pattern, use the patterns color and if we have a shape, make sure to get the pattern on the shape
        if (this.pattern != null) {
            actualBaseColor = this.pattern.patternAtShape(shape, eyePosition);
        }

        var effectiveColor = actualBaseColor.mulC(light.intensity());

        // calculate the direction from eye to light
        var lightVector = light.position().sub(eyePosition).normalize();

        ambientResult = effectiveColor.mulS(this.ambient);
        if (isInShadow) {
            return ambientResult;
        }

        // lightDotNormal: the cosine of the angle between light vector and normal vector
        // a negative value means the light is on the other side of surface!
        // So there's no diffusion or specular highlight for the given light-source
        var lightDotNormal = lightVector.dot(normalVector);
        if (lightDotNormal < 0) {
            diffuseResult = Color.BLACK;
            specularResult = Color.BLACK;
        } else {
            diffuseResult = effectiveColor.mulS(this.diffuse).mulS(lightDotNormal);

            // reflectDotEye represents the cosine of the angle between the reflection vector and the eye vector.
            // A negative number means the light reflects away from the eye
            // So there's no specular highlight for this point
            var reflectVector = lightVector.negate().reflect(normalVector);
            var reflectDotEye = reflectVector.dot(eyeVector);
            if (reflectDotEye <= 0) {
                specularResult = Color.BLACK;
            } else {
                // now we can compute the specular contribution
                var factor = Math.pow(reflectDotEye, this.shininess);
                specularResult = light.intensity().mulS(this.specular).mulS(factor);
            }
        }

        return ambientResult.add(diffuseResult).add(specularResult);
    }


    public static class MaterialBuilder {
        private Color color = new Color(1, 1, 1);
        private double ambient = 0.1;
        private double diffuse = 0.9;
        private double specular = 0.9;
        private double shininess = 200.0;
        private float reflectivity = 0.0f;
        private Pattern pattern;

        public MaterialBuilder color(Color color) {
            this.color = color;
            return this;
        }

        public MaterialBuilder ambient(double ambient) {
            this.ambient = ambient;
            return this;
        }

        public MaterialBuilder diffuse(double diffuse) {
            this.diffuse = diffuse;
            return this;
        }

        public MaterialBuilder specular(double specular) {
            this.specular = specular;
            return this;
        }

        public MaterialBuilder shininess(double shininess) {
            this.shininess = shininess;
            return this;
        }

        public MaterialBuilder reflectivity(float reflectivity) {
            this.reflectivity = reflectivity;
            return this;
        }

        public Material create() {
            return new Material(color, ambient, diffuse, specular, shininess, reflectivity, pattern);
        }

        public MaterialBuilder pattern(Pattern pattern) {
            this.pattern = pattern;
            return this;
        }
    }

}
