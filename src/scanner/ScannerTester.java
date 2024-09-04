package scanner;

import java.io.*;

/**
 * The ScannerTester class reads the test files and tests the Scanner class for proper tokenization.
 * @author Pranav Sukesh
 * @version 9/4/2023
 */
public class ScannerTester
{
    /**
     * The main method of the ScannerTester class. It reads the test files and tests the Scanner
     * class using FileInputStreams.
     * @param args the arguments of the main method
     */
    public static void main(String[] args) throws ScanErrorException, FileNotFoundException
    {

        InputStream inStream =
                new FileInputStream(new File("src/testcases/goodParserTest85.txt"));
        Scanner lex = new Scanner(inStream);
        while (lex.hasNext())
        {
            System.out.println(lex.nextToken());
        }
    }
}
