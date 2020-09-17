package LexicalA;

/**
 *
 * @author Jimena Valderrama
 */
public class Stack<T> {

    private Nodo<T> top;

    public boolean isEmpty() {
        if (top == null) {
            return true;
        }
        return false;
    }

    public void push(T element) {
        Nodo<T> nodo = new Nodo<T>(element);
        nodo.next = top;
        top = nodo;
    }

    public T pop() {
        if (top == null) {
            return null;
        }
        Nodo<T> nodo = top;
        T element = nodo.getElement();

        top = top.next;
        nodo = null;
        return element;
    }

    public T top() {
        if (top == null) {
            return null;
        }
        return top.getElement();
    }
}

class Nodo<T> {

    Nodo<T> next;
    private T element;

    public Nodo(T element) {
        this.element = element;
    }

    public T getElement() {
        return element;
    }
}
