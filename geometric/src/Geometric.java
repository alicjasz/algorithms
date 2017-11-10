import static java.lang.Math.sqrt;

public class Geometric {

    double dist(Point p1, Point p2) {

        double xDist = Math.pow(p1.getXCoord() - p2.getXCoord(), 2);
        double yDist = Math.pow(p1.getYCoord() - p2.getYCoord(), 2);
        double zDist = Math.pow(p1.getZCoord() - p2.getZCoord(), 2);

        return sqrt(xDist +  yDist + zDist);
    }

    double dist(Edge e, Point p) {
        return 0;
    }

    double dist(Edge e1, Edge e2) {
        return 0;
    }

    double dist(Edge e, Face f) {
        return 0;
    }

    double dist(Face f, Point p) {
        return 0;
    }

    double dist(Face f1, Face f2) {
        return 0;
    }

    double dist(Solid s1, Solid s2) {
        return 0;
    }
}
