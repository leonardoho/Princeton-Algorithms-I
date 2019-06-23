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

public class FastCollinearPoints {

    private ArrayList<LineSegment> segments;
    private int numSegments;

    public FastCollinearPoints(Point[] points) {   // finds all line segments containing 4 points

        if (points == null) { throw new IllegalArgumentException(); }

        segments =  new ArrayList<LineSegment>();
        numSegments = 0;
        int totalPoints = points.length;
        Point[] naturalSort = points.clone();
        Point[] slopeSort = points.clone();

        // Sort first in natural order to find which points will be minimum or maximum
        Arrays.sort(naturalSort);

        for (int k = 0; k < naturalSort.length - 1; k++) {
            if (naturalSort[k].compareTo(naturalSort[k+1]) == 0)
                throw new IllegalArgumentException();
        }

        Point origin;

        for (int i = 0; i < totalPoints; i++) {
            origin = naturalSort[i];

            // Sort the points based on normal order and then slope order
            Arrays.sort(slopeSort);
            Arrays.sort(slopeSort, origin.slopeOrder());


            double currSlope = origin.slopeTo(slopeSort[1]);
            int numPoints = 2;
            int minIndex = 1;

            for (int j = 2; j < totalPoints; j++) {

                if (origin.slopeTo(slopeSort[j]) == currSlope) {
                    numPoints++;
                } else {
                    if (numPoints >= 4 && origin.compareTo(slopeSort[minIndex]) < 0) {
                        segments.add(new LineSegment(origin, slopeSort[j - 1]));
                        numSegments++;
                    }
                    numPoints = 2;
                    minIndex = j;
                    currSlope = origin.slopeTo(slopeSort[j]);
                }
            }

            if (numPoints >= 4 && origin.compareTo(slopeSort[minIndex]) < 0) {
                segments.add(new LineSegment(origin, slopeSort[totalPoints - 1]));
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        StdOut.println("Number of Segments: " + collinear.numSegments);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
