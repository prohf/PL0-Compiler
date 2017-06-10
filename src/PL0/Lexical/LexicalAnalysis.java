package PL0.Lexical;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import PL0.Utils.Global;
import PL0.Utils.Word;

import static PL0.Utils.Global.Words;

public class LexicalAnalysis {

    private String buffer;
    private int currentLine; // 当前行
    private int currentPos; // 当前单词位置

    public LexicalAnalysis(String buffer) {
        this.buffer = buffer;
        currentLine = 1;
        currentPos = 1;
    }

    private boolean isIdentifier(String token) {
        String idregex = "^[a-zA-z][0-9a-zA-z]*";
        Pattern IdP = Pattern.compile(idregex);
        Matcher IdM = IdP.matcher(token);
        return IdM.matches();
    }
    private boolean isInteger(String token) {
        String IntStr = "[0-9]|^[1-9][0-9]*";
        Pattern IntP = Pattern.compile(IntStr);
        Matcher IntM = IntP.matcher(token);
        return IntM.matches();
    }
    private Integer TypeOfWord(String token) {
        Integer type = 0;
        for(int i = 0;i< Global.KeyWord.length;i++) {
            if (token.equals(Global.KeyWord[i])) {
                type = i + 1;
            }
        }
        if(type == 0) {
            if(isIdentifier(token)) type = 20;
            if(isInteger(token)) type = 21;
        }
        return type;
    }
    private void TakeWord(String token) {
        if(token.length()> 0) {
            Integer type = TypeOfWord(token);
            Words.add(new Word(token, type, currentLine, currentPos++));
        }
    }

    private boolean isDelimiter(char ch) { // 判断界符
        return ch == ';' || ch == ',' || ch == '.';
    }
    private Integer DelimiterType(String de) {
        switch (de) {
            case ",":
                return 28;
            case ";":
                return 29;
            case ".":
                return 233;
            default:
                return 0;
        }
    }

    private boolean isSingleOperator(char op) { // 判断单目运算符
        return  op == '+' ||
                op == '-' ||
                op == '*' ||
                op == '/' ||
                op == '(' ||
                op == ')' ||
                op == '=' ||
                op == '>' ||
                op == '<';
    }
    private Integer SingleOperatorType(String op) {
        switch (op) {
            case "+":
                return 22;
            case "-":
                return 23;
            case "*":
                return 24;
            case "/":
                return 25;
            case "(":
                return 26;
            case ")":
                return 27;
            case "=":
                return 32;
            case "<":
                return 33;
            case ">":
                return 34;
            default:
                return 0;
        }
    }

    private boolean isDoubleOperator(char ch1, char ch2) {
        if(ch1 == '<' || ch1 == '>' || ch1 == ':') {
            String op1 = "" + ch1;
            String op2 = "" + ch2;
            String op = op1 + op2;
            return op.equals(":=") ||
                    op.equals(">=") ||
                    op.equals("<=") ||
                    op.equals("<>");
        }
        else{
            return false;
        }
    }

    private Integer DoubleOperatorType(String op) {
        switch (op) {
            case ":=":
                return 31;
            case ">=":
                return 35;
            case "<=":
                return 36;
            case "<>":
                return 37;
            default:
                return 0;
        }
    }

    public void Analysis() {
        int length = buffer.length();
        int p = 0;
        char c;
        StringBuilder token = new StringBuilder();
        while(p < length ) {
            c = buffer.charAt(p);
            if( c == ' ' ||
                c == '\n'||
                c == '\t'||
                c == '#'){
                TakeWord(token.toString());
                token = new StringBuilder();
                if(c == '#') {
                    System.out.println("换行");
                    currentLine += 1;
                    currentPos = 1;
                }
            }
            else if(isDelimiter(c)) {
                TakeWord(token.toString());
                token = new StringBuilder();
                String delimiter = "" + c;
                Integer type = DelimiterType(delimiter);
                Words.add(new Word(delimiter,type,currentLine,currentPos++));
            }
            else if(isDoubleOperator(c, buffer.charAt(p+1))) {
                TakeWord(token.toString());
                token = new StringBuilder();
                String op1 = "" + c;
                String op2 = "" + buffer.charAt(p+1);
                String doubleOp = op1 + op2;
                Integer type = DoubleOperatorType(doubleOp);
                Words.add(new Word(doubleOp,type,currentLine,currentPos++));
                p += 1;
            }
            else if(isSingleOperator(c)) {
                TakeWord(token.toString());
                token = new StringBuilder();
                String singleOp = "" + c;
                Integer type = SingleOperatorType(singleOp);
                Words.add(new Word(singleOp,type,currentLine,currentPos++));
            }
            else{
                token.append(c);
            }
            p += 1;
        }
        System.out.println("******************");
    }
}
