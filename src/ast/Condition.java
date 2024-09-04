package ast;

import environment.Environment;
/**
 * A Condition class is an expression that represents a boolean expression. It has a left
 * expression, a right expression, and a relational operator. The relational operator can be <,
 * <=, >, >=, =, or <>.
 *
 * @author  Pranav Sukesh
 * @version 10/4/2023
 *
 */
public class Condition extends Expression
{
    private final Expression left;
    private final Expression right;
    private final String operator;

    /**
     * Construct a condition with the given left and right expressions and the given relational
     * operator.
     * @param left The left expression.
     * @param operator The operator.
     * @param right The right expression.
     */
    public Condition(Expression left, String operator, Expression right)
    {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    /**
     * Evaluates the condition given a relational operator. It returns true if the condition is
     * true, and false otherwise.
     * @param env   The environment in which to evaluate the expression.
     * @return      The result of the binary operation.
     */
    public Object eval(Environment env)
    {
        int leftValue = (Integer)left.eval(env);
        int rightValue = (Integer)right.eval(env);
        return switch (operator)
        {
            case "<" -> leftValue < rightValue;
            case "<=" -> leftValue <= rightValue;
            case ">" -> leftValue > rightValue;
            case ">=" -> leftValue >= rightValue;
            case "=" -> leftValue == rightValue;
            case "<>" -> leftValue != rightValue;
            default -> null;
        };
    }

    /**
     * Compiles the condition into MIPS code. It compiles the left expression, pushes the result
     * onto the stack, compiles the right expression, pops the left expression off the stack, and
     * then performs the binary operation in reverse, jumping to the given label if the condition is
     * false.
     * @param e     The Emitter that emits the MIPS code.
     * @param label The label to jump to if the condition is false.
     */
    public void compile(Emitter e, String label)
    {
        left.compile(e);
        e.emitPush("$v0");
        right.compile(e);
        e.emitPop("$t0");
        switch (operator)
        {
            case "<":
                e.emit("bge $t0 $v0 " + label);
                break;
            case "<=":
                e.emit("bgt $t0 $v0 " + label);
                break;
            case ">":
                e.emit("ble $t0 $v0 " + label);
                break;
            case ">=":
                e.emit("blt $t0 $v0 " + label);
                break;
            case "=":
                e.emit("bne $t0 $v0 " + label);
                break;
            case "<>":
                e.emit("beq $t0 $v0 " + label);
                break;
        }
    }
}
