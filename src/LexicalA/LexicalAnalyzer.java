/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LexicalA;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import LexicalA.ListSt;
import LexicalA.Node;
import LexicalA.Token;

/**
 *
 * @author Jimena Valderrama
 */
public class LexicalAnalyzer {

    private final int ID_ERROR = -1,
            ID_ACHAR = 400,
            ID_OESPECIAL = 405,
            ID_SCHAR = 401,
            //            ID_IDENTIF = 301,
            ID_INT_NUM = 310,
            ID_FLOAT_NUM = 320,
            ID_EQUAL = 301,
            ID_ADD = 500,
            ID_SUB = 501,
            ID_MULT = 502,
            ID_COLON = 305,
            ID_BLNKS = 300;
    private int ID_KEYWORD = 600;
    private int ID_IDENTIF = 800;
    private FileReader file;
    private ListSt errors, symbols, validT, kw, simple, intNum, floatNum;
    char state = '0';
    int linePos = 0;
    public static final String END_FILE = "$";
    int idToken = ID_ERROR;
    String number = "", numF = "";
    String currentLine;
    String token = "";
    String type = "";
    boolean keep = true;
    boolean cut = false;
    boolean error = false;

    //String keywords []= new FileReader("src//LexicalA//Keywords.txt").array();
    String keywords[] = new String[]{"programa", "begin", "end"};
    String ident[] = new String[]{"Cuenta", "Numero", "Resultado"};

    LexicalAnalyzer(String path) {
        file = new FileReader(path);
        this.errors = new ListSt("Errors");
        this.symbols = new ListSt("Identifiers");
        this.validT = new ListSt("Valid Tokens");
        this.kw = new ListSt("Keywords");
        this.simple = new ListSt("Simple Characters");
        this.intNum = new ListSt("Int Numbers");
        this.floatNum = new ListSt("Float Numbers");
    }

    public void readFile() {

        currentLine = file.lineReader();
        while (currentLine != null) {
            for (int i = 0; i < currentLine.length(); i++) {
                this.analyzer(i);
                if (cut || i == currentLine.length() - 1) {
                    if (idToken == -1) {
                        this.addErrors();
                    } else if (idToken == 310) {
                        this.addInts();
                    } else if (idToken == 320) {
                        this.addFloats();
                    } else if (idToken >= 800) {
                        this.addSymbol();
                    } else if (idToken >= 600) {
                        this.addKeywords();
                    } else if (idToken == 401) {
                        this.addSimpleCharacter();

                    }
                    token = "";
                    error = false;
                    cut = false;

                }
            }
            currentLine = file.lineReader();

        }
        reset();
        show();
    }

    public String getToken() {

        if (currentLine == null || endLine(currentLine, linePos)) {
            linePos = 0;
            currentLine = file.lineReader();
        }
        if (currentLine == null) {
            return END_FILE;
        }

        while (!endLine(currentLine, linePos)) {

            analyzer(linePos);
            if (cut || endLine(currentLine, linePos + 1)) {
                if (error) {
                    // si se corto con un caracter, entonces retroceder para luego evaluarlo
                    linePos++;

                    reset();
                    return grammarValue(ID_ERROR);

                } else {
                    if (idToken == ID_ERROR) {
                        idToken = clasify(state);
                    }
                    // Esta linea es para evitar el caracter ' ' en la salida
                    if (!(isBlankSpace(currentLine.charAt(linePos))
                            && idToken == ID_ACHAR)) {
                        int id = idToken;
                        linePos++;

                        if (isKeyword(token)) {
                            String _token = token;
                            reset();
                            return _token;
                        }

                        if (id == ID_OESPECIAL) {
                            String _token = token;
                            reset();
                            return _token;
                        }

                        if (id == ID_ERROR || id == ID_ACHAR) {
                            id = (int) token.charAt(0);
                            linePos--;
                        }

                        if (id == ID_ADD
                                || id == ID_SCHAR
                                || id == ID_EQUAL
                                || id == ID_SUB
                                || id == ID_MULT) {
                            String _token = String.valueOf(grammarValue(id));
                            reset();
                            return _token;
                        }

                        reset();
                        return String.valueOf(grammarValue(id));
                    }
                }
            }
            linePos++;
        }
        reset();
        return getToken();
    }

    private void analyzer(int position) {

        char ch = currentLine.charAt(position);
        if (isBlankSpace(ch) && this.token != "") {

            cut = true;
            idToken = clasify(state);
        } else {

            switch (state) {
                case '0':
                    if (ch == '0') {
                        state = 'E'; //float
                    } else if (isDot(ch)) {
                        state = 'x';//error
                    } else if (isUpperCase(ch)) {
                        state = '4';//identifiers
                    } else if (Character.isLowerCase(ch)) {
                        state = '8';//keyword
                    } else if (isCharacter(ch)) {
                        idToken = clasify(state);
                        //state = 'z';//imprime caracter 
                    } else if (Character.isDigit(ch)) {
                        state = '1';//digit
                    } else if (isDot(ch) && this.token != "") {
                        state = 'E'; //float
                    } else if (isColon(ch)) {
                        state = 'I';
                    } else if (isEqual(ch)) {
                        state = 'x';
                    } else {
                        error = true;
                    }
                    break;

                case '1':
                    if (Character.isDigit(ch)) {
                        state = '1';
                        idToken = clasify(state);
                    } else if (isDot(ch)) {
                        state = 'E';
                    } else if (isCharacter(ch)) {
                        state = 'x';
                    } else if (isAlphabet(ch)) {
                        state = 'x';
                    } else {
                        error = true;
                    }
                    break;

                case 'E':
                    if (Character.isDigit(ch) || isDot(ch)) {
                        idToken = clasify(state);
                    } else {
                        state = 'x';
                    }

                    break;

                case '4':
                    if (isAlphabet(ch) || Character.isDigit(ch)) { //a-z o 0-9     
                        idToken = clasify(state);
                        state = '4';
                    } else if (isUnderScore(ch)) {
                        state = 'D';
                        idToken = clasify(state);
                    } else {
                        state = 'x';
                    }

                    break;

                case 'D':
                    if (isAlphabet(ch) || Character.isDigit(ch)) { //a-z o 0-9     
                        idToken = clasify(state);
                        state = '4';
                    } else if (isUnderScore(ch)) {
                        state = 'D';
                        idToken = clasify(state);
                    } else {
                        state = 'x';
                    }

                    break;
                case '8':
                    if (Character.isLowerCase(ch)) {
                        state = '8';
                        idToken = clasify(state);
                    } else {

                        state = 'x';
                    }
                    break;
                case 'I':
                    if (isEqual(ch)) {
                        idToken = clasify(state);
                    } else {
                        state = 'x';
                    }

            }

            if (!cut) {
                token += ch;
            }
            if (state == 'z') {
                cut = true;

            } else if (state == 'x') {
                idToken = ID_ERROR;
                error = true;
                addErrors();

            }
        }
    }

    String noBlankSpaces(String line) {

        char[] charArray = line.toCharArray();
        String stringWithoutSpaces = "";

        for (int i = 0; i < charArray.length; i++) {
            if ((charArray[i] != ' ') && (charArray[i] != '\t')) {
                stringWithoutSpaces = stringWithoutSpaces + charArray[i];
            }
        }
        return stringWithoutSpaces;
    }

    int clasify(char state) {
        switch (state) {
            case 'E':
                return ID_FLOAT_NUM;
            case '1':
                return ID_INT_NUM;
            case 'x':
                return ID_ERROR;
            case '3':
                return ID_ERROR;
            case '4':
                return ID_IDENTIF;
            case '0':
                return ID_SCHAR;
            case 'D':
                return ID_IDENTIF;
            case '8':
                return ID_KEYWORD;
            case '7':
                return ID_EQUAL;
            case 'I':
                return ID_OESPECIAL;
            default:
                return ID_ERROR;
        }
    }

    private String grammarValue(int c) {
        switch (c) {
            case ID_ERROR:
                return "Lexical Error";
//            case ID_IDENTIF:
//                return "id";
            case ID_INT_NUM:
                return "intliteral";
            case ID_SCHAR:
                return token;
            case ID_ADD:
                return "+";
            case ID_SUB:
                return "-";
            case ID_MULT:
                return "*";
            case ID_OESPECIAL:
                return token;
            case ID_FLOAT_NUM:
                return "realliteral";

        }
        return String.valueOf((char) c);
    }

    private String charType(int c) {
        switch (c) {
            case '!':
                if (c == 33) {
                    type = "Exclamation point";
                    return type;
                }
            case '"':
                if (c == 34) {
                    type = "Quotations marks";
                    return type;
                }
            case '#':
                if (c == 35) {
                    type = "Number sign";

                    return type;
                }
            case '$':
                if (c == 36) {
                    type = "Currency symbol";

                    return type;
                }
            case '%':
                if (c == 37) {
                    type = "Percent sign";

                    return type;
                }
            case '&':
                if (c == 38) {
                    type = "And symbol";

                    return type;
                }
            case '(':
                if (c == 40) {
                    type = "Left parentheses";

                    return type;
                }
            case ')':
                if (c == 41) {
                    type = "Right parentheses";
                    return type;
                }
            case '*':
                if (c == 42) {

                    type = "Asterisks";
                    return type;
                }
            case '+':
                if (c == 43) {
                    type = "Addition sign";

                    return type;
                }
            case ',':
                if (c == 44) {
                    type = "Comma";

                    return type;
                }
            case '-':
                if (c == 45) {
                    type = "Hyphen";

                    return type;
                }
            case '.':
                if (c == 46) {
                    type = "Period";

                    return type;
                }
            case '/':
                if (c == 47) {
                    type = "Slash";

                    return type;
                }
            case ':':
                if (c == 58) {
                    type = "Colon";

                    return type;
                }
            case ';':
                if (c == 59) {
                    type = "Semicolon";

                    return type;
                }
            case '<':
                if (c == 60) {
                    type = "Less than";

                    return type;
                }
            case '=':
                if (c == 61) {
                    type = "Equals Sign";

                    return type;
                }
            case '>':
                if (c == 62) {
                    type = "Greater than";

                    return type;
                }
            case '?':
                if (c == 63) {
                    type = "Question mark";

                    return type;
                }
            case '@':
                if (c == 64) {
                    type = "At symbol";

                    return type;
                }
            case '[':
                if (c == 91) {
                    type = "Left Bracket";
                    return type;
                }
            case ']':
                if (c == 93) {
                    return "Right Bracket";
                }
            case '^':
                if (c == 94) {
                    type = "Caret";

                    return type;
                }
            case '_':
                if (c == 95) {
                    type = "Underscore";

                    return type;
                }
            case '´':
                if (c == 96) {
                    type = "Apostrophe";
                    return type;
                }
            case '{':
                if (c == 123) {
                    type = "Left Brace";
                    return type;
                }
            case '|':
                if (c == 124) {
                    type = "Vertical line";
                    return type;
                }
            case '}':
                if (c == 125) {
                    type = "Right Brace";
                    return type;
                }
            case '~':
                if (c == 33) {
                    type = "Tilde";

                    return type;
                }
                break;
        }
        return type;
    }

    boolean isCharacter(char character) {
        int charac = (int) character;
        return (charac >= 33 && charac <= 47)
                || (charac >= 58 && charac <= 64)
                || (charac >= 91 && charac <= 96)
                || (charac >= 123 && charac <= 254);
    }

    boolean isKeyword(String sign) {
        boolean s = false;
        for (String r : keywords) {
            if (r.equals(sign)) {
                s = true;
            }
        }
        return s;
    }

    boolean isIdentif(String sign) {
        boolean s = false;
        for (String r : ident) {
            if (r.equals(sign)) {
                s = true;
            }
        }
        return s;
    }

    private boolean isUnderScore(char character) {
        return character == '_';
    }

    private boolean isDot(char character) {
        return character == '.';
    }

    private boolean isColon(char character) {
        return character == ':';
    }

    private boolean isEqual(char character) {
        return character == '=';
    }

    boolean isBlankSpace(char character) {
        return (character == ' ' || character == '\t');
    }

    private boolean isAlphabet(char character) {
        return (character >= 'a' && character <= 'z');
    }

    private boolean isUpperCase(char character) {
        return (character >= 'A'
                && character <= 'Z');
    }

    private boolean isAddition(char character) {
        return character == '+';
    }

    private boolean isSub(char character) {
        return character == '-';
    }

    private boolean isMult(char character) {
        return character == '*';
    }

    public void addSymbol() {
        if (isIdentif(token)) {
            
            this.addStorage(symbols, token, "Identifiers", ID_IDENTIF, "Int");
            this.addStorage(validT, token, "Identifiers", ID_IDENTIF++, "Int");
            reset();
        } else {
            this.addErrors();
        }
    }

    public void addSimpleCharacter() {
        char c = token.charAt(0);
        
        this.addStorage(simple, token, "Simple Character", (int) c, charType(c));
        this.addStorage(validT, token, "Simple Character", (int) c, charType(c));
        reset();
    }

    public void addInts() {
        this.addStorage(intNum, token, "Int Numbers", ID_INT_NUM, "Int");
        this.addStorage(validT, token, "Int Numbers", ID_INT_NUM, "Int");
        reset();
    }

    public void addFloats() {
        this.addStorage(floatNum, token, "Float Numbers", ID_FLOAT_NUM, "Float");
        this.addStorage(validT, token, "Float Numbers", ID_FLOAT_NUM, "Float");
        reset();
    }

    public void addKeywords() {
        if (isKeyword(token)) {
            this.addStorage(kw, token, "Keywords", ID_KEYWORD, "Boolean expression");
            this.addStorage(validT, token, "Keywords", ID_KEYWORD++, "Boolean expression ");
            reset();
        } else {
            this.addErrors();
        }
    }

    public void addErrors() {
        this.addStorage(errors, token, "Errors", ID_ERROR, "Error");
        reset();
    }

    private void addStorage(ListSt st, String word, String tkn, int id, String type){
        Token token = new Token(word, tkn, id, type);
       
        if(st.exists(token)){
            token = st.getTokenbyId(id);
            token.setRep(token.getRep()+1);
        }else{
        st.saveUp(token);
    }
    }

    private boolean endLine(String line, int pos) {
        return line.length() <= pos;
    }
    //recuperar la posición en donde se entraba el caracter simple que se leyó
//    private boolean gotCut() {
//        return isCharacter(currentLine.charAt(linePos - 1)) && idToken != ID_ACHAR;
//    }

    private void reset() {
        token = "";
        error = false;
        cut = false;
        state = '0';
        idToken = ID_ERROR;
    }

    public void show() {
        System.out.println("    Lexeme    | ID  |    Token    |   Type   |  Repetitions");
        this.symbols.show();
        System.out.println("\t");
        this.kw.show();
        System.out.println("\t");
        this.intNum.show();
        System.out.println("\t");
        this.floatNum.show();
        System.out.println("\t");
        this.simple.show();
        System.out.println("\t");

    }

    class FileReader {

        BufferedReader br;
        String path;

        FileReader(String path) {
            this.path = path;
            try {
                br = new BufferedReader(new java.io.FileReader(path));
            } catch (FileNotFoundException fnf) {
            }
        }

        String lineReader() {
            try {
                return br.readLine();
            } catch (IOException ioe) {
            }
            return null;
        }

    }

    public static void main(String[] args) {
        String file = "src\\LexicalA\\TestProgram.txt";
        LexicalAnalyzer analyzer = new LexicalAnalyzer(file);
        analyzer.readFile();
        }
    }