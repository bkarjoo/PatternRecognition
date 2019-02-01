import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ResizingArrayBag;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {

    private final ResizingArrayBag<LineSegment> segs
            = new ResizingArrayBag<LineSegment>();

    public FastCollinearPoints(Point[] points) {
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

        Point[] aux = new Point[a.length];
        for (int lo = 0; lo < a.length - 3; lo++)
        {
            Point p = a[lo];

            Point[] copy = new Point[a.length - lo - 1];
            for (int i = lo + 1, j = 0; i < a.length; i++, j++) copy[j] = a[i];

            sort(copy, aux, 0, copy.length - 1, p.slopeOrder());

//            System.out.println("SORTED " + p);
//            for (int i = lo + 1; i < copy.length; i++)
//                System.out.println(copy[i] + " " + p.slopeTo(copy[i]));

            // pass over sorted array to get largest line greater than 4 points
            Double slope = null;
            int count = 1;

            for (int j = 0; j < copy.length; j++) {
                if (slope == null)
                    slope = p.slopeTo(copy[j]);
                else if (Double.compare(slope, p.slopeTo(copy[j])) == 0) {
                    count++;
                } else {
                    if (count >= 3) {
                        // what if the line segment we found was already found
                        // this is only true if one of the previous points also gives same slope
                        boolean addIt = true;
                        for (int i = 0; i < lo; i++) {
                            if (Double.compare(p.slopeTo(a[i]), slope) == 0)
                            {
                                addIt = false;
                                break;
                            }
                        }
                        if (addIt)
                            segs.add(new LineSegment(p, copy[j - 1]));
                    }
                    count = 1;
                    slope = p.slopeTo(copy[j]);
                }
            }

            if (count >= 3) {
                segs.add(new LineSegment(p, copy[copy.length - 1]));
            }
        }

    }

    private static boolean less(Comparator<Point> c, Point v, Point w)
    {
        return c.compare(v, w) < 0;
    }

    private static void merge(Point[] a, Point[] aux, int lo, int mid, int hi, Comparator<Point> c)
    {
        // copy to aux
        for (int i = lo; i <= hi; i++) aux[i] = a[i];

        // merge from aux
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++)
        {
            if (i > mid) a[k] = aux[j++];
            else if (j > hi) a[k] = aux[i++];
            else if (less(c, aux[j], aux[i])) a[k] = aux[j++];
            else a[k] = aux[i++];
        }
    }

    private static void sort(Point[] a, Point[] aux, int lo, int hi, Comparator c)
    {
        if (hi <= lo) return;
        int mid = lo + (hi - lo) / 2;
        sort(a, aux, lo, mid, c);
        sort(a, aux, mid+1, hi, c);
        merge(a, aux, lo, mid, hi, c);
    }

    public int numberOfSegments()
    {
        return segs.size();
    }

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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
