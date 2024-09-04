package ast;

/**
 * The Assignment class is used to represent an assignment statement. An assignment statement
 * assigns an expression to a variable, and it is stored in the given environment. Alternatively,
 * if the expression is a procedure call, then the procedure call is executed.
 *
 * @author  Pranav Sukesh
 * @version 10/4/2023
 */
public class Assignment extends Statement
{
    private final String variable;
    private final Expression expression;

    /**
     * The constructor of the Assignment class.
     * @param variable      The variable to be assigned.
     * @param expression    The expression to be assigned to the variable.
     */
    public Assignment(String variable, Expression expression)
    {
        this.variable = variable;
        this.expression = expression;
    }

    /**
     * Assigns the expression to the variable in the given environment. If the expression is a
     * procedure call, then the procedure call is executed.
     * @param env   The environment in which the assignment is executed.
     */
    public void exec(environment.Environment env)
    {
        if (expression instanceof ProcedureCall)
            expression.eval(env);
        else
            env.setVariable(variable, expression.eval(env));
    }

    /**
     * Compiles the assignment into MIPS code. If the variable is a local variable, it stores
     * the contents on the stack, but if it is a global variable, it stores the contents in
     * the symbol table.
     * @param e The Emitter that emits the MIPS code.
     */
    public void compile(Emitter e)
    {
        expression.compile(e);

        if (e.isLocalVariable(variable))
        {
            e.emit("sw $v0 " + e.getOffset(variable) + "($sp)   " +
                    "# store local variable " + variable);
        }
        else
        {
            e.emit("la $t0 var" + variable);
            e.emit("sw $v0 ($t0)    # store global variable " + variable);
        }
    }
}
