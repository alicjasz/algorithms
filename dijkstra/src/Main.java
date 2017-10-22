import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

class MyEdge{
    public int index=0;
    public int weight=0;
    public String from;
    public String to;

    MyEdge(int index, int weight,String from, String to){
        this.index=index;
        this.weight=weight;
    }

    @Override
    public String toString() {
        return index+": "+weight;
    }


}


public class Main {
    static GraphZoomScrollPane panel = null;
    static DefaultModalGraphMouse graphMouse = null;
    static JComboBox modeBox = null;
    static ScalingControl scaler;
    public static void main(String[] args) {


        /**
         * create graph part
         */
        DirectedSparseMultigraph g = new DirectedSparseMultigraph();


        /**3
         * read file part
         */
        String fileName = "input.txt";
        List<String> list = new ArrayList<>();
        Hashtable<Pair<String>,Integer> edgeWeight=new Hashtable<Pair<String>,Integer>();
        //read file into stream, try-with-resources
        try (BufferedReader br = Files.newBufferedReader(Paths.get(fileName))) {

            //br returns as stream and convert it into a List
            list = br.lines().collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }
        //lenght(u,v) from file

        for(int i=0;i<list.size();i++){
            String[]data= list.get(i).replaceAll(" ","").split(";");
            if(!g.containsVertex(data[0])){
                g.addVertex(data[0]);
                       System.out.println("Vertex added:"+data[0]);
            }
            if(!g.containsVertex(data[1])){
                g.addVertex(data[1]);
                     System.out.println("Vertex added:"+data[1]);
            }

            Object obj=new MyEdge(i,Integer.parseInt(data[2]),data[0],data[1]);
            g.addEdge(obj,data[0],data[1], EdgeType.DIRECTED);
            edgeWeight.put(new Pair<String>(data[0],data[1]),Integer.valueOf(data[2]));
            //   System.out.println("Edge was added.  start:"+data[0]+" end:"+data[1]+" weight:"+data[2]);
        }

        /**
         * Dijkstra part TODO
         */
        // System.out.println("vertex Count: "+g.getVertexCount());
        // System.out.println("edges Count: "+g.getEdgeCount());

        /***
         * Start node: 1
         * End node: 20
         */
        String startN="3";
        String endN="17";

        HashSet<String> setQ=new HashSet<>();  //create vertex set Q
        HashMap<String,Integer> dist=new  HashMap<String,Integer>();
        HashMap<String,String> prev=new HashMap<String,String>();


        for (Object vertex:g.getVertices()) {  //for each vertex v in Graph:
            dist.put(vertex.toString(),1000);//dist[v] ← INFINITY
            prev.put(vertex.toString(),null);//prev[v] ← UNDEFINED
            setQ.add(vertex.toString()); // add v to Q
        }

        dist.put(startN,0);//  dist[source] ← 0

        while (!setQ.isEmpty()){  //while Q is not empty:
            //getMinDistVertex zwraca node z całego dist TODO fix
            String node = Main.getMinDistVertex(dist,setQ); //u ← vertex in Q with min dist[u]
            setQ.remove(node); //remove u from Q

            HashSet<String> neighbours= new HashSet<String>();
            for(Pair key: edgeWeight.keySet()){
                if(key.getFirst().equals(node)){
                    neighbours.add((String) key.getSecond());
                }
            }
            System.out.println("Neighbours " + neighbours);

            for(String neighbour: neighbours){
                int alt= dist.get(node) + edgeWeight.get(new Pair(node,neighbour));
                if(alt<dist.get(neighbour)){
                    dist.put(neighbour,alt);
                    prev.put(neighbour,node);
                }

            }



        }//while end
        //return prev[], dist[]

        String u=endN;
        String sequence="";
        LinkedList<String> sequenceList=new LinkedList<>();


        while (!u.equals(null)||!prev.get(u).equals(null)){
            System.out.println(sequence);
            System.out.println(u);
            sequence=u+"  "+sequence;
            sequenceList.addFirst(u);
            u=prev.get(u);
            if(u.equals(startN)){
                sequenceList.addFirst(u);
                break;
            }
        }
        System.out.println(sequence);
        System.out.println(sequenceList);
        System.out.println("Length: "+Main.getRoadLength(edgeWeight,sequenceList));
        /**
         * make visable part
         */
//        VisualizationImageServer vs =
//                new VisualizationImageServer(
//                        new ISOMLayout(g), new Dimension(700, 700));
        VisualizationViewer vv=new VisualizationViewer<String,Number>(new ISOMLayout<String,Number>(g));

        //show edge labels
        vv.getRenderContext().setEdgeLabelTransformer(new Transformer<MyEdge, String>() {
            @Override
            public String transform(MyEdge o) {
                return (o.toString());
            }
        });
        //show node labels
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        //vs.getRenderContext().setVertexLabelRenderer(new DefaultVertexLabelRenderer(Color.cyan));




        JFrame frame = new JFrame("Dijkstra");

        frame.getContentPane().add(vv);

        //make zoom posible
        graphMouse = new DefaultModalGraphMouse();
        vv.setGraphMouse(graphMouse);
        modeBox = graphMouse.getModeComboBox();
        modeBox.addItemListener(graphMouse.getModeListener());
        graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        scaler = new CrossoverScalingControl();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    public static int getRoadLength(Hashtable<Pair<String>,Integer> edgeData,LinkedList<String> path){
        int length=0;
        for(int i=0;i<path.size()-1;i++){
            length+=edgeData.get(new Pair(path.get(i),path.get(i+1)));
        }
        return length;
    }


    public static String getMinDistVertex(HashMap<String, Integer> dist, HashSet<String> setQ ){

        HashMap<String, Integer> temp=new HashMap<String, Integer>();

        for(String key : dist.keySet()){
            if(setQ.contains(key)){
                System.out.println("Key: " + key + " dist.get(key) " + dist.get(key));
                temp.put(key,dist.get(key));
            }
        }
        Integer[] array=temp.values().toArray(new Integer[0]);
        Integer smallestValue = array[0];

        for(String key : temp.keySet()){
            if(temp.get(key)==smallestValue){
                return key;
            }
        }

        return null;
    }
}

