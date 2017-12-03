public class Face {

    Point x;
    Point y;
    Point z;

    Edge e1;
    Edge e2;
    Edge e3;


    public Face(Point x, Point y, Point z){
        this.x = x;
        this.y = y;
        this.z = z;
        this.e1 = new Edge(x, y);
        this.e2 = new Edge(x, z);
        this.e3 = new Edge(y, z);
    }

    public Point getX() {
        return x;
    }

    public Point getY() {
        return y;
    }

    public Point getZ() {
        return z;
    }
}
