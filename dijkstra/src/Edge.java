public class Edge {

    public int source;
    public int destination;
    public int weight;

    public void setSource(int source){
        this.source = source;
    }

    public void setDestination(int destination){
        this.destination = destination;
    }

    public void setWeight(int weight){
        this.weight = weight;
    }

    public int getSource(){ return source; }

    public int getDestination(){ return destination; }

    public int getWeight(){ return weight; }

    public String toString(){
        return " " + weight;
    }

}
