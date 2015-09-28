import java.io.File;
import java.util.*;

public class routing {
	
	public static void main(String[] args) throws Exception
	{	//Graph Filename, IP FileName, source node and destination node taken as arguments respectively
		String graphInput = args[0];
		String IPinput = args[1];
		int source_node = Integer.parseInt(args[2]);
		int dest_node = Integer.parseInt(args[3]);

		Scanner in = new Scanner(new File(graphInput));
		String nl = in.nextLine();
		//File is scanned accordingly
		int allNodes = Integer.parseInt(nl.split(" +")[0]);
		int allEdges = Integer.parseInt(nl.split(" +")[1]);
		//graph drawn with nodes in file
		Graph graph = new Graph(allNodes);
		while(in.hasNextLine())
		{
				String[] graphInfo = in.nextLine().split(" +");
				if(graphInfo.length == 3)
				{	
					//inputs assigned from graph
					int n1 = Integer.parseInt(graphInfo[0]);
					int n2 = Integer.parseInt(graphInfo[1]);
					Double weight = Double.parseDouble( graphInfo[2]);
					//details entered into the graph
					graph.insertNode(n1);
					graph.insertNode(n2);
					graph.addanEdge(n1, n2,weight);
			}
		}
		in.close();
		Scanner IPin = new Scanner(new File(IPinput));
		int count =0;
		HashMap<String,String> MapRouterIPs = new HashMap<String,String>();
		while(IPin.hasNext())
		{
			String ip = IPin.next();
			String routerStr =""+count;
			MapRouterIPs.put(routerStr, ip);
			++count;
		}
		IPin.close();
		
		//To print the least weight
		Map<Integer, Dijkstra.PrintPath> getTotalWeight = Dijkstra.pathDisp(graph, source_node);
		System.out.println(getTotalWeight.get(dest_node).getWeight().intValue());
    
    
    //For each router R in the network, call ssp implemented in Part 1 to obtain
    //shortest  path from R to each destination router  Y. To . This gives you a set of pairs  <IP address of Y, next-hop router Z>. Insert these pairs into a
    //binary trie. 
    
		//Create router table 
	   	HashMap<String,HashMap<String,String>> router_table = new HashMap<String, HashMap<String,String>>();
	   	
	   	
		//Create Binary trie at each router
		HashMap<String,BinaryTrie> binTrieForRouter = new HashMap<String, BinaryTrie>();
		Map<String,String> vertextIPMap = new HashMap<String,String>();
		Iterator<Integer> visitVertexOne = graph.iterator();
		while(visitVertexOne.hasNext())
		{
			String first_router = visitVertexOne.next().toString();
			HashMap<String,String> firstRouterTable = new HashMap<String,String>();
			BinaryTrie binaryTrie = new BinaryTrie();
			Map<Integer, Dijkstra.PrintPath> shortestPath_First_Router = Dijkstra.pathDisp(graph, Integer.parseInt(first_router));
			vertextIPMap.put(first_router,  getRouOneTwo(MapRouterIPs.get(first_router)));
			Iterator<Integer> visitVertexTwo = graph.iterator();
			while(visitVertexTwo.hasNext())
			{
				String toNextRouter = visitVertexTwo.next().toString();
					if(toNextRouter.equalsIgnoreCase(first_router))
						continue;
					Dijkstra.PrintPath path = shortestPath_First_Router.get(Integer.parseInt(toNextRouter));
					String nextRouIPad = MapRouterIPs.get(toNextRouter);
					String tonextHop = path.toGetNext()+"";
					String rouOnetoTwo = getRouOneTwo(nextRouIPad);
					firstRouterTable.put(rouOnetoTwo, tonextHop);
					binaryTrie.insertWord(rouOnetoTwo, tonextHop);
					
				}
			
				binaryTrie.mergePostOrder();
				router_table.put(first_router, firstRouterTable);	
				binTrieForRouter.put(first_router,binaryTrie);
				
			}
			String destinationIP = vertextIPMap.get(dest_node+"");
			int nodeSor = source_node;
			while(!(nodeSor==(dest_node))){
			BinaryTrie trie = binTrieForRouter.get(nodeSor+"").mergePostOrder();
			String getSour = trie.toGetMatPref(destinationIP+"").getPrefMatch();
			String hopToNext =  trie.toGetMatPref(destinationIP+"").getNodeValue();
			System.out.print(getSour+" ");
			nodeSor = Integer.parseInt(hopToNext);
		}//end while loop
	
	}//end main
	
	// Functions for finding sets of IP address pairs 
	public static String toGetIP(int num)
	{
		String addressOf = Integer.toBinaryString(num);
		int length = 8 - addressOf.length();
		char[] arrayFill = new char[length];
		Arrays.fill(arrayFill, '0');
		String fillString = new String(arrayFill);
		addressOf = fillString + addressOf;
		return addressOf;
	}
	public static String getRouOneTwo(String destIP){
		
		String[] arrNew = destIP.split("\\.");
		assert arrNew.length == 4;
		byte[] lenOfIP = new byte[4];
		StringBuilder findIP = new StringBuilder();
		for (int i = 0; i < 4; ++i) 
		{
			lenOfIP[i] = Integer.valueOf(arrNew[i]).byteValue();
		    Integer nl =  Integer.parseInt(arrNew[i]);
		    findIP.append(toGetIP(nl));
		}
		return findIP.toString();
	}
	
}
