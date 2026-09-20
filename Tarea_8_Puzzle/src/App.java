package src;

import java.util.List;
import java.util.PriorityQueue;

public class App {
    public static void main(String[] args) throws Exception{
/*       
        // Calcular tiempo y memoria inicial
        Runtime runtime = Runtime.getRuntime();
        runtime.gc(); // Run garbage collector to get a more accurate memory usage
        // Calcular memoria inicial y tiempo inicial 
        long startMemory = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.currentTimeMillis();
*/

        String initialState = "7621 3458"; // Estado inicial del puzzle
        String goalState = "12345678 "; // Estado objetivo del puzzle
        SearchTree searchTree = new SearchTree(initialState, goalState);
        //searchTree.breadthFirstSearch();
        //searchTree.deepFirstSearch();
        searchTree.UniformCostSearch();
        System.out.println("End");

        System.out.println("Initial State:" + initialState);

        PriorityQueue<Node> queue = new PriorityQueue<>(new NodePriorityComparator());
        Node n1 = new Node("n1", null);
        n1.setCost(5);

        Node n2 = new Node("n2", null);
        n2.setCost(3);

        Node n3 = new Node("n3", null);
        n3.setCost(7);

        Node n4 = new Node("n4", null);
        n4.setCost(2);

        queue.add(n1); // 5
        queue.add(n2); // 3
        queue.add(n3); // 7
        queue.add(n4); // 2 

        while(!queue.isEmpty()){
            Node node = queue.poll();
            System.out.println(node.getState() + " - Cost: " + node.getCost());
        }


        //List<Node> children = NodeUtils.generateChildren(new Node(initialState, null));
        /*for (Node node : children) {
            System.out.println(node.getState());
        }*/
/* 
        //Calcular tiempo y memoria final
        long endTime = System.currentTimeMillis();
        long endMemory = runtime.totalMemory() - runtime.freeMemory();
        // Calcular tiempo y memoria usados
        long timeUsed = endTime - startTime;
        long memoryUsed = endMemory - startMemory;

        System.out.println("=== Reporte de ejecución ===");
        System.out.println("Tiempo de ejecución: " + timeUsed + " ms");
        System.out.println("Memoria utilizada: " + memoryUsed + " bytes");
        System.out.println("Nodos creados: " + Node.totalNodesCreated);
*/
    }
}
