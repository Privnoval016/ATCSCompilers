package scanner;

/**
 * A token is a string that represents a single unit of a program. It is used by the scanner to
 * tokenize the input file. It is also used by the parser to check for syntactical correctness.
 *
 * @author  Pranav Sukesh
 * @version 9/4/2023
 */
public class Token
{
    private final String token;
    private final Scanner.TOKEN_TYPE type;

    private int lineNum;
    private int charNum;

    /**
     * The constructor of the Token class. It takes in a string representing the token and a
     * TokenType representing the type of the token.
     * @param token the string representing the token
     * @param type the TokenType representing the type of the token
     */
    public Token(String token, Scanner.TOKEN_TYPE type)
    {
        this.token = token;
        this.type = type;
    }

    /**
     * The constructor of the Token class. It takes in a string representing the token and a
     * TokenType representing the type of the token.
     * @param token the string representing the token
     * @param type the TokenType representing the type of the token
     * @param lineNum the line number of the token
     * @param charNum the character number of the token
     */
    public Token(String token, Scanner.TOKEN_TYPE type, int lineNum, int charNum)
    {
        this.token = token;
        this.type = type;

        this.lineNum = lineNum;
        this.charNum = charNum;
    }

    /**
     * The getToken method returns the string representing the token.
     * @return the string representing the token
     */
    public String getToken()
    {
        return token;
    }

    /**
     * The getType method returns the TokenType representing the type of the token.
     * @return the TokenType representing the type of the token
     */
    public Scanner.TOKEN_TYPE getType()
    {
        return type;
    }

    /**
     * The toString method returns the string representation of the token.
     * @return the string representation of the token
     */
    public String toString()
    {
        if (type == Scanner.TOKEN_TYPE.EOF)
            return "EOF";
        return type + ": " + token;
    }

    /**
     * The getPosition method returns the position of the token in the input file.
     * @return the position of the token in the input file
     */
    public String getPosition()
    {
        return "Line " + lineNum + ", Character " + charNum;
    }
}
