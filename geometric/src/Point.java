public class Point {

    double x;
    double y;
    double z;

    public Point(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point(Point face_point) {
    }

    public double getXCoord(){
        return  x;
    }

    public double getYCoord(){
        return  y;
    }

    public double getZCoord(){
        return z;
    }

    public void setXCoord(double x){
        this.x = x;
    }

    public void setYCoord(double y){
        this.y = y;
    }

    public void setZCoord(double z){
        this.z = z;
    }
}
