package ast;

import environment.Environment;
/**
 * A Number class is an expression that represents a number.
 *
 * @author  Pranav Sukesh
 * @version 10/4/2023
 */
public class Number extends Expression
{
    private final int value;

    /**
     * The constructor of the Number class.
     * @param value The value of the number.
     */
    public Number(int value)
    {
        this.value = value;
    }


    /**
     * Returns the value of the number.
     * @param env   The environment in which to evaluate the expression.
     * @return      The value of the number.
     */
    public Object eval(Environment env)
    {
        return value;
    }

    /**
     * Compiles the number into MIPS code. It loads the number into $v0.
     * @param e The Emitter that emits the MIPS code.
     */
    public void compile(Emitter e)
    {
        e.emit("li $v0 " + value);
    }
}
