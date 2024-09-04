package ast;
import environment.Environment;

import java.util.*;

/**
 * A ProcedureDeclaration class represents a procedure declaration. It has a name and a block of
 * statements, and can be executed by a ProcedureCall. Additionally, it has a list of parameters
 * that are assigned to the procedure's parameters when the procedure is called. Before the
 * program is run, all procedure declarations are stored in the environment.
 *
 * @author  Pranav Sukesh
 * @version 10/4/2023
 */
public class ProcedureDeclaration extends Statement
{
    private final String name;
    private final Statement block;
    private final List<String> parameters;
    private final List<Variable> localVariables;

    /**
     * Constructs a procedure declaration with the given name to be stored in the environment.
     *
     * @param name          The name of the procedure.
     * @param block         The block of statements.
     * @param parameters    The parameters of the procedure.
     * @param localVariables The local variables of the procedure.
     */
    public ProcedureDeclaration(String name, Statement block, List<String> parameters,
                                List<Variable> localVariables)
    {
        this.name = name;
        this.block = block;
        this.parameters = parameters;
        this.localVariables = localVariables;
    }

    /**
     * Returns the name of the procedure.
     *
     * @return The name of the procedure.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the list of parameters of the procedure.
     *
     * @return The list of parameters of the procedure.
     */
    public List<String> getParameters()
    {
        return parameters;
    }

    /**
     * Returns the list of local variables of the procedure.
     *
     * @return The list of local variables of the procedure.
     */
    public List<Variable> getLocalVariables()
    {
        return localVariables;
    }

    /**
     * Adds the procedure declaration to the environment.
     *
     * @param env The environment in which to execute the statement.
     */
    public void exec(environment.Environment env)
    {
        block.exec(env);
    }

    /**
     * Returns the block of statements of the procedure.
     *
     * @return The block of statements of the procedure.
     */
    public Statement getBlock()
    {
        return block;
    }

    /**
     * Compiles the procedure declaration into MIPS code. The stack is as follows: parameters,
     * return value, local variables, return address. The procedure declaration is compiled by
     * emitting the MIPS code for the block of statements, and then returning to the caller.
     *
     * @param e The Emitter that emits the MIPS code.
     */
    public void compile(Emitter e)
    {
        e.emit("proc" + name + ":");


        //e.emit("li $v0 0    #sets return value to 0");
        e.emitPush("$zero");

        e.setProcedureContext(this);

        for (Variable localVar : localVariables)
        {
            e.emitPush("$zero");
        }

        e.emitPush("$ra");


        block.compile(e);

        e.emitPop("$ra");
        e.emit("lw $v0 " + e.getOffset(name) + "($sp)   #load return value");
        e.emit("jr $ra  #return to caller");

        e.clearProcedureContext();
    }


}
