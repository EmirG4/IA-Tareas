package src;
import java.util.ArrayList;
import java.util.List;

public class NodeUtils {

    private static String swapPositions(String state, int pos1, int pos2) {
        char[] arr = state.toCharArray();
        char temp = arr[pos1];
        arr[pos1] = arr[pos2];
        arr[pos2] = temp;
        return new String(arr);
    }

    public static List<Node> generateChildren(Node parentNode) {
        
        /*
         1 2 3        1 2 3      1 2 3
         4 5 6   =>     5 6  +   4 5 6
           7 8        4 7 8      7   8

        "123456 78" => ["123 56478", "1234567 8"]
         */

        List<Node> successors = new ArrayList<>();
        int zeroPos = parentNode.getState().indexOf(" ");

        // Adjacency map: for each position, which positions can it swap with
        int[][] adjacentPositions = {
            {1, 3},           // pos 0: can swap with right(1), down(3)
            {0, 2, 4},        // pos 1: can swap with left(0), right(2), down(4)
            {1, 5},           // pos 2: can swap with left(1), down(5)
            {0, 4, 6},        // pos 3: can swap with up(0), right(4), down(6)
            {1, 3, 5, 7},     // pos 4: can swap with up(1), left(3), right(5), down(7)
            {2, 4, 8},        // pos 5: can swap with up(2), left(4), down(8)
            {3, 7},           // pos 6: can swap with up(3), right(7)
            {4, 6, 8},        // pos 7: can swap with up(4), left(6), right(8)
            {5, 7}            // pos 8: can swap with up(5), left(7)
        };

        for (int adjPos : adjacentPositions[zeroPos]) {
            String newState = swapPositions(parentNode.getState(), zeroPos, adjPos);
            successors.add(new Node(newState, parentNode));
        }

        return successors;
    }

    public static String formatState(String state){
        String formattedState = "";
        for(int i = 0; i < state.length(); i++){
            formattedState += state.charAt(i) + " ";
            if((i + 1) % 3 == 0)
                formattedState += "\n";  
        }
        return formattedState;
    }
}
 