package src;

import java.util.Comparator;

public class NodePriorityComparator implements Comparator<Node>{

    @Override
    public int compare(Node n1, Node n2) {
        // TODO Auto-generated method stub
        return Integer.compare(n1.getCost(), n2.getCost());
    }
    
}
