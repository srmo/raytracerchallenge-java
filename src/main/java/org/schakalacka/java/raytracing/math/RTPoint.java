package org.schakalacka.java.raytracing.math;

public class RTPoint extends Tuple {

    public RTPoint(final double x, final double y, final double z) {
        super(x, y, z, 1);
    }

    public Tuple add(RTPoint ignoredThat) {
        throw new ArithmeticException("Can't add two points");
    }

    public RTVector sub(RTPoint that) {
        return Tuple.vector(this.x() - that.x(), this.y() - that.y(), this.z() - that.z());
    }

    @Override
    public Tuple cross(Tuple that) {
        throw new ArithmeticException("Point doesn't allow cross-product");
    }

    @Override
    public Tuple negate() {
        throw new ArithmeticException("Can't negate a point");
    }

    @Override
    public Tuple mul(double scalar) {
        throw new ArithmeticException("Can't multiply point with scalar");
    }

    @Override
    public Tuple div(double scalar) {
        throw new ArithmeticException("Can't divide point by scalar");
    }
}
