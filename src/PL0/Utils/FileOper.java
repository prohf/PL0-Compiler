package PL0.Utils;

import java.io.*;

import static PL0.Utils.Global.PCodes;

public class FileOper {

    public static String FileRead(String filepath) throws IOException {
        File InputFile = new File(filepath);
        if(!InputFile.exists()) {
            throw new FileNotFoundException();
        }
        BufferedReader inputReader = new BufferedReader(new FileReader(InputFile));
        StringBuilder strbuild = new StringBuilder();
        String str = null;
        str = inputReader.readLine();
        while(str!=null) {
            strbuild.append(str + "#");
            str = inputReader.readLine();
        }
        inputReader.close();
        return strbuild.toString();
    }

    public static void PCodeFileWrite(String outpath) throws IOException {
        File OutputFile = new File(outpath);
        if(!OutputFile.exists()) {
            OutputFile.createNewFile();
        }
        FileOutputStream outputStream = new FileOutputStream(OutputFile,true);
        PrintStream outprint = new PrintStream(outputStream);
        for(PCode pcode : PCodes) {
            outprint.println(PCodes.indexOf(pcode) + " " +
                    pcode.getOperator() + " " +
                    pcode.getS1() + " " +
                    pcode.getS2());
        }
        outputStream.close();
    }
}
