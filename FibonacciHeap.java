import java.util.*;
public final class FibonacciHeap
{
    
    public static final class Element
    {
        private int degree = 0; 
        private boolean childCut = false; 
        private Element prevEle;
        private Element nextEle; 
        private Element isParent; 
        private Element isChild; 
        private int element; 
        private double elemPriority; 
        //sets Value of Element
        public void setValue(int value)
        {
            element = value;
        }
        //returns element
        public int getValue()
        {
            return element;
        }
        //returns priority of element
        public double getPriority()
        {
            return elemPriority;
        }

       //Constructor class holding element and priority
        private Element(int elem, double priority)
        {	element = elem;
            nextEle = prevEle = this;
            elemPriority = priority;
        }
    }
    //minimum element
    private Element minEle = null;
    private int heapSize = 0;

    //inserts elements into heap along with priority 
     public Element insertToheap(int value, double priority)
     {
        checkPriority(priority);
        //Insert single element and merge with heap
        Element result = new Element(value, priority);
        minEle = mergeLists(minEle, result);
        ++heapSize;
        return result;
     }

    //returns minimum element
    public Element min()
    {
        if (isEmpty()) throw new NoSuchElementException("Empty Fibonacci Heap");
        return minEle;
    }
    
    public boolean isEmpty()
    {
        return minEle == null;
    }
    
    public int size()
    {
        return heapSize;
    }

    //performs a remove min according to the rules stated in lecture.
    public Element removeMin()
    {
        if (isEmpty()) throw new NoSuchElementException("Empty Fibonacci Heap");

        --heapSize;
        Element minEntry = minEle;
        //removes min element from root list, considering all cases
        if (minEle.nextEle == minEle) 
        { 
            minEle = null;
        } 
        else 
        { 
            minEle.prevEle.nextEle = minEle.nextEle;
            minEle.nextEle.prevEle = minEle.prevEle;
            minEle = minEle.nextEle; 
        }

        //Remove parent fields from children of min element
        if (minEntry.isChild != null) 
        {
           Element curr = minEntry.isChild;
            do{
                curr.isParent = null;
                curr = curr.nextEle;
            }while(curr != minEntry.isChild);
        }
        minEle = mergeLists(minEle, minEntry.isChild);

        if (minEle == null) 
        	return minEntry;

        //Merge and remove trees of same degree using same rules taught in the lecture
        List<Element> treeList = new ArrayList<Element>();
        List<Element> toVisit = new ArrayList<Element>();
        for (Element curr = minEle; toVisit.isEmpty() || toVisit.get(0) != curr; curr = curr.nextEle)
            toVisit.add(curr);
        for (Element curr : toVisit) 
        {
           while (true) 
           {
                while (curr.degree >= treeList.size())
                    treeList.add(null);
                if (treeList.get(curr.degree) == null) {
                    treeList.set(curr.degree, curr);
                    break;
                }
                Element treeListEl = treeList.get(curr.degree);
                treeList.set(curr.degree, null); 
                //check min of the roots and merge accordingly
                Element min = (treeListEl.elemPriority < curr.elemPriority) ? treeListEl : curr;
                Element max = (treeListEl.elemPriority < curr.elemPriority) ? curr : treeListEl;
                //create new tree
                max.nextEle.prevEle = max.prevEle;
                max.prevEle.nextEle = max.nextEle;
                max.nextEle = max.prevEle = max;
                min.isChild = mergeLists(min.isChild, max);
                max.isParent = min;
                //set ChildCut value to False
                max.childCut = false;
                ++min.degree;
                curr = min;
            }

            //Update min
            if (curr.elemPriority <= minEle.elemPriority) minEle = curr;
        }
        return minEntry;
    }

    //Performs DecreaseKey operation according to the ways taught in class.
    public void decreaseKey(Element entry, double newPriority)
    {
        checkPriority(newPriority);
        if (newPriority > entry.elemPriority)
            throw new IllegalArgumentException("New priority exceeds old.");
        decKeyUncheck(entry, newPriority);
    }

    private void checkPriority(double priority)
    {
        if (Double.isNaN(priority))
            throw new IllegalArgumentException(priority + " is invalid.");
    }

    //merges two lists into one circular one considering all cases
    private static Element mergeLists(Element one, Element two)
    {
        
        if (one == null && two == null) //both null
        { 
            return null;
        } 
        else if (one != null && two == null) //second one null
        { 
            return one;
        } 
        else if (one == null && two != null) //first one null
        { 
            return two;
        } 
        else// both not null
        { 
            Element oneNext = one.nextEle; 
            one.nextEle = two.nextEle;
            one.nextEle.prevEle = one;
            two.nextEle = oneNext;
            two.nextEle.prevEle = two;
            return one.elemPriority < two.elemPriority ? one : two;
        }
    }

    
    private void decKeyUncheck(Element entry, double priority)
    {
        entry.elemPriority = priority;
        if (entry.isParent != null && entry.elemPriority <= entry.isParent.elemPriority)
            cutNode(entry);
        if (entry.elemPriority <= minEle.elemPriority) minEle = entry;
    }
    //cuts node from parent
    private void cutNode(Element ele)
    {
        ele.childCut = false;
        if (ele.isParent == null) return;
        if (ele.nextEle != ele) 
        { 
            ele.nextEle.prevEle = ele.prevEle;
            ele.prevEle.nextEle = ele.nextEle;
        }

        if (ele.isParent.isChild == ele) 
        {
            if (ele.nextEle != ele)
            {
                ele.isParent.isChild = ele.nextEle;
            }
            
            else 
            {
                ele.isParent.isChild = null;
            }
        }
        --ele.isParent.degree;

        ele.prevEle = ele.nextEle = ele;
        minEle = mergeLists(minEle, ele);

        if (ele.isParent.childCut)
            cutNode(ele.isParent);
        else
            ele.isParent.childCut = true;
        ele.isParent = null;
    }
}