
package LexicalA;

/**
 *
 * @author Jimena Valderrama
 */
public class Token {
    String lex;
    int id;
    String type;
    
    public Token(String lex,  String type, int id){
        this.lex = lex;
        this.id = id;
        this.type = type;
    }
    //Token
    public String getLex(){
        return lex;
    }
    public int getId(){
        return id;
    }
    public String getType(){
        return type;
    }
    public void setLex(String lex){
        this.lex = lex;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setType(String type){
        this.type = type;
    }

@Override    
    public String toString(){
        return " << " + lex + " >> ID:" + id + " -> "+ String.valueOf(type);
        
    }
}
