import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public class Geometric {

    double dist(Point p1, Point p2) {

        double xDist = Math.pow(p1.getXCoord() - p2.getXCoord(), 2);
        double yDist = Math.pow(p1.getYCoord() - p2.getYCoord(), 2);
        double zDist = Math.pow(p1.getZCoord() - p2.getZCoord(), 2);

        return sqrt(xDist +  yDist + zDist);
    }

    double dot(Point p1, Point p2){
        return p1.getXCoord() * p2.getXCoord() + p1.getYCoord() * p2.getYCoord() + p1.getZCoord() * p2.getZCoord();
    }

    double norm(Point p){
        return Math.sqrt(dot(p,p));
    }

    double d(Point p1, Point p2){

        return norm(new Point(p1.getXCoord() - p2.getXCoord(),
                              p1.getYCoord() - p2.getYCoord(),
                              p1.getZCoord() - p2.getZCoord()));
    }

    double dist(Edge e, Point p) {

        Point v = new Point(e.getTarget().getXCoord() - e.getSource().getXCoord(),
                            e.getTarget().getYCoord() - e.getSource().getYCoord(),
                            e.getTarget().getZCoord() - e.getSource().getZCoord());

        Point w = new Point(p.getXCoord() - e.getSource().getXCoord(),
                            p.getYCoord() - e.getSource().getYCoord(),
                               p.getZCoord() - e.getSource().getZCoord());


        double c1 = dot(v, w);
        if(c1 < 0){
            return d(p,e.getSource());
        }

        double c2 = dot(v, v);
        if(c2 <= c1){
            return d(p, e.getTarget());
        }

        double b = c1 / c2;
        Point pb = new Point(e.getSource().getXCoord() + b * v.getXCoord(),
                             e.getSource().getYCoord() + b * v.getYCoord(),
                             e.getSource().getZCoord() + b * v.getZCoord());

        return d(p, pb);
    }

    double dist(Edge e1, Edge e2){

        Point u = new Point(e1.getTarget().getXCoord() - e1.getSource().getXCoord(),
                            e1.getTarget().getYCoord() - e1.getSource().getYCoord(),
                            e1.getTarget().getZCoord() - e1.getSource().getZCoord());

        Point v = new Point(e2.getTarget().getXCoord() - e2.getSource().getXCoord(),
                            e2.getTarget().getYCoord() - e2.getSource().getYCoord(),
                            e2.getTarget().getZCoord() - e2.getSource().getZCoord());

        Point w = new Point(e1.getSource().getXCoord() - e2.getSource().getXCoord(),
                            e1.getSource().getYCoord() - e2.getSource().getYCoord(),
                            e1.getSource().getZCoord() - e2.getSource().getZCoord());

        double a = dot(u, u);
        double b = dot(u, v);
        double c = dot(v, v);
        double d = dot(u, w);
        double e = dot(v, w);

        double D = a * c  - b * b;
        double sc, sN, sD = D;
        double tc, tN, tD = D;

        if(D < 0.00000001){
            sN = 0.0;
            sD = 1.0;
            tN = e;
            tD = c;
        }
        else {
            sN = (b * e - c * d);
            tN = (a * e - b * d);
            if (sN < 0.0) {
                sN = 0.0;
                tN = e;
                tD = c;
            } else if (sN > sD) {
                sN = sD;
                tN = e + b;
                tD = c;
            }
        }
        if (tN < 0.0) {
            tN = 0.0;
            if (-d < 0.0)
                sN = 0.0;
            else if (-d > a)
                sN = sD;
            else {
                sN = -d;
                sD = a;
            }
        }
        else if (tN > tD) {
            tN = tD;
            if ((-d + b) < 0.0)
                sN = 0;
            else if ((-d + b) > a)
                sN = sD;
            else {
                sN = (-d +  b);
                sD = a;
            }
        }
        sc = (abs(sN) < 0.00000001 ? 0.0 : sN / sD);
        tc = (abs(tN) < 0.00000001 ? 0.0 : tN / tD);

        Point dP = new Point(w.getXCoord() + sc * u.getXCoord() - tc * v.getXCoord(),
                             w.getYCoord() + sc * u.getYCoord() - tc * v.getYCoord(),
                             w.getZCoord()+ sc * u.getYCoord() - tc * v.getZCoord());

        return norm(dP);
    }

    double dist(Edge e, Face f) {

        List<Double> dists = new ArrayList<>();
        dists.add(dist(f.e1,e));
        dists.add(dist(f.e2,e));
        dists.add(dist(f.e3,e));
        dists.add(dist(f, e.getSource()));
        dists.add(dist(f,e.getTarget()));

        return Collections.min(dists);
    }

    double dist(Face f, Point p) {


        Point diff = new Point(p.getXCoord() - f.getX().getXCoord(),
                               p.getYCoord() - f.getX().getYCoord(),
                               p.getZCoord() - f.getX().getZCoord());

        Point edge0 = new Point(f.getY().getXCoord() - f.getX().getXCoord(),
                                f.getY().getYCoord() - f.getX().getYCoord(),
                                f.getY().getZCoord() - f.getX().getZCoord());

        Point edge1 = new Point(f.getZ().getXCoord() - f.getX().getXCoord(),
                                f.getZ().getYCoord() - f.getX().getYCoord(),
                                f.getZ().getZCoord() - f.getX().getZCoord());

        double a00 = dot(edge0, edge0);
        double a01 = dot(edge0, edge1);
        double a11 = dot(edge1, edge1);
        double b0 = -dot(diff, edge0);
        double b1 = -dot(diff, edge1);
        final double  zero = (double)0;
        final double  one = (double)1;
        double det = a00 * a11 - a01 * a01;
        double t0 = a01 * b1 - a11 * b0;
        double t1 = a01 * b0 - a00 * b1;

        if (t0 + t1 <= det)
        {
            if (t0 < zero)
            {
                if (t1 < zero)  // region 4
                {
                    if (b0 < zero)
                    {
                        t1 = zero;
                        if (-b0 >= a00)  // V1
                        {
                            t0 = one;
                        }
                        else  // E01
                        {
                            t0 = -b0 / a00;
                        }
                    }
                    else
                    {
                        t0 = zero;
                        if (b1 >= zero)  // V0
                        {
                            t1 = zero;
                        }
                        else if (-b1 >= a11)  // V2
                        {
                            t1 = one;
                        }
                        else  // E20
                        {
                            t1 = -b1 / a11;
                        }
                    }
                }
                else  // region 3
                {
                    t0 = zero;
                    if (b1 >= zero)  // V0
                    {
                        t1 = zero;
                    }
                    else if (-b1 >= a11)  // V2
                    {
                        t1 = one;
                    }
                    else  // E20
                    {
                        t1 = -b1 / a11;
                    }
                }
            }
            else if (t1 < zero)  // region 5
            {
                t1 = zero;
                if (b0 >= zero)  // V0
                {
                    t0 = zero;
                }
                else if (-b0 >= a00)  // V1
                {
                    t0 = one;
                }
                else  // E01
                {
                    t0 = -b0 / a00;
                }
            }
            else  // region 0, interior
            {
                double invDet = one / det;
                t0 *= invDet;
                t1 *= invDet;
            }
        }
        else
        {
            double tmp0, tmp1, numer, denom;

            if (t0 < zero)  // region 2
            {
                tmp0 = a01 + b0;
                tmp1 = a11 + b1;
                if (tmp1 > tmp0)
                {
                    numer = tmp1 - tmp0;
                    denom = a00 - ((double)2)*a01 + a11;
                    if (numer >= denom)  // V1
                    {
                        t0 = one;
                        t1 = zero;
                    }
                    else  // E12
                    {
                        t0 = numer / denom;
                        t1 = one - t0;
                    }
                }
                else
                {
                    t0 = zero;
                    if (tmp1 <= zero)  // V2
                    {
                        t1 = one;
                    }
                    else if (b1 >= zero)  // V0
                    {
                        t1 = zero;
                    }
                    else  // E20
                    {
                        t1 = -b1 / a11;
                    }
                }
            }
            else if (t1 < zero)  // region 6
            {
                tmp0 = a01 + b1;
                tmp1 = a00 + b0;
                if (tmp1 > tmp0)
                {
                    numer = tmp1 - tmp0;
                    denom = a00 - ((double)2)*a01 + a11;
                    if (numer >= denom)  // V2
                    {
                        t1 = one;
                        t0 = zero;
                    }
                    else  // E12
                    {
                        t1 = numer / denom;
                        t0 = one - t1;
                    }
                }
                else
                {
                    t1 = zero;
                    if (tmp1 <= zero)  // V1
                    {
                        t0 = one;
                    }
                    else if (b0 >= zero)  // V0
                    {
                        t0 = zero;
                    }
                    else  // E01
                    {
                        t0 = -b0 / a00;
                    }
                }
            }
            else  // region 1
            {
                numer = a11 + b1 - a01 - b0;
                if (numer <= zero)  // V2
                {
                    t0 = zero;
                    t1 = one;
                }
                else
                {
                    denom = a00 - ((double)2)*a01 + a11;
                    if (numer >= denom)  // V1
                    {
                        t0 = one;
                        t1 = zero;
                    }
                    else  // 12
                    {
                        t0 = numer / denom;
                        t1 = one - t0;
                    }
                }
            }
        }

        Point closest = new Point(f.getX().getXCoord()+ t0 * edge0.getXCoord() + t1 * edge1.getXCoord(),
                                  f.getX().getYCoord()+ t0 * edge0.getYCoord() + t1 * edge1.getYCoord(),
                                  f.getX().getZCoord()+ t0 * edge0.getZCoord()+ t1 * edge1.getZCoord());

        Point diff2 = new Point(p.getXCoord() - closest.getXCoord(),
                                p.getYCoord() - closest.getYCoord(),
                                p.getZCoord() - closest.getZCoord());

        double squaredDist = dot(diff2, diff2);

        return Math.sqrt(squaredDist);
    }

    double dist(Face f1, Face f2) {

        List<Double> dists= new ArrayList<>();
        dists.add(dist(f2.e1, f1));
        dists.add(dist(f2.e2, f1));
        dists.add(dist(f2.e3, f1));
        dists.add(dist(f1.e1, f2));
        dists.add(dist(f1.e2,f2));
        dists.add(dist(f1.e3,f2));

        return Collections.min(dists);
    }

    double dist(Solid s1, Solid s2) {

        Face [] s1_faces = s1.getFaces();
        Face [] s2_faces = s2.getFaces();
        double distance = Double.MAX_VALUE;

        for(Face f1 : s1_faces){
            for(Face f2 : s2_faces){
                double temp = dist(f1, f2);
                if(temp < distance){
                    distance = temp;
                }
            }
        }
        return distance;
    }

    Solid parse_file(String filename) throws IOException {

        File file1 = new File(filename);
        Scanner input = new Scanner(file1);
        Point[] face_points = new Point[3];
        List<Face> face_list = new ArrayList<>();
        int i = 0;
        while (input.hasNext()) {
            String line = input.next();
            if (line.contains(";")) {
                String[] coord = line.split(";");
                face_points[i] = new Point(Double.parseDouble(coord[0]), Double.parseDouble(coord[1]),
                        Double.parseDouble(coord[2]));
                i += 1;
            }
            if (line.isEmpty() || line.matches("^\\s*$") || i == 3) {
                face_list.add(new Face(face_points[0], face_points[1], face_points[2]));
                i = 0;
            }
            if (!input.hasNext()) {
                    break;
            }
        }
        System.out.println("Face list size: " + face_list.size());
        Solid sld = new Solid(face_list, face_list.size());
        return sld;
    }

    public static void main(String[] args) throws IOException {

        Geometric g = new Geometric();
        Point p = new Point(0.0, 0.0, 0.0);
        Point p1 = new Point(0.0, 100.0, 0.13);
        System.out.println("Point to point: " + g.dist(p, p1));
        Edge e = new Edge(new Point(1, 2, 3), new Point(4, 5, 6));
        Edge e1 = new Edge(new Point(-1, -2, -3), new Point(-4, -5, -6));
        double res = g.dist(e, p);
        System.out.println("Distance between point and edge: " + res);
        System.out.println("Distance between two edges: " + g.dist(e, e1));

        Face f1 = new Face(new Point(1.0, 0.0, 0.0), new Point(0.0, 1.0, 0.0),
                           new Point(0.0, 0.0, 0.0));

        Face f2 = new Face(new Point(100.0, 0.0, 0.13), new Point(0.0, 100.0, 0.13),
                           new Point(-100.0, -100.0, 0.13));

        System.out.println("Distance between 2 faces: " + g.dist(f1, f2));
        Solid solid1 = g.parse_file("C:/Users/ja/Desktop/studia/magisterskie/2 semestr/algorytmy/geometric/src/solid.txt");
        Solid solid2 = g.parse_file("C:/Users/ja/Desktop/studia/magisterskie/2 semestr/algorytmy/geometric/src/solid2.txt");

        System.out.println("Distance between two solids: " + g.dist(solid1, solid2));
    }
}
