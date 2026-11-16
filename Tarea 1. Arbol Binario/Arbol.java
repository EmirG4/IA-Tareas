import java.util.Stack;
/** 
 * 
 * Clase Arbol - 2 Métodos, primero metodo vacio() para comprobar el estado del arbol y
 * buscarNodo() para buscar un nodo teniendo el 'nombre', la busqueda es en preorden o sea
 * Raiz -> Izquierda -> Derecha
 * 
 * */ 
public class Arbol {
    Nodo raiz;

    public boolean vacio(){
        return raiz == null;
    }

    public Nodo buscarNodo(String nombre){
        if(raiz == null){
            return null;
        }

        Stack<Nodo> pila = new Stack<>();
        pila.push(raiz);

        //Busqueda en Preorden
        while(!pila.isEmpty()){
            Nodo actual = pila.pop();

            if(actual.nombre.equalsIgnoreCase(nombre)){
                return actual;
            }

            if(actual.derecha != null){
                pila.push(actual.derecha);

            }
            if(actual.izquierda != null){
                pila.push(actual.izquierda);
            }
        }
        return null;
    }

}
