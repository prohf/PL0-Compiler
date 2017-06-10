package PL0.Grammar;

/**
 * Created by apple on 2016/12/25.
 */

import PL0.Lexical.LexicalAnalysis;
import PL0.Utils.*;

import java.io.IOException;

import static PL0.Utils.Global.*;

public class GrammarAnalysis {

    private int WordIndex; // 单词表索引
    private int CurrentLevel; // 当前level
    private Word CurrentWord; // 当前单词

    private static int MaxLevel = 100;
    private int[] dx; // 地址索引变量，为数组，对应不同层次
    private int cx; // 指令索引指针
    // private int tx; // 符号表索引指针,用symbolTable.size()替代


    public GrammarAnalysis() {
        WordIndex = 0;
        CurrentLevel = 0;
        CurrentWord = new Word();
        //dx = 3; // 初值为3
        dx = new int[MaxLevel];
        for(int i = 0; i < dx.length; i++) {
            dx[i] = 3;
        }
        cx = 0;
        //tx = 0;
    }

    private void Enter(SymbolTable Item) { // 填符号表
        symbolTable.add(Item);
    }

    private int Position(String Name) { // 查找符号表的位置
        int index = -1;
        for(SymbolTable item : symbolTable) {
            if(item.getName().equals(Name)) {
                index = symbolTable.indexOf(item);
            }
        }
        return index;
    }

    private void Gen(String f, int l, int a) {
        PCodes.add(new PCode(f,l,a));
        //cx = PCodes.size();
        cx++;
    }

    // 取下一个单词单词
    private void getsym() {
        CurrentWord = Words.get(WordIndex++);
        System.out.println(WordIndex-1 + " " + CurrentWord.getWord());
    }

    // 语法分析器入口
    public void Program() {
        // 初始化main函数符号表
        SymbolTable InitItem = new SymbolTable("main","procedure",-1,0,0);
        Enter(InitItem);

        Block(); // 进入分程序

        if(CurrentWord.getType().equals(WordTypes.get("FINAL"))) {
            System.out.println("End!"); // 语法分析结束
        }else {
            //错误处理
            System.out.println("Error");

            ErrorList.add(new PL0Error(CurrentWord, 9, ErrorMap.get(9)));
        }
    }

    // <分程序>分析子程序
    private void Block() {
        // 初始化PCode

        int tx0 = symbolTable.size()-1;

        if(tx0 > 0) {
            symbolTable.get(tx0).setAdr(cx);
        }
        Gen("jmp",0,0);
        getsym();
        while(CurrentWord.getType().equals(WordTypes.get("CONST"))) {
            ConstDeclare(); //进入<常量声明>
        }
        while(CurrentWord.getType().equals(WordTypes.get("VAR"))) {
            VarDeclare(); //进入<变量声明>
        }
        while(CurrentWord.getType().equals(WordTypes.get("PROCEDURE"))) {
            ProcedureDeclare(); // 进入<过程声明>
        }
        // 生成PCode
        if(tx0 >= 0) {
            PCodes.get(symbolTable.get(tx0).getAdr()).setS2(cx);
            symbolTable.get(tx0).setAdr(cx);
        }
        Gen("int",0,dx[CurrentLevel]);
        Statement(); // 进入<语句>
        Gen("opr",0,0);
        System.out.println("Block");

    }



    // <常量声明> 分析子程序
    private void ConstDeclare() {
        ConstAssign(); // 进入常量赋值
        while(CurrentWord.getType().equals(WordTypes.get("COMMA"))) { // 读到','
            ConstAssign(); // 进入常量赋值
        }
        if(CurrentWord.getType().equals(WordTypes.get("SEMI"))){ // 读到';'
            System.out.println("Constant Declaration!");
            getsym();
        }
        else {
            // 错误处理
            System.out.print("Current Word is " + CurrentWord.getWord() + " ");
            System.out.println("Error! Expect ; ");

            ErrorList.add(new PL0Error(CurrentWord, 17, ErrorMap.get(17)));

        }
    }
    // <常量赋值>分析子程序
    private void ConstAssign() {
        getsym();
        Word Const = CurrentWord;
        if(Const.getType().equals(WordTypes.get("IDENTIFIER"))) { //判断是不是标识符

            String ConstName = Const.getWord(); // 取常量名
            String Kind = "constant"; // 类型为常量
            System.out.println(ConstName);

            getsym();
            if(CurrentWord.getType().equals(WordTypes.get("EQUAL"))) { //判断是不是等号

                getsym();
                Word IntValue = CurrentWord;
                if(IntValue.getType().equals(WordTypes.get("INTEGER"))) { //判断是不是数字

                    String Value = IntValue.getWord(); //取常量值
                    int value = Integer.parseInt(Value);
                    //常量无level和adr
                    int level = -1;
                    int adr = -1;
                    //写入符号表
                    Enter(new SymbolTable(ConstName,Kind,value,level,adr));
                    System.out.println("Constant Assign");
                    getsym();
                }
                else {
                    // 错误处理
                    System.out.print("Current Word is " + CurrentWord.getWord() + " ");
                    System.out.println("Error! Expect Integer!");

                    ErrorList.add(new PL0Error(CurrentWord, 2, ErrorMap.get(2)));
                }
            }
            else {
                // ErrorHandle
                System.out.print("Current Word is " + CurrentWord.getWord() + " ");
                System.out.println("Error! Expect =");

                ErrorList.add(new PL0Error(CurrentWord, 3, ErrorMap.get(3)));
            }
        }
        else {
            System.out.println("Error! Expect Identifier");

            ErrorList.add(new PL0Error(CurrentWord, 4, ErrorMap.get(4)));
        }
    }

    // <变量声明头>分析子程序
    private void VarDeclare() {
        VarAssign(); // 进入变量赋值
        while(CurrentWord.getType().equals(WordTypes.get("COMMA"))) { // 读到','
            VarAssign(); // 进入变量赋值
        }

        if(CurrentWord.getType().equals(WordTypes.get("SEMI"))){ // 读到';'
            System.out.println("Var Declaration!");
            getsym();
        }
        else {
            System.out.print("Current Word is " + CurrentWord.getWord() + " ");
            System.out.println("Error! Expect ; ");

            ErrorList.add(new PL0Error(CurrentWord, 17, ErrorMap.get(17)));
        }
    }

    // <变量声明>分析子程序
    private void VarAssign() {
        getsym();
        Word var = CurrentWord; // 变量名, 为标识符
        if(var.getType().equals(WordTypes.get("IDENTIFIER"))) {


            String VarName = var.getWord(); // 取变量名
            String Kind = "variable"; // 类型为变量
            int value = -1; // 变量无value
            int level = CurrentLevel; // 取level
            int adr = dx[level]++; // 去adr
            Enter(new SymbolTable(VarName, Kind, value, level, adr));
            System.out.println("Variable Assign");
            getsym();
        }
        else {
            System.out.print("Current Word is " + CurrentWord.getWord() + " ");
            System.out.println("Error! Expect Identifier");

            ErrorList.add(new PL0Error(CurrentWord, 4, ErrorMap.get(4)));
        }
    }

    // <过程声明>分析子程序
    private void ProcedureDeclare() {
        getsym();
        Word proc = CurrentWord; // 过程名，为标识符
        if(proc.getType().equals(WordTypes.get("IDENTIFIER"))) {


            String proc_name = proc.getWord(); // 取过程名
            String Kind = "procedure"; // 类型为过程
            int value = -1; // 过程无value
            int level = CurrentLevel; // 取level

            int adr = -1; // 取adr
            Enter(new SymbolTable(proc_name,Kind,value,level,adr));
            System.out.println("Procedure Declaration");

            getsym();
            if(CurrentWord.getType().equals(WordTypes.get("SEMI"))) { // 读到';'
                CurrentLevel += 1; // 当前level + 1
                dx[CurrentLevel] = 3; // 当前level地址索引初始化
                Block(); //进入分程序
                if(CurrentLevel - 1 >= 0)CurrentLevel -= 1; // 当前level - 1
                getsym();
            }
            else {
                System.out.print("Current Word is " + CurrentWord.getWord() + " ");
                System.out.println("Error! Expect ; ");

                ErrorList.add(new PL0Error(CurrentWord, 5, ErrorMap.get(5)));
            }
        }
        else {
            System.out.print("Current Word is " + CurrentWord.getWord() + " ");
            System.out.println("Error! Expect Identifier");

            ErrorList.add(new PL0Error(CurrentWord, 4, ErrorMap.get(4)));
        }
    }

    // <语句>分析子程序
    private void Statement() {
        Word Follow = CurrentWord;
        if(Follow.getType().equals(WordTypes.get("IDENTIFIER"))) { // 读到 ident

            int i = Position(Follow.getWord()); // 查符号表
            if(i == -1) { // 查无此符号
                System.out.println("Error! Ident Not Find");

                ErrorList.add(new PL0Error(CurrentWord, 11, ErrorMap.get(11)));
            }
            else {
                SymbolTable Item = symbolTable.get(i);
                if(!Item.getKind().equals("variable")) {
                    System.out.println("Error! Expect Variable!");
                    i = -1;
                }
                Assignment(); // 进入赋值语句
                if(i != -1) {
                    int lev = Item.getLevel();
                    int adr = Item.getAdr();
                    Gen("sto", CurrentLevel - lev, adr);
                }
            }

        }
        else if(Follow.getType().equals(WordTypes.get("CALL"))) { // 读到 call
            Call_Procedure(); // 进入过程调用语句
        }
        else if(Follow.getType().equals(WordTypes.get("BEGIN"))) { // 读到 begin
            System.out.println("Enter Mix_Statement");
            //CurrentLevel+=1;
            Mix_Statement(); // 进入复合语句
        }
        else if(Follow.getType().equals(WordTypes.get("IF"))) { // 读到 IF
            if_Condition(); // 进入if条件语句
        }
        else if(Follow.getType().equals(WordTypes.get("WHILE"))) { // 读到 while
            while_Condition(); // 进入while循环语句
        }
        else if(Follow.getType().equals(WordTypes.get("READ"))) { // 读到 read
            read_Statement(); // 进入读语句
        }
        else if(Follow.getType().equals(WordTypes.get("WRITE"))) { // 读到right
            write_Statement(); // 进入写语句
        }
        else if(Follow.getType().equals(WordTypes.get("REPEAT"))) { // 读到repeat
            repeat_Statement(); // 进入重复语句
        }
        else {
            System.out.print("Current Word is " + Words.get(WordIndex-1).getWord() + " ");
            System.out.println("Unknown Statement");

            ErrorList.add(new PL0Error(CurrentWord, 8, ErrorMap.get(8)));
        }
        System.out.println("Statement");
    }

    // <复合语句>分析子程序
    private void Mix_Statement() {
        getsym();
        Statement(); //进入语句

        while(CurrentWord.getType().equals(WordTypes.get("SEMI"))) { // 读到';'
            getsym();
            Statement(); // 进入语句
        }

        if(CurrentWord.getType().equals(WordTypes.get("END"))) { // 读到end
            System.out.println("Exit Mix_Statement"); // 退出复合语句层
            //CurrentLevel -= 1;
            getsym();
        }
        else {
            System.out.print("Current Word is " + CurrentWord.getWord() + " ");
            System.out.println("Error! Expect END");

            ErrorList.add(new PL0Error(CurrentWord, 17, ErrorMap.get(17)));
        }
    }

    // <赋值语句>分析子程序
    private void Assignment() {
        getsym();
        if(CurrentWord.getType().equals(WordTypes.get("ASSIGN"))) { // 读到 :=
            Expression(); // 进入表达式
            System.out.println("Assignment");
            getsym();
        }
        else {
            System.out.print("Current Word is " + CurrentWord.getWord() + " ");
            System.out.println("Error! Expect :=");

            ErrorList.add(new PL0Error(CurrentWord, 13, ErrorMap.get(13)));
        }
    }

    // <过程调用语句>分析子程序
    private void Call_Procedure() {
        getsym();
        Word ident = CurrentWord; // 过程名，为标识符
        if(ident.getType().equals(WordTypes.get("IDENTIFIER"))) {
            int i = Position(ident.getWord()); // 查符号表
            if(i == -1) { // 查无此符号
                System.out.println("Error! Ident Not Find");

                ErrorList.add(new PL0Error(CurrentWord, 11, ErrorMap.get(11)));
            }
            else {
                // 生成PCode
                SymbolTable Item = symbolTable.get(i);
                if(Item.getKind().equals("procedure")){
                    int lev = Item.getLevel();
                    int adr = Item.getAdr();
                    Gen("cal", CurrentLevel - lev, adr);
                }
                else {
                    System.out.println("Error! Expect Procedure");

                    ErrorList.add(new PL0Error(CurrentWord, 15, ErrorMap.get(15)));
                }
            }
        }
        else {
            System.out.print("Current Word is " + CurrentWord.getWord() + " ");
            System.out.println("Error! Expect Identifier");

            ErrorList.add(new PL0Error(CurrentWord, 14, ErrorMap.get(14)));
        }
        getsym();
    }

    // <表达式>分析子程序
    private void Expression() {
        if(CurrentWord.getType().equals(WordTypes.get("PLUS"))) // 读到 + （正号）
            getsym();
        if(CurrentWord.getType().equals(WordTypes.get("MINUS"))) { // 读到 - （负号）

            Gen("opr",0,OPRtypes.get("minus"));
            getsym();
        }
        Term(); // 进入项
        while(CurrentWord.getType().equals(WordTypes.get("PLUS")) ||
              CurrentWord.getType().equals(WordTypes.get("MINUS"))) { // 读到 + 或 -
            Integer op = CurrentWord.getType(); // 记录当前运算符
            Term(); // 进入项
            // 生成PCode
            if(op.equals(WordTypes.get("PLUS"))) { // 加法操作
                Gen("opr",0,OPRtypes.get("+"));
            }
            else { // 减法操作
                Gen("opr",0,OPRtypes.get("-"));
            }

        }
        System.out.println("Expression");

    }

    // <项>分析子程序
    private void Term() {
        Factor(); // 进入因子
        while(CurrentWord.getType().equals(WordTypes.get("MULTIPLE")) ||
              CurrentWord.getType().equals(WordTypes.get("DIVIDE"))) { // 读到 * 或 /
            Integer op = CurrentWord.getType(); // 记录当前运算符
            Factor(); // 进入因子
            // 生成PCode
            if(op.equals(WordTypes.get("MULTIPLE"))) { // 乘法操作
                Gen("opr",0,OPRtypes.get("*"));
            }
            else { // 除法操作
                Gen("opr",0,OPRtypes.get("/"));
            }
        }
        System.out.println("Term");
    }

    // <因子>分析子程序
    private void Factor() {
        getsym();
        Word Item = CurrentWord;
        if(Item.getType().equals(WordTypes.get("IDENTIFIER"))) {

            System.out.println("Identifier");

            int i = Position(Item.getWord()); // 取符号表
            if(i == -1) { // 无此标识符
                System.out.println("Error! Ident Not Find");

                ErrorList.add(new PL0Error(CurrentWord, 11, ErrorMap.get(11)));
            }
            else {
                //生成PCode
                SymbolTable TableItem = symbolTable.get(i);
                switch (TableItem.getKind()) {
                    case "constant": // 为常量
                        int val = TableItem.getValue();
                        Gen("lit", 0, val);
                        break;
                    case "variable": // 为变量
                        int lev = TableItem.getLevel();
                        int adr = TableItem.getAdr();
                        Gen("lod",CurrentLevel - lev, adr);
                        break;
                    default: // 为过程名 错误
                        System.out.println("Error! Not Procedure");

                        ErrorList.add(new PL0Error(CurrentWord, 21, ErrorMap.get(21)));
                }
            }
        }
        else if(Item.getType().equals(WordTypes.get("INTEGER"))) {

            System.out.println("Integer");
            // 生成PCode
            int num = Integer.parseInt(Item.getWord());
            Gen("lit", 0, num);
        }
        else if(Item.getType().equals(WordTypes.get("LPAR"))) {
            Expression();
            getsym();
            if(CurrentWord.getType().equals(WordTypes.get("RPAR"))) {
                System.out.println("(Expression)");
            }
            else {
                System.out.print("Current Word is " + CurrentWord.getWord() + " ");
                System.out.println("Error! Expect )");

                ErrorList.add(new PL0Error(CurrentWord, 22, ErrorMap.get(22)));
            }
        }
        else {
            System.out.print("Current Word is " + CurrentWord.getWord() + " ");
            System.out.println("Error! Expect Var or Int");

            ErrorList.add(new PL0Error(CurrentWord, 23, ErrorMap.get(23)));
        }
        System.out.println("Factor");
        // 设置一个探针
        // 如果因子的下一个单词是运算符，则继续取下一单词，进行表达式解析
        // 如果不是运算符，则跳出表达式分析子程序
        if (Words.get(WordIndex).getType().equals(WordTypes.get("MULTIPLE")) ||
            Words.get(WordIndex).getType().equals(WordTypes.get("DIVIDE"))  ||
            Words.get(WordIndex).getType().equals(WordTypes.get("PLUS")) ||
            Words.get(WordIndex).getType().equals(WordTypes.get("MINUS"))) {
            getsym();
        }
    }

    // <条件>分析子程序
    private void Condition() {

        if(CurrentWord.getType().equals(WordTypes.get("ODD"))) {
            getsym();
            Expression();
            // 生成PCode
            Gen("opr",0,OPRtypes.get("odd"));
        }
        else {
            Expression();
            getsym();
            Integer Operator = CurrentWord.getType(); // 记录当前操作符类型
            Expression();
            // 生成PCode
            switch (Operator) {
                case 32: // =
                    Gen("opr", 0, OPRtypes.get("="));
                    break;
                case 33: // <
                    Gen("opr", 0, OPRtypes.get("<"));
                    break;
                case 34: // >
                    Gen("opr", 0, OPRtypes.get(">"));
                    break;
                case 35: // >=
                    Gen("opr", 0, OPRtypes.get(">="));
                    break;
                case 36: // <=
                    Gen("opr", 0, OPRtypes.get("<="));
                    break;
                case 37: // <>
                    Gen("opr", 0, OPRtypes.get("<>"));
                    break;
                default:
                    System.out.println("Operator is " + Operator);
                    System.out.println("Error! Operator is Wrong!");

                    ErrorList.add(new PL0Error(CurrentWord, 20, ErrorMap.get(20)));
                    break;
            }
        }
    }

    // <条件语句>分析子程序
    private void if_Condition() {
        Condition();
        System.out.println("IF Statement");
        getsym();
        if(CurrentWord.getType().equals(WordTypes.get("THEN"))) {
            getsym();
            // 生成PCode,并记录跳转地址
            int cx1 = cx;
            Gen("jpc",0,0);
            Statement();
            PCodes.get(cx1).setS2(cx);
        }
        else {
            System.out.print("Current Word is " + Words.get(WordIndex-1).getWord() + " ");
            System.out.println("Error! Expect then");

            ErrorList.add(new PL0Error(CurrentWord, 16, ErrorMap.get(16)));
        }
    }

    // <当循环语句>分析子程序
    private void while_Condition() {
        int cx1 = cx;
        Condition();
        System.out.println("WHILE Statement");
        getsym();
        // 生成PCode,并记录跳转地址
        int cx2 = cx;
        Gen("jpc",0,0);
        if(CurrentWord.getType().equals(WordTypes.get("DO"))) {
            getsym();
            Statement();
            Gen("jmp",0,cx1);
            PCodes.get(cx2).setS2(cx);
        }else {
            System.out.print("Current Word is " + Words.get(WordIndex-1).getWord() + " ");
            System.out.println("Error! Expect DO ");

            ErrorList.add(new PL0Error(CurrentWord, 18, ErrorMap.get(18)));
        }
    }

    // <重复语句>分析子程序
    private void repeat_Statement() {
        int cx1 = cx;
        getsym();
        Statement(); //进入语句

        while(CurrentWord.getType().equals(WordTypes.get("SEMI"))) { // 读到';'
            getsym();
            Statement(); // 进入语句
        }

        if(CurrentWord.getType().equals(WordTypes.get("UNTIL"))) { // 读到until
            Condition();
            // 生成p-code
            Gen("jpc", 0, cx1);
            System.out.println("Exit Repeat"); // 退出复合语句层
            //CurrentLevel -= 1;
            getsym();
        }
        else {
            System.out.print("Current Word is " + CurrentWord.getWord() + " ");
            System.out.println("Error! Expect UNTIL");

            ErrorList.add(new PL0Error(CurrentWord, 25, ErrorMap.get(25)));
        }
    }

    // <读语句>分析子程序
    private void read_Statement() {
        getsym();
        if(CurrentWord.getType().equals(WordTypes.get("LPAR"))) {
            getsym();
            if(CurrentWord.getType().equals(WordTypes.get("IDENTIFIER"))) {


                int i = Position(CurrentWord.getWord()); // 取符号表
                if(i == -1) { // 无此标识符
                    System.out.println("Error! Ident Not Find");

                    ErrorList.add(new PL0Error(CurrentWord, 11, ErrorMap.get(11)));
                }
                else {
                    SymbolTable Item = symbolTable.get(i);
                    if(!Item.getKind().equals("variable")) {
                        System.out.println("Error! Expect Variable!");
                        i = -1;
                    }
                    if(i != -1) {
                        int lev = Item.getLevel();
                        int adr = Item.getAdr();
                        Gen("red", CurrentLevel - lev, adr);
                    }
                }


                getsym();
                while(CurrentWord.getType().equals(WordTypes.get("COMMA"))) {
                    getsym();
                    if(CurrentWord.getType().equals(WordTypes.get("IDENTIFIER"))) {


                        int j = Position(CurrentWord.getWord()); // 取符号表
                        if(j == -1) { // 无此标识符
                            System.out.println("Error! Ident Not Find");

                            ErrorList.add(new PL0Error(CurrentWord, 11, ErrorMap.get(11)));
                        }
                        else {
                            SymbolTable Item = symbolTable.get(j);
                            if(!Item.getKind().equals("variable")) {
                                System.out.println("Error! Expect Variable!");

                                ErrorList.add(new PL0Error(CurrentWord, 11, ErrorMap.get(11)));
                            }
                            else{
                                int lev = Item.getLevel();
                                int adr = Item.getAdr();
                                Gen("red", CurrentLevel - lev, adr);
                            }
                        }
                        getsym();
                    }
                    else {
                        System.out.println("Error! Expect Identifier");

                        ErrorList.add(new PL0Error(CurrentWord, 11, ErrorMap.get(11)));
                    }
                }
                if(CurrentWord.getType().equals(WordTypes.get("RPAR"))) {
                    System.out.println("Read Statement");
                    getsym();
                }
                else {
                    System.out.print("Current Word is " + CurrentWord.getWord() + " ");
                    System.out.println("Error! Expect )");

                    ErrorList.add(new PL0Error(CurrentWord, 22, ErrorMap.get(22)));
                }
            }
            else {
                System.out.print("Current Word is " + CurrentWord.getWord() + " ");
                System.out.println("Error! Expect Identifier");

                ErrorList.add(new PL0Error(CurrentWord, 11, ErrorMap.get(11)));
            }
        }
        else {
            System.out.print("Current Word is " + CurrentWord.getWord() + " ");
            System.out.println("Error! Expect (");

            ErrorList.add(new PL0Error(CurrentWord, 40, ErrorMap.get(40)));
        }
    }

    // <写语句>分析子程序
    private void write_Statement() {
        getsym();
        if(CurrentWord.getType().equals(WordTypes.get("LPAR"))) {
            Expression();
            Gen("wrt",0,0);
            getsym();
            while(CurrentWord.getType().equals(WordTypes.get("COMMA"))) {
                Expression();
                Gen("wrt",0,0);
                getsym();
            }
            if(CurrentWord.getType().equals(WordTypes.get("RPAR"))) {
                System.out.println("Write Statement");
                getsym();
            }
            else {
                System.out.print("Current Word is " + CurrentWord.getWord() + " ");
                System.out.println("Error! Expect )");

                ErrorList.add(new PL0Error(CurrentWord, 22, ErrorMap.get(22)));
            }
        }
        else {
            System.out.print("Current Word is " + CurrentWord.getWord() + " ");
            System.out.println("Error! Expect (");

            ErrorList.add(new PL0Error(CurrentWord, 40, ErrorMap.get(40)));
        }
    }
}