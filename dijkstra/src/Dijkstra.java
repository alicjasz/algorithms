import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

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

        System.out.println("Distance: ");
        for (Map.Entry<Integer, Integer> entry : distance.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }

        System.out.println("Prev: ");
        for (Map.Entry<Integer, Integer> entry1 : prev.entrySet()) {
            System.out.println(entry1.getKey() + " " + entry1.getValue());
        }

        System.out.println("Unvisited nodes: ");
        for (Map.Entry<Integer, Integer> entry2 : distance.entrySet()) {
            System.out.println(entry2.getKey() + " " + entry2.getValue());
        }

        List<Integer> neighbours = new ArrayList<>();
        while(!unvisitedNodes.isEmpty()) {
            int vertex = dijkstra.getMinDist(distance, unvisitedNodes);
            unvisitedNodes.remove(vertex);
            //System.out.println("UnvisitedNode size: " + unvisitedNodes.size());

            for (Edge e : edges) {
                if (e.getSource() == vertex) {
                    neighbours.add(e.getDestination());
                }
            }


            //System.out.println("Neighbours weights " + neighbours);
            for (Integer n : neighbours) {
                for(Edge e : edges){
                    if(e.getDestination() == n){
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

        int endPoint = 20;
        LinkedList<Integer> sequenceList=new LinkedList<>();

        while (String.valueOf(prev.get(endPoint)) != null || String.valueOf(endPoint) != null) {
            sequenceList.addFirst(endPoint);
            endPoint = prev.get(endPoint);
            if (endPoint == currentNode) {
                sequenceList.addFirst(endPoint);
                break;
            }
        }
        System.out.println("Sequence list: " + sequenceList);
    }

    public int getMinDist(Map<Integer, Integer> distance, HashSet<Integer> unvisitedNodes) {

        Map<Integer, Integer> temp_values = new HashMap<>();
        for(Map.Entry<Integer, Integer> entry : distance.entrySet()){
            if(unvisitedNodes.contains(entry.getKey())){
                temp_values.put(entry.getKey(), entry.getValue());
            }
        }

        Map.Entry<Integer, Integer> min = Collections.min(temp_values.entrySet(),
                Comparator.comparingDouble(Map.Entry::getValue));
        //System.out.println("getMinDist " + min.getKey());
        return min.getKey();
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
