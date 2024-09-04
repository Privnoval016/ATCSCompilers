package ast;

import environment.Environment;

/**
 * A statement is a series of symbols that performs an action (it does not evaluate to a value).
 * @author  Pranav Sukesh
 * @version 10/4/2023
 */
public abstract class Statement
{
    /**
     * Execute the statement.
     * @param env   The environment in which to execute the statement.
     */
    public abstract void exec(Environment env);


    /**
     * Compiles the expression into MIPS code.
     * @param e The Emitter that emits the MIPS code.
     */
    public void compile(Emitter e)
    {
        // do nothing
    }
}
