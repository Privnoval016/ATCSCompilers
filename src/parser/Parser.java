package parser;

import scanner.*;
import scanner.Scanner;
import ast.*;

import java.util.*;


/**
 * The parser class parses a program and converts it into an abstract syntax tree (AST),
 * evaluated through the root node of the AST. It uses a Scanner object to obtain tokens from the
 * source text. The parser supports procedures, block statements, if statements, while statements,
 * assignment statements, writeln statements, binary operations, variable storing, conditions.
 * It uses the following grammar:
 * program → PROCEDURE id ( maybeparms ) ; stmt program | stmt .
 * maybeparms → parms | ε
 * parms → parms , id | id
 * stmt → WRITELN ( expr ) ; | BEGIN stmts END ; | id := expr ;
 * | IF cond THEN stmt | WHILE cond DO stmt
 * stmts → stmts stmt | ε
 * expr → term whileexp
 * whileexp → + term whileexp | - term whileexp | ε
 * term → factor whileterm
 * whileterm → * factor whileterm | / factor whileterm | ε
 * factor → ( expr ) | - factor | num | id ( maybeargs ) | id
 * maybeargs → args | ε
 * args → args , expr | expr
 * cond → expr relop expr
 * relop → = | <> | < | > | <= | >=
 *
 * @author  Pranav Sukesh
 * @version 9/25/2023
 */
public class Parser
{
    private final Scanner scanner;
    private Token currentToken;

    private final List<Variable> vars;
    /**
     * The constructor of the Parser class. It takes in a Scanner object.
     * @param scanner the Scanner object
     */
    public Parser(Scanner scanner) throws ScanErrorException
    {
        this.scanner = scanner;
        currentToken = scanner.nextToken();
        vars = new ArrayList<>();
    }


    /**
     * The eat method takes in a string representing the expected token. If the expected token
     * matches the current token, then the next token is obtained and is stored as the current
     * token. Otherwise, it throws an IllegalArgumentException.
     * @param token the expected token
     * @throws IllegalArgumentException if the expected token does not match the current token
     */
    private void eat(String token) throws ScanErrorException
    {
        if (currentToken.getToken().equals(token))
        {
            currentToken = scanner.nextToken();
        }
        else
        {
            throw new IllegalArgumentException("Expected " + token + " but found " + currentToken);
        }
    }

    /**
     * The parseNumber method parses a number, which is a sequence of digits.
     *
     * @precondition  the current token is a number
     * @postcondition the current token is the next token after the number
     * @return        the number
     */
    private Expression parseNumber() throws ScanErrorException
    {
        int number = Integer.parseInt(currentToken.getToken());

        eat(currentToken.getToken());

        return new ast.Number(number);
    }


    /**
     * The parseProgram method keeps parsing ProcedureDeclarations until it reaches the block of
     * statements to execute. It adds all the ProcedureDeclarations to the Program's stored
     * environment. Then, it parses the statement block that is to be executed.
     * Its grammar is:
     * program → PROCEDURE id ( maybeparms ) ; stmt program | stmt .
     * maybeparms → parms | ε
     * parms → parms , id | id
     *
     * @precondition    the current token is a program
     * @postcondition   the current token is the next token after the program
     * @return          the program with the block of statements to execute and the list of
     *                  ProcedureDeclarations
     */
    public Program parseProgram() throws ScanErrorException
    {
        List<ProcedureDeclaration> procs = new ArrayList<>();
        while (currentToken.getToken().equals("VAR"))
        {
            eat("VAR");
            vars.add(new Variable(currentToken.getToken()));
            eat(currentToken.getToken());
            while (currentToken.getToken().equals(","))
            {
                eat(",");
                vars.add(new Variable(currentToken.getToken()));
                eat(currentToken.getToken());
            }
            eat(";");
        }
        while (currentToken.getToken().equals("PROCEDURE"))
        {
            eat("PROCEDURE");
            String name = currentToken.getToken();
            eat(name);
            eat("(");
            List<String> parameters = new ArrayList<>();
            if (!currentToken.getToken().equals(")"))
            {
                parameters.add(currentToken.getToken());
                eat(currentToken.getToken());
                while (currentToken.getToken().equals(","))
                {
                    eat(",");
                    parameters.add(currentToken.getToken());
                    eat(currentToken.getToken());
                }
            }
            eat(")");
            eat(";");

            List<Variable> localVariables = new ArrayList<>();
            while (currentToken.getToken().equals("VAR"))
            {
                eat("VAR");
                localVariables.add(new Variable(currentToken.getToken()));
                eat(currentToken.getToken());
                while (currentToken.getToken().equals(","))
                {
                    eat(",");
                    localVariables.add(new Variable(currentToken.getToken()));
                    eat(currentToken.getToken());
                }
                eat(";");
            }

            procs.add(new ProcedureDeclaration(name, parseStatement(), parameters, localVariables));
        }


        return new Program(parseStatement(), procs, vars);
    }

    /**
     * The parseStatement method parses a statement, which can be a WRITELN statement, a
     * BEGIN statement, an IF statement, a WHILE statement, or an assignment statement.
     * Its grammar is:
     * stmt → WRITELN(expr); |BEGIN stmts END;| id := expr;
     * | IF cond THEN stmt ELSE stmt | WHILE cond DO stmt
     * whilebegin → END ; | stmt whilebegin
     * cond → expr relop expr
     * relop → = | <> | < | > | <= | >=
     *
     *
     * @precondition    the current token is a statement
     * @postcondition   the current token is the next token after the statement
     * @return          the statement
     *
     */
    public Statement parseStatement() throws ScanErrorException
    {
        Statement statement = null;
        if (currentToken.getToken().equals("VAR"))
        {
            eat("VAR");
            vars.add(new Variable(currentToken.getToken()));
            eat(currentToken.getToken());
            while (currentToken.getToken().equals(","))
            {
                eat(",");
                vars.add(new Variable(currentToken.getToken()));
                eat(currentToken.getToken());
            }
            eat(";");
            return parseStatement();
        }
        else if (currentToken.getToken().equals("WRITELN"))
        {
            eat("WRITELN");
            eat("(");
            statement = new Writeln(parseExpression());
            eat(")");
            eat(";");
        }
        else if (currentToken.getToken().equals("BEGIN"))
        {
            eat("BEGIN");
            statement = new Block();
            while (!currentToken.getToken().equals("END"))
            {
                ((Block) statement).add(parseStatement());
            }
            eat("END");
            eat(";");
        }
        else if (currentToken.getType().equals(Scanner.TOKEN_TYPE.ID))
        {
            if (currentToken.getToken().equals("IF"))
            {
                eat("IF");
                Expression left = parseExpression();
                String operator = currentToken.getToken();
                eat(operator);
                Expression right = parseExpression();
                Condition condition = new Condition(left, operator, right);
                eat("THEN");
                Statement trueStatement = parseStatement();
                Statement falseStatement = null;
                if (currentToken.getToken().equals("ELSE"))
                {
                    eat("ELSE");
                    falseStatement = parseStatement();
                }
                statement = new If(condition, trueStatement, falseStatement);
            }
            else if (currentToken.getToken().equals("WHILE"))
            {
                eat("WHILE");
                Expression left = parseExpression();
                String operator = currentToken.getToken();
                eat(operator);
                Expression right = parseExpression();
                Condition condition = new Condition(left, operator, right);
                eat("DO");
                Statement whileStatement = parseStatement();
                statement = new While(condition, whileStatement);
            }
            else
            {
                String id = currentToken.getToken();
                eat(id);
                eat(":=");


                statement = new Assignment(id, parseExpression());
                eat(";");

            }
        }


        return statement;
    }

    /**
     * The parseFactor method parses a factor, which is any expression we can multiply or divide.
     * Its grammar is:
     * factor → ( expr ) | - factor | num | id ( maybeargs ) | id
     * maybeargs → args | ε
     * args → args , expr | expr
     * The method returns the value of the factor.
     *
     * @precondition    the current token is a factor
     * @postcondition   the current token is the next token after the factor
     * @return          the value of the factor
     */
    private Expression parseFactor() throws ScanErrorException
    {
        if (currentToken.getToken().equals("("))
        {
            eat("(");
            Expression value = parseExpression();
            eat(")");
            return value;
        }
        else if (currentToken.getToken().equals("-"))
        {
            eat("-");
            return new BinOp(new ast.Number(0), "-", parseFactor());
        }
        else if (currentToken.getType().equals(Scanner.TOKEN_TYPE.ID))
        {
            Expression exp = null;
            String id = currentToken.getToken();
            eat(id);
            if (currentToken.getToken().equals("("))
            {
                eat("(");
                List<Expression> parameters = new ArrayList<>();
                if (!currentToken.getToken().equals(")"))
                {
                    parameters.add(parseExpression());
                    while (currentToken.getToken().equals(","))
                    {
                        eat(",");
                        parameters.add(parseExpression());
                    }
                }
                eat(")");
                exp = new ProcedureCall(id, parameters);
            }
            else
            {
                exp = new Variable(id);
            }
            return exp;

        }
        else
        {
            return parseNumber();
        }

    }

    /**
     * The parseTerm method parses a term, which is any expression with multiplication and
     * division of numbers.
     * Its grammar is:
     * term → factor whileterm
     * whileterm → * factor whileterm | / factor whileterm | ε
     * The method returns the value of the term.
     *
     * @precondition    the current token is a term
     * @postcondition   the current token is the next token after the term
     * @return          the value of the term
     */
    private Expression parseTerm() throws ScanErrorException
    {
        Expression value = parseFactor();

        while (currentToken.getToken().equals("*") || currentToken.getToken().equals("/"))
        {
            if (currentToken.getToken().equals("*"))
            {
                eat("*");
                value = new BinOp(value, "*", parseFactor());
            }
            else if (currentToken.getToken().equals("/"))
            {
                eat("/");
                value = new BinOp(value, "/", parseFactor());
            }
        }

        return value;
    }

    /**
     * The parseExpression method parses an expression, which is any expression with addition,
     * subtraction, multiplication, and division of numbers.
     * Its grammar is:
     * expression → term whileexp
     * whileexp → + term whileexp | - term whileexp | ε
     * The method returns the value of the expression.
     *
     * @precondition    the current token is an expression
     * @postcondition   the current token is the next token after the expression
     * @return          the value of the expression
     */
    private Expression parseExpression() throws ScanErrorException
    {
        Expression value = parseTerm();

        while (currentToken.getToken().equals("+") || currentToken.getToken().equals("-"))
        {
            if (currentToken.getToken().equals("+"))
            {
                eat("+");
                value = new BinOp(value, "+", parseTerm());
            }
            else if (currentToken.getToken().equals("-"))
            {
                eat("-");
                value = new BinOp(value, "-", parseTerm());
            }
        }


        return value;
    }

}
