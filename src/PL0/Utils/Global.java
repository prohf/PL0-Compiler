package PL0.Utils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by apple on 2016/12/24.
 */
public class Global {
    public static String[] KeyWord; // 关键字表

    private static void setKeyWord() { // 关键字表初始化
        KeyWord = new String[]{"const", "var", "procedure", "odd", "if", "then",
                "while", "do", "call", "begin", "end", "read", "write", "repeat", "until"};
    }

    public static ArrayList<Word> Words; // 词法分析单词表

    public static HashMap<String, Integer> WordTypes; // 单词类型表

    private static void setWordTypes() { // 单词类型表初始化
        WordTypes = new HashMap<>();
        WordTypes.put("ERROR", 0);
        WordTypes.put("CONST", 1);
        WordTypes.put("VAR", 2);
        WordTypes.put("PROCEDURE", 3);
        WordTypes.put("ODD", 4);
        WordTypes.put("IF", 5);
        WordTypes.put("THEN", 6);
        WordTypes.put("WHILE", 7);
        WordTypes.put("DO", 8);
        WordTypes.put("CALL", 9);
        WordTypes.put("BEGIN", 10);
        WordTypes.put("END", 11);
        WordTypes.put("READ", 12);
        WordTypes.put("WRITE", 13);
        WordTypes.put("REPEAT", 14);
        WordTypes.put("UNTIL", 15);

        WordTypes.put("IDENTIFIER", 20);
        WordTypes.put("INTEGER", 21);
        WordTypes.put("PLUS", 22);
        WordTypes.put("MINUS", 23);
        WordTypes.put("MULTIPLE", 24);
        WordTypes.put("DIVIDE", 25);
        WordTypes.put("LPAR", 26);
        WordTypes.put("RPAR", 27);
        WordTypes.put("COMMA", 28);
        WordTypes.put("SEMI", 29);
        WordTypes.put("COLON", 30);
        WordTypes.put("ASSIGN", 31);
        WordTypes.put("EQUAL", 32);
        WordTypes.put("LESSTHAN", 33);
        WordTypes.put("MORETHAN", 34);
        WordTypes.put("NOLESSTHAN", 35);
        WordTypes.put("NOMORETHAN", 36);
        WordTypes.put("NOEQUAL", 37);
        WordTypes.put("FINAL", 233);

    }

    public static ArrayList<PCode> PCodes; // P-Code表

    public static ArrayList<SymbolTable> symbolTable; // 符号表

    public static HashMap<String, Integer> OPRtypes; // OPR 操作符类型

    private static void setOPRtypes() {
        OPRtypes = new HashMap<>();
        OPRtypes.put("return",0);
        OPRtypes.put("minus",1);
        OPRtypes.put("+",2);
        OPRtypes.put("-",3);
        OPRtypes.put("*",4);
        OPRtypes.put("/",5);
        OPRtypes.put("odd",6);

        OPRtypes.put("=",8);
        OPRtypes.put("<>",9);
        OPRtypes.put("<",10);
        OPRtypes.put(">=",11);
        OPRtypes.put(">",12);
        OPRtypes.put("<=", 13);
    }

    //public static int Current_Level;

    public static ArrayList<PL0Error> ErrorList;

    public static HashMap<Integer, String>ErrorMap;

    private static void setErrorMap() {
        ErrorMap = new HashMap<>();
        ErrorMap.put(1,"应是 =，不是 :=");
        ErrorMap.put(2,"= 后应为数");
        ErrorMap.put(3,"标识符后应为=");
        ErrorMap.put(4,"const, var, procedure 后应为标识符");
        ErrorMap.put(5,"漏掉逗号或分号");
        ErrorMap.put(6,"过程说明后的符号不正确");
        ErrorMap.put(7,"应为语句");
        ErrorMap.put(8,"程序体内部分语句后的符号不正确");
        ErrorMap.put(9,"应为句号");
        ErrorMap.put(10,"语句之间漏分号");
        ErrorMap.put(11,"标识符未说明");
        ErrorMap.put(12,"不可向常量或过程赋值");
        ErrorMap.put(13,"应为赋值运算符:=");
        ErrorMap.put(14,"call后应为标识符");
        ErrorMap.put(15,"不可调用常量或变量");
        ErrorMap.put(16,"应为then");
        ErrorMap.put(17,"应为分号或end");
        ErrorMap.put(18,"应为do");
        ErrorMap.put(19,"语句后的符号不正确");
        ErrorMap.put(20,"应为关系运算符");
        ErrorMap.put(21,"表达式不可有过程标识符");
        ErrorMap.put(22,"漏右括号");
        ErrorMap.put(23,"因子后不可为此符号");
        ErrorMap.put(24,"表达式不合法");
        ErrorMap.put(25,"应为until");
        ErrorMap.put(30,"这个数太大");
        ErrorMap.put(40,"应为左括号");
    }

    public Global() {
        setKeyWord();
        Words = new ArrayList<>();
        setWordTypes();
        PCodes = new ArrayList<>();
        symbolTable = new ArrayList<>();
        setOPRtypes();
        ErrorList = new ArrayList<>();
        setErrorMap();
    }
}
