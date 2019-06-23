/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {

    private final SET<Point2D> points;

    public PointSET() {                                 // construct an empty set of points
       points = new SET<Point2D>();
    }

    public boolean isEmpty() {                          // is the set empty?
        return points.isEmpty();
    }

    public int size() {                                 // number of points in the set
        return points.size();
    }

    public void insert(Point2D p) {                     // add the point to the set (if it is not already in the set)
        if (p == null)
            throw new IllegalArgumentException();

        points.add(p);
    }

    public boolean contains(Point2D p) {                // does the set contain point p?
        if (p == null)
            throw new IllegalArgumentException();

        return points.contains(p);
    }

    public void draw() {                                // draw all points to standard draw
        for (Point2D point : points) {
            point.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {       // all points that are inside the rectangle (or on the boundary)
        if (rect == null)
            throw new IllegalArgumentException();

        Queue<Point2D> rectPoints = new Queue<Point2D>();

        for (Point2D point : points) {
            if (rect.contains(point))
                rectPoints.enqueue(point);
        }

        return rectPoints;
    }

    public Point2D nearest(Point2D p) {                  // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null)
            throw new IllegalArgumentException();

        Point2D nearestPoint = null;
        double minDistance = 1;
        double currDistance = 1;

        for (Point2D point : points) {
            currDistance = p.distanceSquaredTo(point);

            if (nearestPoint == null) {
                nearestPoint = point;
                minDistance = currDistance;
            }

            if (currDistance < minDistance) {
                nearestPoint = point;
                minDistance = currDistance;
            }
        }

        return nearestPoint;
    }

    public static void main(String[] args) {
        // Test
    }
}
