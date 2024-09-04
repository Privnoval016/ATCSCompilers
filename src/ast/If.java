package ast;

/**
 * The If class is used to represent an if statement, which executes a statement if a condition is
 * true. If the condition is false, then the statement is not executed. The if statement can also
 * have an else statement, which is executed if the condition is false.
 * @author  Pranav Sukesh
 * @version 10/4/2023
 */
public class If extends Statement
{
    private final Condition condition;
    private final Statement trueStatement;
    private final Statement falseStatement;

    /**
     * The constructor of the If class.
     * @param condition         The condition to be evaluated.
     * @param trueStatement     The statement to be executed if the condition is true.
     * @param falseStatement    The statement to be executed if the condition is false.
     */
    public If(Condition condition, Statement trueStatement, Statement falseStatement)
    {
        this.condition = condition;
        this.trueStatement = trueStatement;
        this.falseStatement = falseStatement;
    }

    /**
     * Executes the if statement. If the condition is true, then the true statement is executed.
     * Otherwise, the false statement is executed (if it exists).
     * @param env   The environment in which the if statement is executed.
     */
    public void exec(environment.Environment env)
    {
        if (condition.eval(env).equals(true))
        {
            trueStatement.exec(env);
        }
        else
        {
            if (falseStatement != null)
                falseStatement.exec(env);
        }
    }

    /**
     * Compiles the if statement into MIPS code. It compiles the condition, the true statement, and
     * the false statement (if it exists).
     * @param e The Emitter that emits the MIPS code.
     */
    public void compile(Emitter e)
    {
        String falseLabel = "endif" + e.nextLabelID();
        String endLabel = "endif" + e.nextLabelID();
        condition.compile(e, falseLabel);
        trueStatement.compile(e);
        e.emit("j " + endLabel);
        e.emit(falseLabel + ":");
        if (falseStatement != null)
            falseStatement.compile(e);
        e.emit(endLabel + ":");
    }
}
