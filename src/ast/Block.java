package ast;

import java.util.*;
import environment.Environment;
/**
 * A Block class is a list of statements that are executed sequentially.
 *
 * @author  Pranav Sukesh
 * @version 10/4/2023
 */
public class Block extends Statement
{
    private final List<Statement> statementList;

    /**
     * The constructor of the Block class.
     */
    public Block()
    {
        statementList = new ArrayList<>();
    }

    /**
     * Adds a statement to the block.
     * @param statement     The statement to be added.
     */
    public void add(Statement statement)
    {
        statementList.add(statement);
    }

    /**
     * Executes the block of statements.
     * @param env   The environment in which to execute the block.
     */
    public void exec(Environment env)
    {
        for (Statement statement : statementList)
        {
            statement.exec(env);
        }
    }

    /**
     * Compiles the block of statements into MIPS code. It compiles each statement in the block and
     * writes the MIPS code to the file.
     * @param e The Emitter that emits the MIPS code.
     */
    public void compile(Emitter e)
    {
        for (Statement statement : statementList)
        {
            statement.compile(e);
        }
    }


}
