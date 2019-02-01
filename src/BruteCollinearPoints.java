import edu.princeton.cs.algs4.ResizingArrayBag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {

    private final ResizingArrayBag<LineSegment> segs
            = new ResizingArrayBag<LineSegment>();

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points)
    {
        if (points == null)
            throw new IllegalArgumentException("Argument can't be null");

        Point[] a = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException("Null points aren't allowed");
            a[i] = points[i];
        }

        Arrays.sort(a);

        for (int i = 1; i < a.length; i++)
        {
            if (a[i-1].compareTo(a[i]) == 0)
                throw new IllegalArgumentException("Duplicate points aren't allowed");
        }

        for (int i = 0; i < a.length - 3; i++)
            for (int j = i + 1; j < a.length - 2; j++)
                for (int k = j + 1; k < a.length - 1; k++)
                    for (int m = k + 1; m < a.length; m++)
                        if (Double.compare(
                            a[i].slopeTo(a[j]),
                            a[j].slopeTo(a[k])) == 0
                            && Double.compare(
                            a[i].slopeTo(a[j]),
                            a[k].slopeTo(a[m])) == 0
                        ) segs.add(new LineSegment(a[i], a[m]));

    }


    // the number of line segments
    public int numberOfSegments()
    {
        return segs.size();
    }

    // the line segments
    public LineSegment[] segments()
    {
        LineSegment[] segArray = new LineSegment[numberOfSegments()];
        int i = 0;
        for (LineSegment s : segs) {
            segArray[i++] = s;
        }
        return segArray;
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
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
