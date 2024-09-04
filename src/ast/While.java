package ast;

import java.util.*;

/**
 * The While class is used to represent a while statement. It executes a statement while a condition
 * is true. If the condition is false, then the statement is not executed.
 * @author  Pranav Sukesh
 * @version 10/4/2023
 */
public class While extends Statement
{
    private final Condition condition;
    private final Statement statement;


    /**
     * The constructor of the While class.
     * @param condition         The condition to be evaluated.
     * @param statement         The statement to be executed if the condition is true.
     */
    public While(Condition condition, Statement statement)
    {
        this.condition = condition;
        this.statement = statement;

    }


    /**
     * Executes the stored statement while the condition is true.
     * @param env   The environment in which the while statement is executed.
     */
    public void exec(environment.Environment env)
    {
        while (condition.eval(env).equals(true))
        {
            statement.exec(env);
        }
    }

    /**
     * Compiles the while statement into MIPS code. It creates a start label and an end label. It
     * compiles the condition, the statement, and a jump back to the start label as long as the
     * condition is true.
     * @param e The Emitter that emits the MIPS code.
     */
    public void compile(Emitter e)
    {
        String startLabel = "while" + e.nextLabelID();
        String endLabel = "endwhile" + e.nextLabelID();
        e.emit(startLabel + ":");
        condition.compile(e, endLabel);
        statement.compile(e);
        e.emit("j " + startLabel);
        e.emit(endLabel + ":");
    }
}
