package org.schakalacka.java.raytracing;

public class Tuple {

    private final float x;
    private final float y;
    private final float z;
    private final float w;

    public float x() {
        return x;
    }

    public float y() {
        return y;
    }

    public float z() {
        return z;
    }

    public float w() {
        return w;
    }

    private Tuple(final float x, final float y, final float z, final float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public static Tuple tuple(float x, float y, float z, float w) {
        return new Tuple(x, y, z, w);
    }

    public static Tuple point(float x, float y, float z) {
        return new Tuple(x, y, z, 1.0f);
    }

    public static Tuple vector(float x, float y, float z) {
        return new Tuple(x, y, z, 0.0f);
    }

    public final boolean isVector() {
        return w == 0.0f;
    }

    public final boolean isPoint() {
        return w == 1.0f;
    }

    public Tuple add(Tuple that) {
        if (this.isPoint() && that.isPoint()) {
            throw new ArithmeticException("Can't add two Points");
        }
        return new Tuple(this.x + that.x, this.y + that.y, this.z + that.z, this.w + that.w);

    }

    public Tuple sub(Tuple that) {
        return new Tuple(this.x - that.x, this.y - that.y, this.z - that.z, this.w - that.w);
    }
}


