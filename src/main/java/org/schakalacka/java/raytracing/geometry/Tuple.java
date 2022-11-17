package org.schakalacka.java.raytracing.geometry;

import java.util.Objects;

public class Tuple {

    public static final double EPSILON = 0.00000001;

    private final double[] values = new double[4];
    public double x() {
        return values[0];
    }

    public double y() {
        return values[1];
    }

    public double z() {
        return values[2];
    }

    public double w() {
        return values[3];
    }

    private Tuple(final double x, final double y, final double z, final double w) {
        values[0] = x;
        values[1] = y;
        values[2] = z;
        values[3] = w;
    }

    public static Tuple tuple(double x, double y, double z, double w) {
        return new Tuple(x, y, z, w);
    }

    public static Tuple point(double x, double y, double z) {
        return new Tuple(x, y, z, 1.0);
    }

    public static Tuple vector(double x, double y, double z) {
        return new Tuple(x, y, z, 0.0);
    }

    public final boolean isVector() {
        return w() == 0.0;
    }

    public final boolean isPoint() {
        return w() == 1.0f;
    }

    public Tuple add(Tuple that) {
        if (this.isPoint() && that.isPoint()) {
            throw new ArithmeticException("Can't add two Points");
        }
        return new Tuple(this.x() + that.x(), this.y() + that.y(), this.z() + that.z(), this.w() + that.w());

    }

    public Tuple sub(Tuple that) {
        if (this.isVector() && that.isPoint()) {
            throw new ArithmeticException("Can't subtract Point from Vector");
        }
        return new Tuple(this.x() - that.x(), this.y() - that.y(), this.z() - that.z(), this.w() - that.w());
    }

    public Tuple negate() {
        return new Tuple(this.x() * -1, this.y() * -1, this.z() * -1, this.w() * -1);
    }

    public Tuple mul(double scalar) {
        return new Tuple(this.x() * scalar, this.y() * scalar, this.z() * scalar, this.w() * scalar);
    }

    public Tuple div(double scalar) {
        return new Tuple(this.x() / scalar, this.y() / scalar, this.z() / scalar, this.w() / scalar);
    }

    public double magnitude() {
        return Math.sqrt(x() * x() + y() * y() + z() * z());
    }

    public Tuple normalize() {
        final double magnitude = magnitude();
        return new Tuple(this.x() / magnitude, this.y() / magnitude, this.z() / magnitude, this.w() / magnitude);
    }

    public double dot(Tuple that) {
        return this.x() * that.x()
                + this.y() * that.y()
                + this.z() * that.z()
                + this.w() * that.w();
    }


    /***
     * We define this only for our 3 dimensional vectors
     * @param that another vector. Not null
     * @return the new Vector as result of the cross product. Never null
     * @throws ArithmeticException when either this or that tuple isn't a vector
     */
    public Tuple cross(Tuple that) {
        if (this.isVector() && that.isVector()) {
            return Tuple.vector(
                    this.y() * that.z() - this.z() * that.y(),
                    this.z() * that.x() - this.x() * that.z(),
                    this.x() * that.y() - this.y() * that.x()
            );
        } else {
            throw new ArithmeticException("Cross product only defined for Vectors");
        }
    }

    @Override
    public String toString() {
        return "{" +
                "x=" + x() +
                ", y=" + y() +
                ", z=" + z() +
                ", w=" + w() +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(x(), y(), z(), w());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Tuple) {
            return
                    this.x() - ((Tuple) obj).x() < EPSILON &&
                            this.y() - ((Tuple) obj).y() < EPSILON &&
                            this.z() - ((Tuple) obj).z() < EPSILON &&
                            this.w() - ((Tuple) obj).w() < EPSILON;
        }

        return false;

    }

    public double get(int i) {
        return values[i];
    }

    public Tuple reflect(Tuple that) {
        return this.sub(that.mul(2).mul(this.dot(that)));
    }
}


