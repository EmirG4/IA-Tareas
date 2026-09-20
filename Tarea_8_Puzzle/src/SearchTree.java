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

    /* TAREA, UNA VEZ TERMINADO, HAY QUE IMPRIMIR LAS COMPLEJIDADES de tiempo y espacio usado
    E IMPRIMIR ALGO BONITO COMO IMPRIMIR DESDE EL ESTADO INICIAL HASTA EL ESTADO META
    NECESITAMOS CREAR UN METODO DENTRO DE SearchTree */
    public void breadthFirstSearch() {
        int time=0;
        // Crear estructura de datos para almacenar los nodos visitados
        Set<String> visited = new HashSet<String>();
        // 1. Buscar el nodo raíz y agregarlo a la cola
        Node currentNode = root;
        Queue<Node> queue = new LinkedList<>();
        queue.add(currentNode);
        // 2. Mientras la cola no esté vacía, hacer lo siguiente:
        while (!queue.isEmpty()) {
            time++;
            // 3. Sacar el primer nodo de la cola y verificar si es el nodo objetivo
            currentNode = queue.poll();
            visited.add(currentNode.getState());
            //System.out.println(NodeUtils.formatState(currentNode.getState()));
            if(currentNode.getState().equals(goalState)) {
                System.out.println("Goal state found: " + currentNode.getState());
                // print the path from root to goal
                printPath(currentNode);
                break;
            }
            // 4. Si no es el nodo objetivo, generar sus hijos y agregarlos a la cola
            List<Node> children = NodeUtils.generateChildren(currentNode);
            for (Node child : children) {
                if (!visited.contains(child.getState()))
                    queue.add(child);
            }
        }
        System.out.println("Time: " + time);
        System.out.println("Estados visitados: " + visited.size());
        System.out.println("Stack: " + queue.size());
    }

    public void deepFirstSearch() {
        int time=0;
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
            visited.add(currentNode.getState());
            //System.out.println(NodeUtils.formatState(currentNode.getState()));
            if(currentNode.getState().equals(goalState)) {
                System.out.println("Goal state found: " + currentNode.getState());
                // print the path from root to goal
                printPath(currentNode);
                break;
            }
            // 4. Si no es el nodo objetivo, generar sus hijos y agregarlos a la cola
            List<Node> children = NodeUtils.generateChildren(currentNode);
            for (Node child : children) {
                if (!visited.contains(child.getState()))
                    stack.add(child);
            }
        }

        System.out.println("Time: " + time);
        System.out.println("Estados visitados: " + visited.size());
        System.out.println("Stack: " + stack.size());
    }

    public void UniformCostSearch(){
        int time=0;
        // Crear estructura de datos para almacenar los nodos visitados
        Set<String> visited = new HashSet<String>();
        // 1. Buscar el nodo raíz y agregarlo a la cola
        Node currentNode = root;
        PriorityQueue <Node> queue = new PriorityQueue<>(new NodePriorityComparator());
        queue.add(currentNode);
        // 2. Mientras la cola no esté vacía, hacer lo siguiente:
        while (!queue.isEmpty()) {
            time++;
            // 3. Sacar el primer nodo de la cola y verificar si es el nodo objetivo
            currentNode = queue.poll();
            visited.add(currentNode.getState());
            //System.out.println(NodeUtils.formatState(currentNode.getState()));
            if(currentNode.getState().equals(goalState)) {
                System.out.println("Goal state found: " + currentNode.getState());
                // print the path from root to goal
                printPath(currentNode);
                break;
            }
            // 4. Si no es el nodo objetivo, generar sus hijos y agregarlos a la cola
            List<Node> children = NodeUtils.generateChildren(currentNode);
            for (Node child : children) {
                if (!visited.contains(child.getState())){
                    child.setCost(child.getParent().getCost() + 1);
                    queue.add(child);
                }
            }
        }
        System.out.println("Time: " + time);
        System.out.println("Estados visitados: " + visited.size());
        System.out.println("Stack: " + queue.size());
    }

    public void printPath(Node node) {
        if (node == null) {
            return;
        }
        printPath(node.getParent());
        System.out.println(NodeUtils.formatState(node.getState()));
    }
    /* 
    public void breadthFirstSearch() {
        Node currentNode = root;
        Queue<Node> queue = new LinkedList<>();
        
        // HashSet para guardar los estados que ya visitamos y evitar ciclos infinitos
        Set<String> visitedStates = new HashSet<>(); 

        // 1. Buscar el nodo raiz y agregarlo a la cola y a los visitados
        queue.add(currentNode);
        visitedStates.add(currentNode.getState());

        // 2. Mientras la cola no esté vacía, hacer lo siguiente:
        while (!queue.isEmpty()) {
            
            // 3. Sacar el primer nodo de la cola
            currentNode = queue.poll();
            
            // Usamos getState() porque 'state' es private en Node.java
            if(currentNode.getState().equals(goalState)){
                System.out.println("¡Se encontró el estado objetivo!: " + currentNode.getState());
                return;
            }
            
            // 4. Si no es el nodo objetivo, genera sus hijos
            List<Node> children = NodeUtils.generateChildren(currentNode);
            
            for (Node child : children) {
                // Solo agregamos el hijo a la cola si NO lo hemos visitado antes
                if (!visitedStates.contains(child.getState())) {
                    queue.add(child);
                    visitedStates.add(child.getState());
                }
            }
        }
        
        System.out.println("No se encontró una solución.");
    }
    */
}
