package org.schakalacka.java.raytracing.geometry.algebra;

import java.util.Objects;

public record UV(double u, double v) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UV uv = (UV) o;
        return Double.compare(uv.u, u) == 0 && Double.compare(uv.v, v) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(u, v);
    }
}
