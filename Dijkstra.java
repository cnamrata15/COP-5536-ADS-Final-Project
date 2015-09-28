import java.util.*;
public final class Dijkstra {
    //Gives output Map
    public static Map<Integer, Double> sssPath(Graph graph, int source) 
    {
     
        FibonacciHeap dist = new FibonacciHeap();

        Map<Integer, FibonacciHeap.Element> elements = new HashMap<Integer, FibonacciHeap.Element>();
        //map from vertex to its dist
        Map<Integer, Double> cost = new HashMap<Integer, Double>();
        //intitially, distance from source is infinity for all vertices
        Set<Integer> allNodes = graph.getAllNodes();
        for(Integer node:allNodes)
        {
        	elements.put(node, dist.insertToheap(node, Double.POSITIVE_INFINITY));
        }
        //distance of source from itself is 0
        dist.decreaseKey(elements.get(source), 0.0);
        while (!dist.isEmpty()) 
        {
            FibonacciHeap.Element curr = dist.removeMin();
            cost.put(curr.getValue(), curr.getPriority());
            for (Map.Entry<Integer, Double> arc : graph.isEdge(curr.getValue()).entrySet()) 
            {
                if (cost.containsKey(arc.getKey())) continue;

                double pathCost = curr.getPriority() + arc.getValue();

                FibonacciHeap.Element dest = elements.get(arc.getKey());
                if (pathCost < dest.getPriority())
                    dist.decreaseKey(dest, pathCost);
            }
        }
        return cost;
    }
    
    public static class PrintPath {

		private Double weight;
	 
		private Map<Integer,Integer>  DestToSource ;
	
		private int nodeId;
	
		public PrintPath() {
		
		DestToSource = new HashMap<Integer,Integer>();
		}
	
		public Double getWeight() {
		return weight;
		}
		public void setWeight(Double weight) {
		this.weight = weight;
		}
		//This method is for getting the next hop router
		public int toGetNext()
		{
			
			int curr = nodeId;
			int prev = curr;
			int las = curr;
			LinkedList<Integer> lasToFir = new LinkedList<Integer>();
			//Current node is inserted at the beginning of list
			lasToFir.addFirst(curr);
			do {
				int tmp = DestToSource.get(curr);
				lasToFir.addFirst(tmp);
				las = prev;
				prev = curr;
				curr = tmp;
				} while (curr!=prev);
			return las;
		}
		
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "Vertext :" + getNodeId() + " Cost :"+ weight+ " Backtrack Map :"+DestToSource.toString();
		}
		
		public Map<Integer,Integer> getDestToSource() {
			return DestToSource;
		}

		public void setDestToSource(Map<Integer,Integer> DestToSource) {
			this.DestToSource = DestToSource;
		}

		public int getNodeId() {
			return nodeId;
		}

		public void setNodeId(int nodeId) {
			this.nodeId = nodeId;
		}
		
    }	
    //for printing the path taken
    public static Map<Integer, PrintPath> pathDisp(Graph graph, int source) 
    {	
    	StringBuilder show_path = new StringBuilder();
        FibonacciHeap path = new FibonacciHeap();
        Map<Integer, FibonacciHeap.Element> entries = new HashMap<Integer, FibonacciHeap.Element>();
        Map<Integer,PrintPath> pathMap = new HashMap<Integer,PrintPath>();
        Map<Integer,Integer>  pathPrevMap = new HashMap<Integer,Integer>();
        Map<Integer, Double> result = new HashMap<Integer, Double>();

  
        Set<Integer> allNodes = graph.getAllNodes();
        for(Integer node:allNodes)
        {
        	
            entries.put(node, path.insertToheap(node, Double.POSITIVE_INFINITY));
        }

        path.decreaseKey(entries.get(source), 0.0);
        pathPrevMap.put(source, source);
        while (!path.isEmpty()) 
        {
        
            FibonacciHeap.Element curr = path.removeMin();
            show_path.append(curr.getValue());
            result.put(curr.getValue(), curr.getPriority());
            PrintPath sresult = new PrintPath();
            sresult.setNodeId(curr.getValue());
            pathMap.put(curr.getValue(), sresult);
            sresult.setWeight(curr.getPriority());
            sresult.setDestToSource(pathPrevMap);
            //Update the edge priorities
            for (Map.Entry<Integer, Double> connect : graph.isEdge(curr.getValue()).entrySet()) 
            {
            	if (result.containsKey(connect.getKey())) continue;
            	
                //path cost with new connection
                double pathCost = curr.getPriority() + connect.getValue();
                
                //if path cost with new connection is less, keep it, else discard
                FibonacciHeap.Element dest = entries.get(connect.getKey());
                if (pathCost < dest.getPriority())
                {
                    path.decreaseKey(dest, pathCost);
                    pathPrevMap.put(dest.getValue(), curr.getValue());
                  }
            }
        }
        return pathMap;
    }
}