public class Edge {

    Point source;
    Point target;

    public Edge(Point s, Point t){
        this.source = s;
        this.target = t;
    }

    public Point getSource(){
        return source;
    }

    public Point getTarget() {
        return target;
    }

    public void setSource(Point source) {
        this.source = source;
    }

    public void setTarget(Point target) {
        this.target = target;
    }
}
