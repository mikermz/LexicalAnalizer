/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LexicalA;

/**
 *
 * @author Jimena Valderrama
 */
public class Grammar2 {

    private String[] terminal,
            noTerminal;
    private String[][] productions;
    private String initialSymbol;
    private ReadFiles file = new ReadFiles();
    private List2<String> noTerm;
    private List2<String> term;
    private List2<String[]> produ;
    LexicalAnalyzer lex;

    public Grammar2() {
        file.read("src\\LexicalA\\GramaticaN.txt");
//        System.out.println(" << Grammar >>");
//        show();
//        System.out.println("");
        System.out.println(" << No terminal >>");
        noTerminal = getNoTerminal();
        System.out.println("");
        System.out.println(" << Productions >>");
        productions = getProductions();
        System.out.println("");
        System.out.println(" << Terminal >>");
        terminal = getTerminal();
        System.out.println("");

        initialSymbol = getInitialSymbol();

    }

    private String leftSide(String content) {
        int index = 0;
        char state = 'i';
        String r = "";

        while (index < content.length()) {
            char ch = content.charAt(index);
            switch (state) {
                case 'i':
                    if (ch != '-' && ch != ' ' && ch != '\t') {
                        state = 'n';
                    } else {
                        state = 'e';
                    }
                    break;
                case 'n':
                    if (ch == '-') {
                        state = '-';
                    } else if (ch == ' ' || ch == '\t') {
                        state = 's';
                    } else {
                        state = 'n';
                    }
                    break;
                case '-':
                    if (ch == ' ' || ch == '\t') {
                        state = 'e';
                    } else if (ch == '>') {
                        state = '>';
                    } else {
                        state = 'n';
                    }
                    break;
                case 's':
                    if (ch == ' ' || ch == '\t') {
                        state = 's';
                    } else if (ch == '-') {
                        state = 't';
                    } else {
                        state = 'e';
                    }
                    break;
                case 't':
                    if (ch == '>') {
                        state = '>';
                    } else {
                        state = 'e';
                    }
                    break;
            }

            if (state == 'e') {
                return null;
            }
            if (ch != ' ' && ch != '\t') {
                r += ch;
            }
            if (state == '>') {
                r = r.substring(0, r.length() - 2);
                return r;

            }
            index++;
        }
        return null;
    }

    private String[] rightSide(String content) {
        List2<String> productions = new List2<String>();
        String symbol = "";
        int index = 0;
        char state = 'i';
        String r = "";

        while (index < content.length()) {
            char ch = content.charAt(index);
            switch (state) {
                case 'i':
                    if (ch == '-') {
                        state = '-';
                    }
                    break;
                case '-':
                    if (ch == '>') {
                        state = '>';
                    }
                    break;
                case '>':
                    if (ch == ' ' || ch == '\t') {
                        if (index == content.length() - 1) {
                            if (productions.size() == 0) {
                                System.out.println("  ");
                                return new String[]{""};
                            }
                        }
                    } else {
                        symbol += ch;
                        state = 'n';
                    }
                    break;
                case 'n':
                    if (ch == ' ' || ch == '\t') {
                        productions.addLast(symbol);
                        symbol = "";
                        state = '>';
                    } else {
                        symbol += ch;
                    }
                    break;
            }
            index++;
        }
        if (state == 'n') {
            productions.addLast(symbol);
        }
        if (productions.size() == 0) {
            return new String[]{""};
        } else {
            String[] rightS = new String[productions.size()];
            for (int i = 0; i < rightS.length; i++) {
                rightS[i] = productions.getElement(i);
                System.out.print(rightS[i] + " ");
            }
            System.out.println("");
            return rightS;

        }
    }

    private String[] getNoTerminal() {
        String[] content = file.getContent();
        noTerm = new List2<String>();

        for (int i = 0; i < content.length; i++) {
            String currentLine = content[i];
            if (!currentLine.equals("")) {
                currentLine = leftSide(currentLine);
                if (currentLine != null) {
                    if (!noTerm.exists(currentLine)) {
                        noTerm.addLast(currentLine);
                    }
                }
            }
        }
        content = new String[noTerm.size()];
        for (int i = 0; i < content.length; i++) {
            content[i] = noTerm.getElement(i);
            System.out.println(content[i]);
        }

        return content;
    }

    private String[][] getProductions() {
        String[] content = file.getContent();
        produ = new List2<String[]>();

        for (int i = 0; i < content.length; i++) {

            String currentLine = content[i];
            if (!currentLine.equals("")) {
                String[] production = rightSide(currentLine);
                if (!produ.exists(production)) {
                    produ.addLast(production);
                }
            }
        }
        String contents[][] = new String[produ.size()][0];

        for (int i = 0; i < content.length; i++) {
            contents[i] = produ.getElement(i);
        }
        return contents;
    }

    private String[] getTerminal() {

        term = new List2<String>();

        for (int i = 0; i < productions.length; i++) {
            for (int j = 0; j < productions[i].length; j++) {
                if (!productions[i][j].equals("")
                        && !term.exists(productions[i][j])
                        && !noTerm.exists(productions[i][j])) {
                    term.addLast(productions[i][j]);
                }
            }
        }
//        if (!term.exists("$")) {
//            term.addLast("$");
//        }
        String terminales[] = new String[term.size()];
        for (int i = 0; i < terminales.length; i++) {
            terminales[i] = term.getElement(i);
            System.out.println(terminales[i]);
        }
        return terminales;
    }

    private String getInitialSymbol() {
        List2<String> production = new List2<String>();
      

        for (int i = 0; i < this.productions.length; i++) {
            for (int j = 0; j < this.productions[i].length; j++) {
                production.addFirst(this.productions[i][j]);

            }
        }
        
            for (int i = 0; i < this.noTerminal.length; i++) {
                if (!production.exists(noTerminal[i])) {
                    return noTerminal[i];
                }
            }

            return "";
       
    }

    public String[] symbolsTerminal() {
        return terminal;
    }

    public String[] symbolsNoTerminal() {
        return noTerminal;
    }

    public String[][] productions() {
        return productions;
    }

    public String initialSymbol() {
        return initialSymbol;
    }

    public void show() {
        String[] content = file.getContent();

        for (int i = 0; i < content.length; i++) {
            String currentLine = content[i];
            if (!currentLine.equals("")) {
                System.out.println(currentLine);
            }
        }

    }

}
