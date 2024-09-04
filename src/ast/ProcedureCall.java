package ast;
import environment.Environment;
import java.util.*;

/**
 * A ProcedureCall class is an expression that represents a procedure call. A procedure call
 * is a call to an already declared procedure. The procedure call is evaluated by executing
 * the procedure declaration with the same name (stored in the given environment). The parameters
 * of the procedure call are evaluated in the given environment, and the parameters are then
 * assigned to the procedure's parameters in the new environment. The procedure is then executed
 * in the new environment.
 *
 * @author  Pranav Sukesh
 * @version 10/4/2023
 */
public class ProcedureCall extends Expression
{
    private final String name;
    private List<Expression> parameters;

    /**
     * Constructs a procedure call with the given name.
     * @param name          The name of the procedure.
     * @param parameters    The parameters of the procedure.
     */
    public ProcedureCall(String name, List<Expression> parameters)
    {
        this.name = name;
        this.parameters = parameters;
    }

    /**
     * Evaluates the procedure call by executing the ProcedureDeclaration with the same name.
     * A new environment is created for the procedure call, and the parameters are evaluated
     * in the new environment. The parameters are then assigned to the procedure's parameters
     * in the new environment. The procedure is then executed in the new environment.
     * @param env   The environment in which to evaluate the expression.
     * @return      The value of the local variable in the new environment with the same name
     *              as the procedure.
     */
    public Object eval(environment.Environment env)
    {
        ProcedureDeclaration proc = env.getProcedure(name);
        Environment newEnv = new Environment();
        newEnv.declareVariable(name, 0);
        newEnv.setParent(env.getParent() != null ? env.getParent() : env);
        List<String> procParams = proc.getParameters();
        for (int i = 0; procParams != null && i < procParams.size(); i++)
        {
            newEnv.declareVariable(procParams.get(i),
                    parameters.get(i).eval(env));
        }
        List<Variable> localVars = proc.getLocalVariables();
        for (Variable var: localVars)
        {
            newEnv.declareVariable(var.getName(), 0);
        }
        proc.exec(newEnv);
        return newEnv.getVariable(name);
    }

    /**
     * Compiles the procedure call. It first compiles and pushes the parameters onto the 
     * stack, then jumps to the correct procedure declaration. It then pops the parameters
     * afterward.
     * @param e The Emitter that emits the MIPS code.
     */
    public void compile(Emitter e)
    {
        for (Expression param : parameters)
        {
            param.compile(e);
            e.emitPush("$v0");
        }
        e.emit("jal proc" + name + "    # Procedure call");

        for (int i = 0; i < parameters.size(); i++)
        {
            e.emitPop("$t0");
        }
    }
}
