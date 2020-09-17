
package LexicalA;

/**
 *
 * @author Jimena Valderrama
 */
public class SyntaxTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       LexicalAnalyzer an = new LexicalAnalyzer("src\\LexicalA\\TestProgram.txt");
       an.readFile();
        
       LlDriver ld = new LlDriver("src\\LexicalA\\TestProgram.txt");
      
    }
    
}
