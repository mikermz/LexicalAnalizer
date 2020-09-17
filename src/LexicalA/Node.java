
package LexicalA;


public class Node {
   Token token;
   Node next;
   
   public Node(Token token){
       this.token = token;
       next= null;
   }
   
   public Token getToken(){
       return token;
   }
   public Node getNext(){
       return next;
   }
   public void setToken(Token token){
       this.token = token;
   }
   public void setNext(Node next){
       this.next= next;
   }
   
   @Override
   public String toString(){
      return this.token.toString();
   }
}
