package LexicalA;

/**
 *
 * @author Jimena Valderrama
 */
public class LlDriver {

    private LexicalAnalyzer lex;
    private Grammar2 grammar;
    private Stack<String> stack;
    private int[][] matrix;
    private String x, a;

    public LlDriver(String path) {
        lex = new LexicalAnalyzer(path);
        grammar = new Grammar2();
        System.out.println("--------------------------------");
        matrix = new int[][]{
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 3, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 6, 0, 0, 6, 0, 6, 6, 0, 0, 0},
            {0, 0, 0, 0, 0, 8, 0, 8, 0, 0, 7, 7, 7},
            {0, 0, 0, 10, 0, 0, 9, 0, 11, 12, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 13, 14, 15},};

        process();
    }

    private void process() {
        stack = new Stack<String>();

        stack.push(grammar.initialSymbol());
        x = stack.top();

        a = lex.getToken();

        while (!stack.isEmpty()) {
            if (isNoTerminal(x)) {
                int predic = pre(x, a);
                if (predic != 0) {

                    replacePro(predic);
                    x = stack.top();
                } else {
                    processError();
                    

                }
            } else {
                if (x.equals(a)) {
                    stack.pop();
                    x = stack.top();

                    System.out.println(x + " " + " " +a);
                    
                    a = lex.getToken();
                } else {
                    processError();
       
                }
            }
        }
        System.out.println();
        System.out.println("*** Analysis finalized ***");
    }

    private boolean isNoTerminal(String token) {
        String[] noTerm = grammar.symbolsNoTerminal();

        for (int i = 0; i < noTerm.length; i++) {
            if (token.equals(noTerm[i])) {

                return true;
            }
        }

        return false;
    }

    private int pre(String x, String a) {
        int index_x = -1, index_a = -1;
        String[] noTerm = grammar.symbolsNoTerminal();
        for (int i = 0; i < noTerm.length; i++) {
            if (x.equals(noTerm[i])) {

                index_x = i;
                break;
            }
        }

        String[] terminals = grammar.symbolsTerminal();
        for (int i = 0; i < terminals.length; i++) {
            if (a.equals(terminals[i])) {
                index_a = i;
                break;
            }
        }

        if (index_a == -1) {
            return 0;
        }
        return matrix[index_x][index_a];
    }

    private void replacePro(int indexP) {
        stack.pop();
        String[][] productions = grammar.productions();
        String[] production = productions[indexP - 1];
        for (int i = production.length - 1; i > -1; i--) {
            if (!production[i].equals("")) {
                stack.push(production[i]);
            }
        }
    }

    private void processError() {
        System.out.print("Syntax error: ");
        System.out.print("Expecting: '" + x + "' , found: " + a);
        System.out.println("\t");
        System.out.println("Couldn't continue. Error found.");
        System.exit(0);
        //realiza el paso como si hubiera encontrado coincidencias con x y a 
        //para poder procesar el siguiente token
        System.out.println();
        stack.pop();

        x = stack.top();

        a = lex.getToken();

    }
}
