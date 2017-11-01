import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Dijkstra {

    public Map<Integer, Integer> distance;
    public Map<Integer, Integer> prev;
    public HashSet<Integer> visitedNodes;
    public HashSet<Integer> unvisitedNodes;


    public static List<String> parse_file(String file_name) throws IOException {

        File file = new File(file_name);
        String content = "";
        List<String> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            content = sb.toString();
        }

        String[] s1 = content.split("[\r\n]+");
        for(int i = 0; i < s1.length; i++){
            data.add(s1[i]);
        }
        return data;
    }

    public static List<Edge> stringToEdge(List<String> lista){

        List<Edge> edges = new ArrayList<>();
        for(int i = 0; i < lista.size(); i++){
            String[] data = lista.get(i).split("; ");
            Edge e = new Edge();
            e.setSource(Integer.parseInt(data[0]));
            e.setDestination(Integer.parseInt(data[1]));
            e.setWeight(Integer.parseInt(data[2]));
            edges.add(e);
        }
        return edges;
    }

    public void dijkstra_algorithm(List<Edge> edges) {

        Dijkstra dijkstra = new Dijkstra();
        visitedNodes = new HashSet<>();
        unvisitedNodes = new HashSet<>();
        distance = new HashMap<>();
        prev = new HashMap<>();
        int currentNode = 1; // the initial node as well
        int endPoint = 20;

        // Creating graph
        DirectedSparseMultigraph graph = new DirectedSparseMultigraph();
        for(Edge e : edges){
            if(!graph.containsVertex(e.getSource())){
                graph.addVertex(e.getSource());
            }
            if(!graph.containsVertex(e.getDestination())){
                graph.addVertex(e.getDestination());
            }
            graph.addEdge(e, e.getSource(), e.getDestination(), EdgeType.DIRECTED);
        }


        for(Object v : graph.getVertices()){
            distance.put(Integer.parseInt(v.toString()), Integer.MAX_VALUE);
            prev.put(Integer.parseInt(v.toString()), -1);
            unvisitedNodes.add(Integer.parseInt(v.toString()));
        }

        distance.put(currentNode, 0);

        while(!unvisitedNodes.isEmpty()) {

            List<Integer> neighbours = new ArrayList<>();

            Map<Integer, Integer> temp_values = new HashMap<>();
            for(Map.Entry<Integer, Integer> entry : distance.entrySet()){
                if(unvisitedNodes.contains(entry.getKey())){
                    temp_values.put(entry.getKey(), entry.getValue());
                }
            }

            Map.Entry<Integer, Integer> min = Collections.min(temp_values.entrySet(),
                    Comparator.comparingDouble(Map.Entry::getValue));

            int vertex = min.getKey();
            unvisitedNodes.remove(vertex);

            for (Edge e : edges) {
                if (e.getSource() == vertex) {
                    neighbours.add(e.getDestination());
                }
            }

            /*System.out.println("For vertex " + vertex);
            System.out.println(neighbours);*/

            for (Edge e : edges) {
                for (Integer n : neighbours) {
                    if (e.getDestination() == n && e.getSource() == vertex) {
                        int alt = distance.get(vertex) + e.getWeight();
                        //System.out.println(alt);
                        if (alt < distance.get(n)) {
                            distance.put(n, alt);
                            prev.put(n, vertex);
                        }
                    }
                }
            }
        }

        List<Integer> path = new ArrayList<>();
        path.add(endPoint);
        while(prev.get(endPoint) != currentNode){
            path.add(prev.get(endPoint));
            endPoint = prev.get(endPoint);
            if(prev.get(endPoint) == currentNode){
                path.add(currentNode);
            }
        }

        System.out.println("The shortest path between source and target: " + distance.get(distance.size()));
        System.out.println("Path between source and target: " + path);

        VisualizationViewer vv=new VisualizationViewer<String, Number>(new ISOMLayout<String, Number>(graph));
        vv.getRenderContext().setEdgeLabelTransformer((Transformer<Edge, String>) o -> (o.toString()));
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        JFrame frame = new JFrame("Dijkstra");

        frame.getContentPane().add(vv);

        DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
        graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
        vv.setGraphMouse(graphMouse);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    public static void main(String[] args){

        List<String> lista;
        List<Edge> edges;
        Dijkstra dijkstra = new Dijkstra();
        try {
            lista = parse_file("input.txt");
            edges = stringToEdge(lista);
            dijkstra.dijkstra_algorithm(edges);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
