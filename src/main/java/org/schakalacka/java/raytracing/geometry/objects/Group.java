package org.schakalacka.java.raytracing.geometry.objects;

import org.schakalacka.java.raytracing.Constants;
import org.schakalacka.java.raytracing.geometry.tracing.Intersection;
import org.schakalacka.java.raytracing.geometry.tracing.Ray;
import org.schakalacka.java.raytracing.math.Tuple;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Group extends Shape {
    private final List<Shape> children = new ArrayList<>();
    private BoundingBox boundingBox = new BoundingBox(
            Tuple.point(Constants.POSITIVE_INFINITY, Constants.POSITIVE_INFINITY, Constants.POSITIVE_INFINITY),
            Tuple.point(Constants.NEGATIVE_INFINITY, Constants.NEGATIVE_INFINITY, Constants.NEGATIVE_INFINITY));

    @Override
    public BoundingBox getBounds() {
        return boundingBox;
    }

    private void calculateBounds() {
        if (children.size() == 0) {
            this.boundingBox = new BoundingBox(
                    Tuple.point(Constants.POSITIVE_INFINITY, Constants.POSITIVE_INFINITY, Constants.POSITIVE_INFINITY),
                    Tuple.point(Constants.NEGATIVE_INFINITY, Constants.NEGATIVE_INFINITY, Constants.NEGATIVE_INFINITY));
        } else {
            double minX, minY, minZ, maxX, maxY, maxZ;
            minX = minY = minZ = maxX = maxY = maxZ = 0;

            for (Shape child : children) {
                BoundingBox childBoundingBox = child.getBounds().getTransformedBounds(child.getTransformationMatrix());
                minX = Math.min(minX, childBoundingBox.lower().x());
                minY = Math.min(minY, childBoundingBox.lower().y());
                minZ = Math.min(minZ, childBoundingBox.lower().z());
                maxX = Math.max(maxX, childBoundingBox.upper().x());
                maxY = Math.max(maxY, childBoundingBox.upper().y());
                maxZ = Math.max(maxZ, childBoundingBox.upper().z());
            }

            this.boundingBox = new BoundingBox(Tuple.point(minX, minY, minZ), Tuple.point(maxX, maxY, maxZ));

        }
    }

    @Override
    public List<Intersection> localIntersect(Ray ray) {
        var intersections = new ArrayList<Intersection>();
        for (Shape child : children) {
            intersections.addAll(child.intersect(ray));
        }
        intersections.sort(Comparator.comparingDouble(Intersection::getDistance));
        return intersections;
    }

    @Override
    public Tuple localNormalVectorAt(Tuple point) {
        throw new UnsupportedOperationException("Group does not have a normal vector");
    }

    public List<Shape> getChildren() {
        return children;
    }

    public Group addChild(Shape s) {
        children.add(s);
        s.setParent(this);
        calculateBounds();
        return this;
    }
}
