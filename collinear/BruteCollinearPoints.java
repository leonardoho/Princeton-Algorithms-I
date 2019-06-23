/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {

    private final ArrayList<LineSegment> segments;
    private int numSegments;

    public BruteCollinearPoints(Point[] points) {   // finds all line segments containing 4 points
        if (points == null) { throw new IllegalArgumentException(); }

        Point[] sortedPoints = points.clone();
        segments = new ArrayList<LineSegment>();
        numSegments = 0;

        double slope1;
        double slope2;
        double slope3;

        Arrays.sort(sortedPoints);

        for (int j = 0; j < sortedPoints.length - 1; j++) {
            if (sortedPoints[j].compareTo(sortedPoints[j+1]) == 0)
                throw new IllegalArgumentException();
        }

        for (int i = 0; i < sortedPoints.length - 3; i++) {

            for (int j = i + 1; j < sortedPoints.length - 2; j++) {

                slope1 = sortedPoints[i].slopeTo(sortedPoints[j]);

                for (int k = j + 1; k < sortedPoints.length - 1; k++) {

                    slope2 = sortedPoints[i].slopeTo(sortedPoints[k]);

                    if (slope1 == slope2) {
                        for (int m = k + 1; m < sortedPoints.length; m++) {

                            slope3 = sortedPoints[i].slopeTo(sortedPoints[m]);

                            if (slope1 == slope2 && slope2 == slope3) {
                                segments.add(new LineSegment(sortedPoints[i], sortedPoints[m]));
                                numSegments++;
                            }
                        }
                    }
                }
            }
        }

    }

    public int numberOfSegments() {                 // the number of line segments
        return numSegments;
    }

    public LineSegment[] segments() {               // the line segments
        return segments.toArray(new LineSegment[segments.size()]);
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        StdOut.println(collinear.numberOfSegments());

        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
