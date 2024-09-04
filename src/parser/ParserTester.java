package parser;

import ast.*;
import scanner.*;

import java.io.*;
import java.nio.Buffer;
import java.nio.file.Files;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import environment.Environment;
import ast.*;

/**
 * The ParserTester class reads the test files and tests the Parser class for proper parsing.
 * @author Pranav Sukesh
 * @version 9/4/2023
 */
public class ParserTester
{
    /**
     * The main method of the ScannerTester class. It reads the test files and tests the Scanner
     * class using FileInputStreams.
     * @param args the arguments of the main method
     */
    public static void main(String[] args) throws ScanErrorException, IOException
    {
        String fileName = "src/testcases/akulsTestcases/parserTest12.txt";
        byte[] bad = Files.readAllBytes((new File(fileName)).toPath());

        for (byte b : bad)
        {
            //System.out.print((char)b);
        }

        ArrayList<Byte> good = new ArrayList<>();
        for (byte value : bad)
        {
            good.add(value);
        }
        for (int i = good.size()-1; i >= 0; i--)
        {
            if (good.get(i) == 10 && good.get(i-1) != 13)
            {
                good.add(i, (byte)13);
            }
        }

        BufferedWriter fw = new BufferedWriter(new FileWriter(fileName));
        for (byte b : good)
        {
            fw.write(String.valueOf((char) b));
        }
        fw.flush();
        fw.close();


        InputStream inStream =
                new FileInputStream(new File(fileName));
        Scanner s = new Scanner(fileName);
        Environment env = new Environment();
        Parser p = new Parser(s);
        Program program = p.parseProgram();
        program.exec(env);
        program.compile("bytecode.asm");



    }

}
