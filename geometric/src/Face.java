public class Face {

    Point x;
    Point y;
    Point z;

    public Face(Point x, Point y, Point z){
        this.x = x;
        this.y = y;
        this.z = z;
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

    public void setX(Point x) {
        this.x = x;
    }

    public void setY(Point y) {
        this.y = y;
    }

    public void setZ(Point z) {
        this.z = z;
    }
}
