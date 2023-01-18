package org.schakalacka.java.raytracing.geometry.algebra;

public class UVMapping {


    public UV sphericalMap(Tuple point) {
        // compute the azimuthal angle
        // -π < theta <= π
        // angle increases clockwise as viewed from above,
        // which is opposite of what we want, but we'll fix it later.
        // srm: "opposite" b/c we use left-hand rule?
        var theta = Math.atan2(point.x(), point.z());


        // vec is the vector pointing from the sphere's origin (the world origin)
        // to the point, which will also happen to be exactly equal to the sphere's
        // radius.
        var vector = Tuple.vector(point.x(), point.y(), point.z());
        var radius = vector.magnitude();

        // compute the polar angle
        // 0 <= phi <= π
        var phi = Math.acos(point.y() / radius);

        // -0.5 < raw_u <= 0.5
        var raw_u = theta / (2 * Math.PI);

        // 0 <= u < 1
        // here's also where we fix the direction of u. Subtract it from 1,
        // so that it increases counterclockwise as viewed from above.
        var u = 1 - (raw_u + 0.5);

        // we want v to be 0 at the south pole of the sphere,
        // and 1 at the north pole, so we have to "flip it over"
        // by subtracting it from 1.
        var v = 1 - phi / Math.PI;
        return new UV(u, v);
    }
}
