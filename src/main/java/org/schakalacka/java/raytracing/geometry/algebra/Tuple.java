package org.schakalacka.java.raytracing.geometry.algebra;

import java.util.Objects;

import static org.schakalacka.java.raytracing.Constants.EPSILON;

// TODO refactor this into an interface and make point and vector first class citizens
public class Tuple {

    private final double[] values = new double[4];

    public Tuple(final double x, final double y, final double z, final double w) {
        values[0] = x;
        values[1] = y;
        values[2] = z;
        values[3] = w;
    }

    public static Tuple tuple(double x, double y, double z, double w) {
        if (w == 0) {
            return new RTVector(x, y, z);
        } else if (w == 1) {
            return new RTPoint(x, y, z);
        } else {
            return new Tuple(x, y, z, w);
        }
    }

    public static RTPoint point(double x, double y, double z) {
        return new RTPoint(x, y, z);
    }

    public static RTVector vector(double x, double y, double z) {
        return new RTVector(x, y, z);
    }

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

    public final boolean isVector() {
        return this instanceof RTVector;
    }

    public final boolean isPoint() {
        return this instanceof RTPoint;
    }

    public Tuple add(Tuple that) {
        return Tuple.tuple(this.x() + that.x(), this.y() + that.y(), this.z() + that.z(), this.w() + that.w());
    }

    public Tuple sub(Tuple that) {
        return Tuple.tuple(this.x() - that.x(), this.y() - that.y(), this.z() - that.z(), this.w() - that.w());
    }

    public Tuple negate() {
        return Tuple.tuple(this.x() * -1, this.y() * -1, this.z() * -1, this.w() * -1);
    }

    public Tuple mul(double scalar) {
        return Tuple.tuple(this.x() * scalar, this.y() * scalar, this.z() * scalar, this.w() * scalar);
    }

    public Tuple div(double scalar) {
        return Tuple.tuple(this.x() / scalar, this.y() / scalar, this.z() / scalar, this.w() / scalar);
    }

    public double magnitude() {
        return Math.sqrt(Math.pow(x(), 2) + Math.pow(y(), 2) + Math.pow(z(), 2) + Math.pow(w(), 2));
    }

    public Tuple normalize() {
        final double magnitude = magnitude();
        return Tuple.tuple(this.x() / magnitude, this.y() / magnitude, this.z() / magnitude, this.w() / magnitude);
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
        return Tuple.vector(
                this.y() * that.z() - this.z() * that.y(),
                this.z() * that.x() - this.x() * that.z(),
                this.x() * that.y() - this.y() * that.x()
        );
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
        if (obj instanceof Tuple that) {
            return Math.abs(this.x() - that.x()) < EPSILON &&
                    Math.abs(this.y() - that.y()) < EPSILON &&
                    Math.abs(this.z() - that.z()) < EPSILON &&
                    Math.abs(this.w() - that.w()) < EPSILON;
        }

        return false;

    }

    public double get(int i) {
        return values[i];
    }

    public Tuple reflect(Tuple that) {
        return this.sub(that.mul(2).mul(this.dot(that)));
    }

    public double[] getArray() {
        return this.values;
    }
}


