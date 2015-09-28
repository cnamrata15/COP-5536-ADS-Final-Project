import java.io.File;
import java.util.*;

public class ssp {
	
		public static void main(String[] args) throws Exception
		{
			//Filename source node and destination node taken as arguments respectively
			String filename = args[0];
			int source_node = Integer.parseInt(args[1]);
			int destination_node = Integer.parseInt(args[2]);
			Scanner in = new Scanner(new File(filename));
			String nl = in.nextLine();
			//File is scanned accordingly
			int numberOfNodes = Integer.parseInt(nl.split(" +")[0]);
			int numberOfEdges = Integer.parseInt(nl.split(" +")[1]);
			//graph drawn with nodes in file
			Graph myGraph = new Graph(numberOfNodes);
			while(in.hasNextLine())
			{	
				String line = in.nextLine();
				String[] graphInfo = line.split(" +");
				if(graphInfo.length == 3)
				{	
					//inputs assigned from graph
					int n1 = Integer.parseInt(graphInfo[0]);
					int n2 = Integer.parseInt(graphInfo[1]);
					Double weight = Double.parseDouble(graphInfo[2]);
					//details entered into the graph
					myGraph.insertNode(n1);
					myGraph.insertNode(n2);
					myGraph.addanEdge(n1, n2,weight);
				}
			}
			in.close();
			//To print the total weight
			Map<Integer, Dijkstra.PrintPath> getTotalWeight = Dijkstra.pathDisp(myGraph, source_node);
			Dijkstra.PrintPath result = getTotalWeight.get(destination_node);
			System.out.println(result.getWeight().intValue());
			
			//For printing the shortest path
			Map<Integer, Integer> DestToSource = result.getDestToSource();
			int present = destination_node;
			//We traverse a linked list from destination node to current node using child to parent links
			LinkedList<Integer> traverse = new LinkedList<Integer>();
			traverse.addFirst(destination_node);
			do{
				int temp = DestToSource.get(present);
				traverse.addFirst(temp);
				present = temp;
				} while (!(present==(source_node)));
			//Keep on printing till list is empty
			while (!traverse.isEmpty()) {
			System.out.print(traverse.remove() + " ");
		}
	}

}
