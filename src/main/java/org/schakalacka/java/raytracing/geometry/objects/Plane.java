package org.schakalacka.java.raytracing.geometry.objects;

import org.schakalacka.java.raytracing.Constants;
import org.schakalacka.java.raytracing.geometry.tracing.Intersection;
import org.schakalacka.java.raytracing.geometry.tracing.Ray;
import org.schakalacka.java.raytracing.math.Tuple;

import java.util.Collections;
import java.util.List;

import static org.schakalacka.java.raytracing.Constants.SHAPE_POINT_OFFSET_EPSILON;

/***
 * A Plane is infinite in XZ direction and infinitely thin (y == 0)
 */
public class Plane extends Shape {
    @Override
    public BoundingBox getBounds() {
        return new BoundingBox(Tuple.point(Constants.NEGATIVE_INFINITY, 0, Constants.NEGATIVE_INFINITY),
                Tuple.point(Constants.POSITIVE_INFINITY, 0, Constants.POSITIVE_INFINITY));
    }

    @Override
    public List<Intersection> localIntersect(Ray ray) {
        if (Math.abs(ray.direction().y()) < SHAPE_POINT_OFFSET_EPSILON) {
            return Collections.emptyList();
        }

        double distance = -ray.origin().y() / ray.direction().y();
        return Collections.singletonList(new Intersection(this, distance));
    }

    @Override
    public Tuple localNormalVectorAt(Tuple point) {
        return Tuple.vector(0, 1, 0);
    }
}
