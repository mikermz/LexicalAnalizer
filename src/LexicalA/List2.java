
package LexicalA;

/**
 *
 * @author Jimena Valderrama
 */
public class List2<T> {
    private Node<T> first, last;
    private int size;
    
    public int size(){
        return size;
    }
    
    public void addFirst(T element){
     Node<T> n = new Node<T>(element);
     if(first ==null){
         last = n;
     }else {
        n.next = first;
        }
        first = n;
        size++;
    }

    // Permite agregar un element al final de la lista
    public void addLast(T element) {
        Node<T> n = new Node<T>(element);
        if (first == null) {
            first = n;
        } else {
            last.next = n;
        }
        last = n;
        size++;
    }

    // Regresa el primer element
    public T getFirst() {
        if (first == null) {
            return null;
        }
        return first.info;
    }

    // Regresa el last element
    public T getLast() {
        if (first== null) {
            return null;
        }
        return last.info;
    }

    // Permite regresar un element de la lista en base a su ind
    public T getElement(int ind) {
        if (ind >= size || ind < 0) {
            return null;
        }
        Node<T> itd = first;
        for (int i = 0; i < ind; i++) {
            itd = itd.next;
        }
        return itd.info;
    }

    // Permite remove el primer element de la lista
    public void removeFirst() {
        if (first != null) {
            Node<T> remove = first;
            first = first.next;
            if (remove.equals(last)) {
                last = null;
            }
            size--;
        }
    }

    // Permite remove el last element de la lista
    public void removeLast() {
        if (first != null) {
            if (last.equals(first)) {
                first = null;
                last = null;
            } else {
                Node<T> itd = first;
                while (itd.next.next != null) {
                    itd = itd.next;
                }
                last = itd;
                itd.next = null;
            }
            size--;
        }
    }

    // Permite removeLast un element de la lista (si exists)
    public void remove(T element) {
        if (first == null); else if (first.info.equals(element)) {
            removeFirst();
        } else if (last.info.equals(element)) {
            removeLast();
        } else {
            Node<T> itd = first;
            while (itd.next != null) {
                T element_actual = itd.next.info;
                if (element_actual.equals(element)) {
                    Node<T> remove = itd.next;
                    itd.next = remove.next;
                    size--;
                    break;
                }
                itd = itd.next;
            }
        }
    }

    public boolean empty() {
        return size == 0;
    }

    // Permite saber si un element exists en la lista
    public boolean exists(T element) {
        boolean found = false;
        Node<T> itd = first;

        while (itd != null) {
            T element_actual = itd.info;
            if (element_actual.equals(element)) {
                found = true;
                break;
            }
            itd = itd.next;
        }
        return found;
    }

    private class Node<T> {
        T info;
        Node<T> next;

        Node(T info) {
            this.info = info;
        }
    }
}
