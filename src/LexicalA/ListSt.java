package LexicalA;

/**
 *
 * @author Jimena Valderrama
 */
public class ListSt {

    String type;
    Node start, end;

    public ListSt() {
        this.type = null;
        this.start = null;
        this.end = null;
    }

    public ListSt(String type) {
        start = null;
        end = null;
        this.type = type;
    }

    public int size() {
        Node nd;
        nd = start;
        int ct = 0;
        while (nd != null) {
            ct++;
            nd = nd.getNext();
        }
        return ct;
    }

    public void saveUp(Token token) {
        Node n = new Node(token);
        if (start == null) {
            start = n;
            end = n;
        } else {
            end.setNext(n);
            end = n;
        }
    }

    public void show() {
        Node nd;
        nd = start;
        System.out.println(this.type);
        while (nd != null) {
            String s = nd.toString();
            System.out.println(s);
            nd = nd.getNext();
        }

    }
    public Token getTokenbyId(int id){
        Node nd=start;
        while(nd!=null){
            if (nd.getToken().id == id) {
                return nd.getToken();
            }
            nd=nd.getNext();
        }
        return null;
        
    }

    public boolean exists(Token itemId) {
        Node nd;
        nd = start;
        while (nd != null) {
            if (nd.getToken().id == itemId.id) {
                return true;
            }
            nd=nd.getNext();
        }
        return false;
    }

}
