package ast;

import environment.Environment;
/**
 * The Writeln class is used to represent a writeln statement.
 * It contains an expression to be printed.
 *
 * @author  Pranav Sukesh
 * @version 10/4/2023
 */
public class Writeln extends Statement
{
    private final Expression expression;

    /**
     * The constructor of the Writeln class.
     * @param expression    The expression to be printed.
     */
    public Writeln(Expression expression)
    {
        this.expression = expression;
    }

    /**
     * Evaluates the expression by printing it.
     * @param env   The environment in which the expression is evaluated.
     */
    public void exec(Environment env)
    {
        System.out.println(expression.eval(env));
    }

    /**
     * Compiles the writeln statement into MIPS code. It compiles the expression and prints it.
     * @param e The Emitter that emits the MIPS code.
     */
    public void compile(Emitter e)
    {
        expression.compile(e);
        e.emit("move $a0 $v0");
        e.emit("li $v0 1");
        e.emit("syscall");
        e.emit("la $a0 newline");
        e.emit("li $v0 4");
        e.emit("syscall #printed something");
    }

}
