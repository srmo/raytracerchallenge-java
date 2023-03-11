package org.schakalacka.java.raytracing.scene;

import org.schakalacka.java.raytracing.math.RTPoint;

public record PointLight(RTPoint position, Color intensity) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PointLight that = (PointLight) o;
        return position.equals(that.position) && intensity.equals(that.intensity);
    }

}
