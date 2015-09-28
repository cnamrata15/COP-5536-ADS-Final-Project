import java.util.*;

class Node 
{
	//class for defining nodes of a binary trie
	int count;
	private String Key;
	private String valueOfNode;
	char isPresent; 
	private String prefMatch;
	private boolean end; 
	Node left;
    Node right;
    private Node parent;
	
    //returns left child
    public Node getLeftChild() 
    {
			return left;
	}
    //returns right child
	public Node getRightChild() 
	{
		return right;
	}
	//sets value of left child
	 public void setLeftChild(Node leftChild) 
	 {
			this.left = leftChild;
	 }
	//sets value of right child
	public void setRightChild(Node rightChild) 
	{
		this.right = rightChild;
	}
	public Node(char c,String key,String value)
    {
		count = 0;
		Key = key;
		valueOfNode = value;
		isPresent = c;
		prefMatch = "";
		setEnd(true);
        left = null;
        right = null;
      }  
    public Node getsChildNode(char choice)
    {
    		//function to return left or right child as required
    		switch (choice) 
    		{
    			case '0' : return left;
    			case '1' : return right;
    		}
    		
    		if(choice=='0')
                 return left;
    		else 
    		
    			return right;
    }
    //To add a child node
    	public void insertChild(char choice, String key, String value)
    	{
    	Node newChild = new Node(choice,key,value);
    	newChild.putParent(this);
    	newChild.setPrefMatch(prefMatch+choice);
    	if(choice=='0')
    	{
    		this.left =newChild;
    	}
    	else
    	{
    		this.right = newChild;
    	}
    	if(this.End()==true )
    	{
    	
    		this.setEnd(false);
    	}
    		
    }
    public String getNodeValue() 
    {
    		return valueOfNode;
    }
    public String getNodeKey() {
		return Key;
	}
	public void setkey(String key) {
		this.Key = key;
	}
    public void putParent(Node parent) {
		this.parent = parent;
	}
    public Node returnParent() 
    {
		return parent;
	}
	public void setVal(String nodeValue)
	{
		this.valueOfNode = nodeValue;
	}
	public boolean End() 
	{
		return end;
	}
	public void setPrefMatch(String matPref) 
	{
		this.prefMatch = matPref;
	}
	public String getPrefMatch() 
	{
		return prefMatch;
	}
	public void setEnd(boolean isEnd) 
	{
		this.end = isEnd;
	}
}

//Class to create the operations performed in a binary trie
	public class BinaryTrie
	{
		private Node trieRoot;
		public BinaryTrie()
		{
			trieRoot = new Node(' ',"ROOT","ROOT"); 
        
		}
		//To insert a word
		public void insertWord(String key,String value)
		{	
			boolean putFlag = false;
			Node nodeOld = null;
			if (toSear(key) == true) 
				return;        
			StringBuilder prefixAlreadyMatched = new StringBuilder();
        
			Node current = trieRoot; 
			for (char ch : key.toCharArray() )
			{
				prefixAlreadyMatched.append(ch);
				Node newChild = current.getsChildNode(ch);
				if (newChild != null)
				{
					if(!newChild.End())
					{
						current = newChild;
					}
					else
					{
						nodeOld = newChild;
						putFlag=true;
						current = current.getsChildNode(ch);
						current.insertChild(ch,key,value);
						current.setVal("");
					}
                
				}
				else 
				{
					current.insertChild(ch,key,value);
					current = current.getsChildNode(ch);
					break;
				}
				current.count++;
			}//end for
			
			//checks if end of current is true
			current.setEnd(true);
			if("".equalsIgnoreCase(current.getNodeValue()))
				current.setVal(value);
			if(putFlag==true){
				insertWord(nodeOld.getNodeKey(),nodeOld.getNodeValue());
			}
		}
    
		//For merging using post order traversal
		//This function helps to merge
		private Node doPostMerge(Node node) 
		{
			if(node==null || node.End())
			{
				return node;
			}
			Node leftChild = doPostMerge(node.getLeftChild());
			Node rightChild = doPostMerge(node.getRightChild());
			//checks all the conditions and sets values accordingly
			if(leftChild!=null && rightChild!= null && leftChild.End() && rightChild.End() && leftChild.getNodeValue().equalsIgnoreCase(rightChild.getNodeValue()))
			{
				node.setVal(rightChild.getNodeValue());
				node.setLeftChild(null);
				node.setRightChild(null);
				node.setEnd(true);
			}
			//if the right child is null and the left is not, delete left child, put the value in its parent
			else if(leftChild!= null && rightChild == null && leftChild.End())
			{
				node.setVal(leftChild.getNodeValue());
				node.setLeftChild(null);
				node.setEnd(true);
			}
			//similarly delete right child in the opposite case
			else if(leftChild == null && rightChild != null && rightChild.End()){
				node.setVal(rightChild.getNodeValue());
				node.setLeftChild(null);
				node.setRightChild(null);
				node.setEnd(true);
			}
			return node;
		}
		public BinaryTrie mergePostOrder()
		{
			doPostMerge(trieRoot);
			return this;
		}
    //To get the matched prefix
		public Node toGetMatPref(String word)
		{
			Node present = trieRoot;  
			for (char ch : word.toCharArray() )
			{
				if (present.getsChildNode(ch) == null)
					return present;
				else
					present = present.getsChildNode(ch);
			}      
			return present;
        }
    
		//This function is used for searching a word in the trie
		public boolean toSear(String word)
		{
			Node current = trieRoot;  
			for (char ch : word.toCharArray() )
			{
				if (current.getsChildNode(ch) == null)
					return false;
				else
					current = current.getsChildNode(ch);
			}      
			if (current.End() == true) 
				return true;
			return false;
		}
	}
   
    
       
 