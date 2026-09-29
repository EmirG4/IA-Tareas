package src;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception{
      

        Scanner sc = new Scanner(System.in);
        // Calcular tiempo y memoria inicial
        Runtime runtime = Runtime.getRuntime();



        String initialState = "7621 3458"; // Estado inicial del puzzle
        String goalState = "12345678 "; // Estado objetivo del puzzle
        SearchTree searchTree = new SearchTree(initialState, goalState);

        byte op=0;

        do{
            System.out.println("==== Menú ====");
            System.out.println("Método a elegir: ");
            System.out.println("1. Breadth First Search ");
            System.out.println("2. Deep First Search ");
            System.out.println("3. Uniform Cost Search ");
            System.out.println("4. Iterative Deepening Search ");
            System.out.println("0. Terminar ");
            op = sc.nextByte();

            if(op == 0) break;

            runtime.gc(); // Run garbage collector to get a more accurate memory usage
            // Calcular memoria inicial y tiempo inicial 
            long startMemory = runtime.totalMemory() - runtime.freeMemory();
            long startTime = System.currentTimeMillis();
            Node.totalNodesCreated = 0;

            switch(op){
                case 1: searchTree.breadthFirstSearch(); break;
                case 2: searchTree.deepFirstSearch(); break;
                case 3: searchTree.uniformCostSearch(); break;
                case 4: searchTree.iterativeDeepeningSearch(); break;
                default: break;
            }
        System.out.println("Initial State:" + initialState);
 
        //Calcular tiempo y memoria fina11l
        long endTime = System.currentTimeMillis();
        long endMemory = runtime.totalMemory() - runtime.freeMemory();
        // Calcular tiempo y memoria usados
        long timeUsed = endTime - startTime;
        long memoryUsed = endMemory - startMemory;

        System.out.println("=== Reporte de ejecución ===");
        System.out.println("Tiempo de ejecución: " + timeUsed + " ms");
        System.out.println("Memoria utilizada: " + memoryUsed + " bytes");
        System.out.println("Nodos creados: " + Node.totalNodesCreated);
        System.out.println("====================");     
        } while(!(op == 0));

    }
}
