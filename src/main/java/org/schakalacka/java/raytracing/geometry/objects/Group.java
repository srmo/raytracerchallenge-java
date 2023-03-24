package org.schakalacka.java.raytracing.geometry.objects;

import org.schakalacka.java.raytracing.geometry.tracing.Intersection;
import org.schakalacka.java.raytracing.geometry.tracing.Ray;
import org.schakalacka.java.raytracing.math.Tuple;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Group extends Shape {
    private final List<Shape> children = new ArrayList<>();
    private Bounds bounds = new Bounds(Tuple.point(0, 0, 0), Tuple.point(0, 0, 0));
    @Override
    public Bounds getBounds() {
        return bounds;
    }

    private void calculateBounds() {
        if (children.size() == 0) {
            this.bounds =  new Bounds(Tuple.point(0, 0, 0), Tuple.point(0, 0, 0));
        } else {
            double minX, minY, minZ, maxX, maxY, maxZ;
            minX = minY = minZ = maxX = maxY = maxZ = 0;

            for (Shape child:children) {
                Bounds childBounds = child.getBounds().getTransformedBounds(child.getTransformationMatrix());
                minX = Math.min(minX, childBounds.lower().x());
                minY = Math.min(minY, childBounds.lower().y());
                minZ = Math.min(minZ, childBounds.lower().z());
                maxX = Math.max(maxX, childBounds.upper().x());
                maxY = Math.max(maxY, childBounds.upper().y());
                maxZ = Math.max(maxZ, childBounds.upper().z());
            }

            this.bounds = new Bounds(Tuple.point(minX, minY, minZ), Tuple.point(maxX, maxY, maxZ));

        }
    }

    @Override
    public List<Intersection> localIntersect(Ray ray) {
        var intersections = new ArrayList<Intersection>();
        for (Shape child:children) {
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
