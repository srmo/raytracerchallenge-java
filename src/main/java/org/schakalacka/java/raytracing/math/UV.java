package org.schakalacka.java.raytracing.math;

import java.util.Objects;

public record UV(float u, float v) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UV uv = (UV) o;
        return Float.compare(uv.u, u) == 0 && Float.compare(uv.v, v) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(u, v);
    }
}
