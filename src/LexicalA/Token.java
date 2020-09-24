package LexicalA;

/**
 *
 * @author Jimena Valderrama
 */
public class Token {
    String lex;
    int id;
    String token;
    String type;
    int rep;

    public int getRep() {
        return rep;
    }

    public void setRep(int rep) {
        this.rep = rep;
    }
    
    public Token(String lex,  String token, int id, String type){
        this.lex = lex;
        this.id = id;
        this.token = token;
        this.type = type;
        rep =1;
    }
    //Token
    public String getLex(){
        return lex;
    }
    public int getId(){
        return id;
    }
    public String getToken(){
        return token;
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
    public void setToken(String token){
        this.token = token;
    }
    public void setType(String type){
        this.type = type;
    }

@Override    
    public String toString(){
        return " << " + lex + " >> | " + id + " | "+ String.valueOf(token)
           + " | " + String.valueOf(type)  + "  |  "+String.valueOf(rep);
        
    }
}
