package PL0.Compiler;

import PL0.Grammar.GrammarAnalysis;
import PL0.Lexical.LexicalAnalysis;
import PL0.Utils.*;

import java.io.IOException;
import java.util.Scanner;

import static PL0.Utils.FileOper.PCodeFileWrite;
import static PL0.Utils.Global.*;

/**
 * Created by apple on 2016/12/24.
 */
public class Compile {



    public static void main(String[] args) {

        Global global = new Global();
        Scanner file = new Scanner(System.in);
        System.out.println("请输入你想要编译的文件：");

        String inputfile = file.nextLine();
        String outputfile = inputfile + "_P-code";
        String inputpath = "test/" + inputfile + ".txt";
        String outputpath = "test/" + outputfile + ".txt";
        String BufferString = "";
        try {
            BufferString = FileOper.FileRead(inputpath);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        LexicalAnalysis LA = new LexicalAnalysis(BufferString);
        LA.Analysis();

        System.out.println("单词表");
        for(Word word:Words) {
            System.out.println("(" + Words.indexOf(word) + " | " + word.getWord() + " | " + word.getType() +
                                " | " + word.getLine() + " | " + word.getPosition() + ")");
        }
        System.out.println("*******************************************");

        System.out.println("语法分析");

        GrammarAnalysis GA = new GrammarAnalysis();
        GA.Program();

        System.out.println("*******************************************");

        System.out.println("符号表");

        for(SymbolTable item :symbolTable) {
            System.out.println(symbolTable.indexOf(item) + ": " + item.getName() + "," + item.getKind()
                    + "," + item.getValue() + "," + item.getLevel() + "," + item.getAdr());
        }

        if(!ErrorList.isEmpty()) {
            System.out.println("*******************************************");

            System.out.println("Error表");

            for (PL0Error error : ErrorList) {
                System.out.println(ErrorList.indexOf(error) + " " + "At Line " +
                        error.getErrorWord().getLine() + " " +
                        error.getErrorWord().getPosition() + " " + ", kind: " +
                        error.getErrorKind() + " " +
                        error.getError());
            }
        }
        else {
            System.out.println("*******************************************");

            System.out.println("P-Code表");

            for(PCode pcode : PCodes) {
                System.out.println(PCodes.indexOf(pcode) + " " +
                        pcode.getOperator() + " " +
                        pcode.getS1() + " " +
                        pcode.getS2());
            }
            try {
                PCodeFileWrite(outputpath);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}