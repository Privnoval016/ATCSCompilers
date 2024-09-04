package ast;

import environment.Environment;

/**
 * An expression is a series of symbols that evaluates to a value.
 * @author  Pranav Sukesh
 * @version 10/4/2023
 */
public abstract class Expression
{
    /**
     * Evaluate the expression.
     * @param env   The environment in which to evaluate the expression.
     */
    public abstract Object eval(Environment env);


    /**
     * Compiles the expression into MIPS code.
     * @param e The Emitter that emits the MIPS code.
     */
    public void compile(Emitter e)
    {
        // do nothing
    }

}

