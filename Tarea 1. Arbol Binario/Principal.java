/*By: Emir Gabino Pérez Villarreal
 */
public class Principal {
    public static void main(String[] args) {
        Arbol arbol = new Arbol();
        System.out.println("Vacio?: " + arbol.vacio());

        arbol.raiz = new Nodo("Carlos");
        arbol.raiz.izquierda = new Nodo("Ana");
        arbol.raiz.derecha = new Nodo("Luis");

        System.out.println("Vacio?: " + arbol.vacio());

        Nodo encontrado = arbol.buscarNodo("Ana");
        System.out.println("Encontrado: " + (encontrado != null ? encontrado.nombre : "No existe"));

    }
}