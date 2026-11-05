package src;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class SearchTree {
    Node root;
    String goalState;
    String initialState;

    
    public SearchTree(String initialState, String goalState) {
        this.initialState = initialState;
        this.goalState = goalState;
        this.root = new Node(initialState, null);
    }

    // Búsqueda en anchura
    public void breadthFirstSearch() {
        System.out.println("===== Búsqueda en anchura =====");
        int time=0;
        int maxQueue = 0;
        // Crear estructura de datos para almacenar los nodos visitados
        Set<String> visited = new HashSet<String>();
        // 1. Buscar el nodo raíz y agregarlo a la cola
        Node currentNode = root;
        Queue<Node> queue = new LinkedList<>();
        queue.add(currentNode);
        visited.add(currentNode.getState());
        // 2. Mientras la cola no esté vacía, hacer lo siguiente:
        while (!queue.isEmpty()) {
            time++;
            // 3. Sacar el primer nodo de la cola y verificar si es el nodo objetivo
            currentNode = queue.poll();
            //System.out.println(NodeUtils.formatState(currentNode.getState()));
            if(currentNode.getState().equals(goalState)) {
                System.out.println("Goal state found: " + currentNode.getState());
                // print the path from root to goal
                printPath(currentNode);
                System.out.println("===== Búsqueda en anchura =====");
                System.out.println("Profundidad de la solución: " + currentNode.getDepth());
                break;
            }
            // 4. Si no es el nodo objetivo, generar sus hijos y agregarlos a la cola
            List<Node> children = NodeUtils.generateChildren(currentNode);
            for (Node child : children) {
                child.setDepth(currentNode.getDepth() + 1);
                if(!visited.contains(child.getState())){
                    visited.add(child.getState());
                    queue.add(child);
                }
            }
            maxQueue = Math.max(maxQueue, queue.size());
        }
        System.out.println("Time: " + time);
        System.out.println("Estados visitados: " + visited.size());
        System.out.println("Stack: " + queue.size());
        System.out.println("Max frontera: " + maxQueue);
    }
  
    // Búsqueda en profundidad
    public void deepFirstSearch() {
        System.out.println("===== Búsqueda en profundidad =====");
        int time=0;
        int maxStack = 0;
        // Crear estructura de datos para almacenar los nodos visitados
        Set<String> visited = new HashSet<String>();
        // 1. Buscar el nodo raíz y agregarlo a la cola
        Node currentNode = root;
        Stack<Node> stack = new Stack<>(); //Cambiar de cola 
        stack.push(currentNode);
        // 2. Mientras la cola no esté vacía, hacer lo siguiente:
        while (!stack.isEmpty()) {
            time++;
            // 3. Sacar el primer nodo de la cola y verificar si es el nodo objetivo
            currentNode = stack.pop();
            if (!visited.add(currentNode.getState())) 
               continue;
            //System.out.println(NodeUtils.formatState(currentNode.getState()));
            if(currentNode.getState().equals(goalState)) {
                System.out.println("Goal state found: " + currentNode.getState());
                // print the path from root to goal
                printPath(currentNode);
                System.out.println("===== Búsqueda en profundidad =====");                
                System.out.println("Profundidad de la solución: " + currentNode.getDepth());
                break;
            }
            // 4. Si no es el nodo objetivo, generar sus hijos y agregarlos a la cola
            List<Node> children = NodeUtils.generateChildren(currentNode);
            for (Node child : children) {
                child.setDepth(currentNode.getDepth() + 1);
                if (!visited.contains(child.getState()))
                    stack.push(child);
            }
            maxStack = Math.max(maxStack, stack.size());
        }

        System.out.println("Time: " + time);
        System.out.println("Estados visitados: " + visited.size());
        System.out.println("Stack: " + stack.size());
        System.out.println("Max frontera: " + maxStack);
    }

    // Búsqueda de costo uniforme
    public void uniformCostSearch(){
        System.out.println("===== Búsqueda de costo uniforme =====");
        int time=0;
        int maxQueue = 0;
        // Crear estructura de datos para almacenar los nodos visitados
        Set<String> visited = new HashSet<String>();
        // 1. Buscar el nodo raíz y agregarlo a la cola
        Node currentNode = root;
        PriorityQueue <Node> queue = new PriorityQueue<>(new NodePriorityComparator());
        queue.add(currentNode);
        visited.add(currentNode.getState()); 
        // 2. Mientras la cola no esté vacía, hacer lo siguiente:
        while (!queue.isEmpty()) {
            time++;
            // 3. Sacar el primer nodo de la cola y verificar si es el nodo objetivo
            currentNode = queue.poll();

            //System.out.println(NodeUtils.formatState(currentNode.getState()));
            if(currentNode.getState().equals(goalState)) {
                System.out.println("Goal state found: " + currentNode.getState());
                // print the path from root to goal
                printPath(currentNode);
                System.out.println("===== Búsqueda de costo uniforme =====");
                System.out.println("Profundidad de la solución: " + currentNode.getDepth());
                break;
            }
            // 4. Si no es el nodo objetivo, generar sus hijos y agregarlos a la cola
            List<Node> children = NodeUtils.generateChildren(currentNode);
            for (Node child : children) {
                child.setDepth(currentNode.getDepth() + 1);
                if (!visited.contains(child.getState())){
                    visited.add(child.getState());
                    child.setCost(child.getParent().getCost() + 1);
                    queue.add(child);
                }
            }
            maxQueue = Math.max(maxQueue, queue.size());
        }
        System.out.println("Time: " + time);
        System.out.println("Estados visitados: " + visited.size());
        System.out.println("Stack: " + queue.size());
        System.out.println("Max frontera: " + maxQueue);
    }

    public void printPath(Node node) {
        if (node == null) {
            return;
        }
        printPath(node.getParent());
        System.out.println(NodeUtils.formatState(node.getState()));
    }

    // Búsqueda en Profundidad Iterativa con Profundidad Limitada
    public void iterativeDeepeningSearch(){
        System.out.println("===== Búsqueda Iterativa con limitada =====");
        int maxDepthLimit = 0;
        int time = 0;
        int maxStack = 0;

        // Bucle de la prufundidad iterativa
        // Aumentará el límite infinitamente hasta encontrar el objetivo
        while(true){
            System.out.println("Buscando con límite de profundidad: " + maxDepthLimit);

            Stack<Node> stack = new Stack<>();

            root.setDepth(0);
            stack.push(root);

            while (!stack.isEmpty()) {
                time++;
                Node currentNode = stack.pop();
                
                // Verificamos si es la meta
                if (currentNode.getState().equals(goalState)) {
                    System.out.println("¡Goal state found!: " + currentNode.getState());
                    printPath(currentNode); // Imprime el camino bonito
                    System.out.println("===== Búsqueda Iterativa con limitada =====");
                    System.out.println("Profundidad de la solución: " + currentNode.getDepth());
                    System.out.println("Time (iteraciones totales): " + time);
                    System.out.println("Stack: " + stack.size());
                    System.out.println("Max frontera: " + maxStack);
                    return; 
                }
                
                // Restriccion de la profundidad limitada
                // Solo generamos hijos si la profundidad actual es menor al límite actual
                if (currentNode.getDepth() < maxDepthLimit) {
                    
                    List<Node> children = NodeUtils.generateChildren(currentNode);
                    
                    for (Node child : children) {
                        // El hijo debe saber su propia profundidad (la del padre + 1)
                        child.setDepth(currentNode.getDepth() + 1);
                        
                        // Si el estado no está ya en el camino que estamos explorando, va a la pila
                        if (!isStateInPath(currentNode, child.getState())) {
                            stack.push(child);
                        }
                    }
                    maxStack = Math.max(maxStack, stack.size());
                }
            } 
            // Si la pila se vació y no retornó (no encontró la meta),
            // significa que necesitamos buscar más profundo.
            maxDepthLimit++;
        }

    }
    
   // Valida si un estado ya existe en el camino que estamos explorando
    private boolean isStateInPath(Node parent, String stateToCheck) {
        Node current = parent;
        while (current != null) {
            if (current.getState().equals(stateToCheck)) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }


}
