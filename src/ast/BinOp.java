package ast;

import environment.Environment;

/**
 * A BinOp class represents a binary operation. It has a left expression, a right expression,
 * and an operator. The operator can be +, -, *, /, or %.
 *
 * @author Pranav Sukesh
 * @version 10/4/2023
 */
public class BinOp extends Expression
{
    private final Expression left;
    private final Expression right;
    private final String operator;

    /**
     * Construct a binary operation with the given left and right expressions and the given
     * operator.
     *
     * @param left     The left expression.
     * @param operator The operator.
     * @param right    The right expression.
     */
    public BinOp(Expression left, String operator, Expression right)
    {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    /**
     * Evaluates the binary operation and returns the result.
     *
     * @param env The environment in which to evaluate the expression.
     * @return The result of the binary operation.
     */
    public Object eval(Environment env)
    {
        int leftValue = (int) left.eval(env);
        int rightValue = (int) right.eval(env);
        return switch (operator)
        {
            case "+" -> leftValue + rightValue;
            case "-" -> leftValue - rightValue;
            case "*" -> leftValue * rightValue;
            case "/" -> leftValue / rightValue;
            case "mod" -> leftValue % rightValue;
            default -> null;
        };
    }

    /**
     * Compiles the binary operation into MIPS code. It compiles the left expression, pushes the
     * result onto the stack, compiles the right expression, pops the left expression off the stack,
     * and then performs the binary operation and stores the result in $v0.
     *
     * @param e The Emitter that emits the MIPS code.
     */
    public void compile(Emitter e)
    {
        left.compile(e);
        e.emitPush("$v0");
        right.compile(e);
        e.emitPop("$t0");
        switch (operator)
        {
            case "+" -> e.emit("addu $v0 $t0 $v0 #adds the values and stores in $v0");
            case "-" -> e.emit("subu $v0 $t0 $v0 #subtracts the values and stores in $v0");
            case "*" ->
            {
                e.emit("mult $v0 $t0 #multiplies the values and stores in " + "$v0");
                e.emit("mflo $v0");
            }
            case "/" ->
            {
                e.emit("div $v0 $t0 #divides the values and stores in $v0");
                e.emit("mflo $v0");
            }
            case "mod" ->
            {
                e.emit("div $v0 $t0 #finds the remainder of the division and stores in $v0");
                e.emit("mfhi $v0");
            }
        }
    }
}
