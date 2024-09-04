package scanner;

import com.sun.source.tree.ReturnTree;

import java.io.*;

/**
 * A scanner reads an input stream, recognizes tokens, and returns them one at a time.  A token is
 * either an operator, an identifier, or a number.  An operator is one of the following characters:
 * = + - * / % ( ) < > : ;.  An identifier is a sequence of letters and digits beginning with a
 * letter. A number is a sequence of digits.  White space (blanks, tabs, new lines) is used to
 * separate tokens.  A comment is either a single line comment that begins with // and ends with
 * the end of the line, or a block comment that begins with /* and ends with * / (without the
 * space).  The scanner skips comments.
 *
 * @author  Pranav Sukesh
 * @version 9/4/2023
 */
public class Scanner
{
    private BufferedReader in;
    private char currentChar;
    private boolean eof;
    private int endToken;
    private int lineNumber;
    private int charNumber;

    public static enum TOKEN_TYPE
    {
        ID, NUM, OP, SEP, EOF
    }
    /**
     * Scanner constructor for construction of a scanner that
     * uses an InputStream object for input.
     * Usage:
     * FileInputStream inStream = new FileInputStream(new File(<file name>);
     * Scanner lex = new Scanner(inStream);
     *
     * @param inStream the input stream to use
     */
    public Scanner(InputStream inStream)
    {
        in = new BufferedReader(new InputStreamReader(inStream));
        eof = false;
        endToken = 0;
        lineNumber = 1;
        charNumber = -1;
        getNextChar();
    }

    /**
     * Scanner constructor for constructing a scanner that
     * scans a given input string.  It sets the end-of-file flag an then reads
     * the first character of the input string into the instance field currentChar.
     * Usage: Scanner lex = new Scanner(input_string);
     *
     * @param inString the string to scan
     */
    public Scanner(String inString) throws FileNotFoundException
    {
        in = new BufferedReader(new FileReader(inString));
        eof = false;
        endToken = 0;
        lineNumber = 1;
        charNumber = -1;
        getNextChar();
    }


    /**
     * Method: getNextChar
     * The method sets the instance field currentChar to the value read from the input stream using
     * the stream read method. If the read method returns -1, indicating end of file, the
     * instance field eof is set to true. Additionally, if the current character is a period,
     * the method sets the instance field eof to true.
     *
     * @postcondition the instance field currentChar is set to the value read from the input stream
     */
    private void getNextChar()
    {
        try
        {
            int inp = in.read();
            charNumber++;
            if (inp == '\n')
            {
                lineNumber++;
                charNumber = -1;
            }
            else if (inp == '.' || inp == -1)
            {
                endToken++;
                if (endToken >= 2)
                    eof = true;
                currentChar = '.';
            }
            else
            {
                currentChar = (char) inp;
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Method: eat
     * The method compares the parameter to the instance field currentChar.  If they are equal, the
     * method calls getNextChar to read the next character from the input stream.  If they are
     * not equal, the method throws ScanErrorException.
     *
     * @postcondition the instance field currentChar is set to the value read from the input stream
     * @param expected the character expected
     * @throws ScanErrorException if the parameter does not match the instance field currentChar
     */
    private void eat(char expected) throws ScanErrorException
    {
        if (expected == currentChar)
        {
            getNextChar();
        }
        else
        {
            throw new ScanErrorException(
                    "Illegal character, expected " + expected + " and found " + currentChar);
        }
    }

    /**
     * Method: hasNext
     * The method determines whether the input stream has reached end of file (EOF).
     *
     * @return false if the input stream has reached end of file; otherwise true
     */
    public boolean hasNext()
    {
        return !eof;
    }

    /**
     * Method: nextToken
     * The method scans the input stream and returns the next token. The method first skips over
     * any white space characters, and then it determines whether the next token is a number, an
     * identifier, or an operator and returns the appropriate token. If it reaches a "//", it skips
     * the rest of the line. If it reaches a /*, it skips the character until it reaches the end
     * of the block comment. If the character is none of these things, it throws a
     * ScanErrorException. If the scanner reaches the end of the file, it returns "END" and
     * terminates further tokenization.
     *
     * @postcondition the instance field currentChar is set to the value after the token
     * @return  the next token as a String
     */
    public Token nextToken() throws ScanErrorException
    {

        if (!hasNext())
        {
            return new Token("EOF", TOKEN_TYPE.EOF, lineNumber, charNumber);
        }


        while (hasNext() && isWhiteSpace(currentChar))
        {
            eat(currentChar);
        }
        if (currentChar == '/')
        {
            eat(currentChar);
            if (currentChar == '/')
            {
                while (currentChar != '\n')
                {
                    eat(currentChar);
                }
                eat(currentChar);
                while (hasNext() && (isWhiteSpace(currentChar) || isSemiColon(currentChar)))
                {
                    eat(currentChar);
                }
            }
            else if (currentChar == '*')
            {

                eat(currentChar);
                char prevChar = currentChar;
                while (prevChar != '*' || currentChar != '/')
                {
                    prevChar = currentChar;
                    eat(currentChar);
                }
                eat(currentChar);
                while (hasNext() && (isWhiteSpace(currentChar) || isSemiColon(currentChar)))
                {
                    eat(currentChar);
                }
            }

            else
            {
                return new Token("/", TOKEN_TYPE.OP, lineNumber, charNumber);
            }
        }
        if (isSemiColon(currentChar))
        {
            eat(currentChar);
            return new Token(";", TOKEN_TYPE.SEP, lineNumber, charNumber);
        }
        else if (isComma(currentChar))
        {
            eat(currentChar);
            return new Token(",", TOKEN_TYPE.SEP, lineNumber, charNumber);
        }
        else if (isLetter(currentChar))
        {
            return new Token(scanIdentifier(), TOKEN_TYPE.ID, lineNumber, charNumber);
        }
        else if (isDigit(currentChar))
        {
            return new Token(scanNumber(), TOKEN_TYPE.NUM, lineNumber, charNumber);
        }
        else if (isOperator(currentChar))
        {
            return new Token(scanOperator(), TOKEN_TYPE.OP, lineNumber, charNumber);
        }
        else if (isPeriod(currentChar) )
        {
            eof = true;
            endToken++;
            return new Token("EOF", TOKEN_TYPE.EOF, lineNumber, charNumber);
        }
        else
        {
            throw new ScanErrorException("Illegal character: " + currentChar);

        }
    }

    /**
     * Method: scanNumber
     * The method scans a number from the input stream.  When it is called, the instance field
     * currentChar must be a digit, and it returns a String consisting of the sequence of digits
     * starting with currentChar.  When the method returns, the instance field currentChar will
     * be the first character after the number.
     *
     * @precondition    the instance field currentChar must be a digit
     * @postcondition   the instance field currentChar will be the first character after the number
     * @return the number as a String
     */
    private String scanNumber() throws ScanErrorException
    {
        String number = "";
        while (isDigit(currentChar))
        {
            number += currentChar;
            eat(currentChar);
        }
        return number;
    }


    /**
     * Method: scanIdentifier
     * The method scans an identifier from the input stream.  When it is called, the instance field
     * currentChar must be a letter, and it returns a String consisting of the sequence of
     * letters and digits starting with currentChar. When the method returns, the instance field
     * currentChar will be the first character after the identifier.
     *
     * @precondition    the instance field currentChar must be a letter
     * @postcondition   the instance field currentChar will be the first character after the
     *                  identifier
     * @return the identifier as a String
     */
    private String scanIdentifier() throws ScanErrorException
    {
        String identifier = "";
        while (isLetter(currentChar)|| isDigit(currentChar))
        {
            identifier += currentChar;
            eat(currentChar);
        }
        return identifier;
    }

    /**
     * Method: scanOperator
     * The method scans an operator from the input stream.  When it is called, the instance field
     * currentChar must not be a letter or a digit, and it returns a String consisting of the
     * currentChar.  When the method returns, the instance field currentChar will be the first
     * character after the operator. It scans <=, >=, <>, and := as single operators.
     *
     * @precondition    the instance field currentChar must not be a letter or a digit
     * @postcondition   the instance field currentChar will be the first character after the
     *                  operator
     * @return the operator as a String
     */
    private String scanOperator() throws ScanErrorException
    {
        String operator = "";
        if (isOperator(currentChar))
        {
            operator += currentChar;
            eat(currentChar);
            if (currentChar == '=')
            {
                operator += currentChar;
                eat(currentChar);
            }
            if (currentChar == '>')
            {
                operator += currentChar;
                eat(currentChar);
            }
        }
        return operator;
    }


    /**
     * Method: isDigit
     * The method determines whether the given char is a digit [0-9].
     *
     * @param c the character to check
     * @return true if the character is a digit; otherwise false
     */
    public static boolean isDigit(char c)
    {
        return c >= '0' && c <= '9';
    }

    /**
     * Method: isLetter
     * The method determines whether the given char is a letter [a-z] or [A-Z].
     *
     * @param c the character to check
     * @return true if the character is a letter; otherwise false
     */
    public static boolean isLetter(char c)
    {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    /**
     * Method: isWhiteSpace
     * The method determines whether the given char is a white space character [space], [tab],
     * [newline], or [carriage].
     *
     * @param c the character to check
     * @return true if the character is a white space character; otherwise false
     */
    public static boolean isWhiteSpace(char c)
    {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }

    /**
     * Method: isOperator
     * The method determines whether the given char is an operator ['=','+','-','*','/','%','(',
     * ')','<','>',':'].
     *
     * @param c the character to check
     * @return  true if the character is an operator; otherwise false
     */
    public static boolean isOperator(char c)
    {
        return c == '=' || c == '+' || c == '-' || c == '*' || c == '/' || c == '%' || c == '('
                || c == ')' || c == '<' || c == '>' || c == ':';
    }

    /**
     * Method: isSemiColon
     * The method determines whether the given char is a semicolon [;].
     *
     * @param c the character to check
     * @return  true if the character is a semicolon; otherwise false
     */
    public static boolean isSemiColon(char c)
    {
        return c == ';';
    }

    /**
     * Method: isComma
     * The method determines whether the given char is a comma [,].
     *
     * @param c the character to check
     * @return  true if the character is a comma; otherwise false
     */
    public static boolean isComma(char c)
    {
        return c == ',';
    }

    /**
     * Method: isPeriod
     * The method determines whether the given char is a period [.].
     *
     * @param c the character to check
     * @return  true if the character is a period; otherwise false
     */
    public static boolean isPeriod(char c)
    {
        return c == '.';
    }
}
