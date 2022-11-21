package org.schakalacka.java.raytracing.scene;

import static org.schakalacka.java.raytracing.Constants.EPSILON;

public record Color(double r, double g, double b) {

    public static final Color BLACK = new Color(0, 0, 0);
    public static final Color WHITE = new Color(1, 1, 1);

    public static int scaleValue(double value, int scale) {
        if (value < 0) {
            return 0;
        }

        if (value > 1) {
            return scale;
        }

        return (int) Math.ceil(value * scale);
    }

    public int rs(int scale) {
        return Color.scaleValue(r, scale);
    }

    public int gs(int scale) {
        return Color.scaleValue(g, scale);
    }

    public int bs(int scale) {
        return Color.scaleValue(b, scale);
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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Color) {
            return Math.abs(this.r - ((Color) obj).r) < EPSILON &&
                    Math.abs(this.g - ((Color) obj).g) < EPSILON &&
                    Math.abs(this.b - ((Color) obj).b) < EPSILON;
        }

        return false;

    }
}


