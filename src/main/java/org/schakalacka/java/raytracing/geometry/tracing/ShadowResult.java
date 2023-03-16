package org.schakalacka.java.raytracing.geometry.tracing;

import java.util.Objects;

public final class ShadowResult {
    private final boolean isShadowed;
    private final Intersection distance;

    public ShadowResult(boolean isShadowed, Intersection distance) {
        this.isShadowed = isShadowed;
        this.distance = distance;
    }

    public boolean isShadowed() {
        return isShadowed;
    }

    public Intersection distance() {
        return distance;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ShadowResult) obj;
        return this.isShadowed == that.isShadowed &&
                Objects.equals(this.distance, that.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isShadowed, distance);
    }

    @Override
    public String toString() {
        return "ShadowResult[" +
                "isShadowed=" + isShadowed + ", " +
                "distance=" + distance + ']';
    }

}
