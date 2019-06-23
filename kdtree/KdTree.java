/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {

    private int size;
    private Node root;

    private static class Node {
        private final Point2D point;
        private final RectHV rect;
        private Node lb;
        private Node rt;
        private final boolean vertical;

        public Node(Point2D p, double xmin, double ymin, double xmax, double ymax, boolean vert) {
            point = p;
            rect = new RectHV(xmin, ymin, xmax, ymax);
            lb = null;
            rt = null;
            vertical = vert;
        }
    }

    public KdTree() {                                 // construct an empty set of points
        root = null;
        size = 0;

    }

    public boolean isEmpty() {                          // is the set empty?
        return size == 0;
    }

    public int size() {                                 // number of points in the set
        return size;
    }

    public void insert(Point2D p) {                     // add the point to the set (if it is not already in the set)

        if (root == null) {
            root = new Node(p, 0, 0, 1, 1, true);
            size++;
        }

        Node iteratePointer = root;

        // Converted level to boolean to save memory
        double xmin = 0;
        double xmax = 1;
        double ymin = 0;
        double ymax = 1;

        if (!this.contains(p)) {
            while (true) {

                // Check the current level to see if the key is x-coor or y-coor
                if (iteratePointer.vertical) {
                    if (p.x() < iteratePointer.point.x()) {
                        xmax = iteratePointer.point.x();
                        if (iteratePointer.lb == null) {
                            iteratePointer.lb = new Node(p, xmin, ymin, xmax, ymax, false);
                            break;
                        } else {
                            iteratePointer = iteratePointer.lb;
                        }
                    } else {
                        xmin = iteratePointer.point.x();
                        if (iteratePointer.rt == null) {
                            iteratePointer.rt = new Node(p, xmin, ymin, xmax, ymax, false);
                            break;
                        } else {
                            iteratePointer = iteratePointer.rt;
                        }
                    }
                } else {
                    if (p.y() < iteratePointer.point.y()) {
                        ymax = iteratePointer.point.y();
                        if (iteratePointer.lb == null) {
                            iteratePointer.lb = new Node(p, xmin, ymin, xmax, ymax, true);
                            break;
                        } else {
                            iteratePointer = iteratePointer.lb;
                        }
                    } else {
                        ymin = iteratePointer.point.y();
                        if (iteratePointer.rt == null) {
                            iteratePointer.rt = new Node(p, xmin, ymin, xmax, ymax, true);
                            break;
                        } else {
                            iteratePointer = iteratePointer.rt;
                        }
                    }
                }
            }

            size++;
        }
    }

    public boolean contains(Point2D p) {                // does the set contain point p?

        if (p == null)
            throw new IllegalArgumentException();

        return get(p) != null;

        // Alternative implmenetation of contains where we traverse the tree through a while loop

        // if (root == null)
        //     return false;
        //
        // Node iteratePointer = root;
        // int level = 0;
        //
        // while (iteratePointer != null) {
        //     if (iteratePointer.point.x() == p.x() && iteratePointer.point.y() == p.y())
        //         return true;
        //
        //     if (level % 2 == 0) {
        //         if (p.x() < iteratePointer.point.x()) {
        //             iteratePointer = iteratePointer.lb;
        //         } else {
        //             iteratePointer = iteratePointer.rt;
        //         }
        //     } else {
        //         if (p.y() < iteratePointer.point.y()) {
        //             iteratePointer = iteratePointer.lb;
        //         } else {
        //             iteratePointer = iteratePointer.rt;
        //         }
        //     }
        //     level++;
        // }
        //
        // return false;
    }

    private Node get(Point2D p) {
        return get(root, p);
    }

    private Node get(Node n, Point2D p) {

        if (n == null)
            return null;

        if (n.point.x() == p.x() && n.point.y() == p.y())
            return n;

        if (n.vertical) {
            if (p.x() < n.point.x())
                return get(n.lb, p);
            else
                return get(n.rt, p);
        } else {
            if (p.y() < n.point.y())
                return get(n.lb, p);
            else
                return get(n.rt, p);
        }
    }

    public void draw() {                                // draw all points to standard draw
        draw(root);
    }

    private void draw(Node n) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        n.point.draw();

        if (n.vertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.point.x(), n.rect.ymin(), n.point.x(), n.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BOOK_BLUE);
            StdDraw.line(n.rect.xmin(), n.point.y(), n.rect.xmax(), n.point.y());
        }

        if (n.lb != null)
            draw(n.lb);

        if (n.rt != null)
            draw(n.rt);
    }

    public Iterable<Point2D> range(RectHV rect) {       // all points that are inside the rectangle (or on the boundary)
        Queue<Point2D> rectPoints = new Queue<Point2D>();
        search(root, rect, rectPoints);
        return rectPoints;
    }

    private void search(Node n, RectHV rect, Queue<Point2D> queue) {

        if (n == null)
            return;

        if (n.point.x() <= rect.xmax() && n.point.x() >= rect.xmin() && n.point.y() <= rect.ymax() && n.point.y() >= rect.ymin()) {
            queue.enqueue(n.point);
        }

        if (n.lb != null && n.lb.rect.intersects(rect))
            search(n.lb, rect, queue);

        if (n.rt != null && n.rt.rect.intersects(rect))
            search(n.rt, rect, queue);
    }

    public Point2D nearest(Point2D p) {                  // a nearest neighbor in the set to point p; null if the set is empty
        if (isEmpty())
            return null;

        return nearestPoint(p, root);

    }

    private Point2D nearestPoint(Point2D queryPoint, Node n) {

        Point2D min = n.point;
        double currMin = n.point.distanceSquaredTo(queryPoint);
        double leftDist;
        double rightDist;

        if (n.lb == null && n.rt == null)
            return min;
        else if (n.lb != null && n.rt == null) {
            leftDist = n.lb.rect.distanceSquaredTo(queryPoint);
            if (leftDist < currMin && n.lb.point.distanceSquaredTo(queryPoint) < currMin)
                min = nearestPoint(queryPoint, n.lb);
        }
        else if (n.lb == null) {
            rightDist = n.rt.rect.distanceSquaredTo(queryPoint);
            if (rightDist < currMin && n.rt.point.distanceSquaredTo(queryPoint) < currMin)
                min = nearestPoint(queryPoint, n.rt);
        } else {
            leftDist = n.lb.rect.distanceSquaredTo(queryPoint);
            rightDist = n.rt.rect.distanceSquaredTo(queryPoint);
            if (leftDist < rightDist) {
                if (leftDist < currMin && n.lb.point.distanceSquaredTo(queryPoint) < currMin) {
                    min = nearestPoint(queryPoint, n.lb);
                    currMin = min.distanceSquaredTo(queryPoint);
                }
                if (rightDist < currMin) {
                    min = nearestPoint(queryPoint, n.rt);
                }
            } else {
                if (rightDist < currMin && n.rt.point.distanceSquaredTo(queryPoint) < currMin) {
                    min = nearestPoint(queryPoint, n.rt);
                    currMin = min.distanceSquaredTo(queryPoint);
                }
                if (leftDist < currMin) {
                    min = nearestPoint(queryPoint, n.lb);
                }
            }
        }
        return min;
    }

    public static void main(String[] args) {

        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            StdOut.println(kdtree.size);
        }

        // StdOut.println("=================================");
        //
        // if (kdtree.contains(new Point2D(0.499, 0.208))) {
        //     StdOut.println("found!");
        // } else {
        //     StdOut.println("Not found");
        // }

    }
}
