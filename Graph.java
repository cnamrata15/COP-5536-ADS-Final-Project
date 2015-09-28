import java.util.*;

public final class Graph {
	//Map from vertices to outgoing edges where edges themselves are maps from edge to doubles
    private final Map<Integer, Map<Integer, Double>> graph ;
    
    public Graph(int allNodes) {
    	graph = new HashMap<Integer, Map<Integer, Double>>(allNodes);
		// TODO Auto-generated constructor stub
	}

   //add a node
    public boolean insertNode(int node) {
        
        if (graph.containsKey(node))
            return false;
        graph.put(node, new HashMap<Integer, Double>());
        return true;
    }
    //add edge
    public void addanEdge(int start, int dest, double length) 
    {
        if (!graph.containsKey(start) || !graph.containsKey(dest))
            throw new NoSuchElementException("Check nodes");
        graph.get(start).put(dest, length);
        graph.get(dest).put(start, length);
    }
    //remove edge
    public void removeanEdge(int start, int dest) 
    {
       if (!graph.containsKey(start) || !graph.containsKey(dest))
            throw new NoSuchElementException("Check nodes");

        graph.get(start).remove(dest);
        graph.get(dest).remove(start);
    }
    @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	return "Total Keys : "+graph.size()+"\n Edges \n"+graph.toString();
    }
    //Map from node to its outgoing edges
    public Map<Integer, Double> isEdge(int node) 
    {
        
        Map<Integer, Double> incEdge = graph.get(node);
        if (incEdge == null)
            throw new NoSuchElementException("Check the node");
        return Collections.unmodifiableMap(incEdge);
    }
    //Iterator to go through nodes
    public Iterator<Integer> iterator() {
        return graph.keySet().iterator();
    }
    //returns all nodes
    public Set<Integer> getAllNodes(){
    	 return graph.keySet();
    }
}   
    
