package org.schakalacka.java.raytracing.geometry.objects;

import org.schakalacka.java.raytracing.math.RTPoint;

import java.util.Objects;

public class Bounds {


    private final RTPoint lower, upper;

    public Bounds(RTPoint lower, RTPoint upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public RTPoint getUpper() {
        return upper;
    }

    public RTPoint getLower() {
        return lower;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bounds bounds = (Bounds) o;
        return lower.equals(bounds.lower) && upper.equals(bounds.upper);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lower, upper);
    }

    @Override
    public String toString() {
        return "Bounds{" +
                "lower=" + lower +
                ", upper=" + upper +
                '}';
    }
}
