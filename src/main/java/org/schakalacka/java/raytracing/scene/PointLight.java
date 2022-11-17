package org.schakalacka.java.raytracing.scene;

import org.schakalacka.java.raytracing.geometry.Tuple;

public class PointLight {

    private Tuple position;
    private Color intensity;

    public PointLight(Tuple position, Color intensity) {

        this.position = position;
        this.intensity = intensity;
    }

    public Color intensity() {
        return intensity;
    }

    public Tuple position() {
        return position;
    }
}
