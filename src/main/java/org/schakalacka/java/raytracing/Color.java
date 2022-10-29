package org.schakalacka.java.raytracing;

import java.util.Objects;

public class Color {

    public static final double EPSILON = 0.0000000000000001;

    private final double r;
    private final double g;
    private final double b;

    public Color(double r, double g, double b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }


    public double r() {
        return r;
    }

    public double g() {
        return g;
    }

    public double b() {
        return b;
    }


    @Override
    public String toString() {
        return "{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                '}';
    }

    public Color add(Color that) {
        return new Color(this.r + that.r, this.g + that.g, this.b + that.b);
    }

    public Color sub(Color that) {
        return new Color(this.r - that.r, this.g - that.g, this.b - that.b);
    }

    public Color mulS(double scalar) {
        return new Color(this.r * scalar, this.g * scalar, this.b * scalar);
    }

    public Color mulC(Color that) {
        return new Color(this.r * that.r, this.g * that.g, this.b * that.b);
    }


    @Override
    public int hashCode() {
        return Objects.hash(r, g, b);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Color) {
            return this.r - ((Color) obj).r < EPSILON &&
                    this.g - ((Color) obj).g < EPSILON &&
                    this.b - ((Color) obj).b < EPSILON;
        }

        return false;

    }
}


