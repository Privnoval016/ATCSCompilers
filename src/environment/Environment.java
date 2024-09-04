package environment;
import java.util.*;

import ast.ProcedureDeclaration;

/**
 * The Environment class stores the values and names of both variables and procedures of a program.
 * It also stores the parent environment, which is used to access variables and procedures that are
 * not in the current environment. The parent environment is set when a new environment is created
 * for a block of statements or a procedure call.
 *
 * @author  Pranav Sukesh
 * @version 10/4/2023
 */
public class Environment
{
    private final Map<String, Object> variables;

    private final Map<String, ProcedureDeclaration> procs;

    private Environment parent;

    /**
     * The constructor of the Environment class.
     */
    public Environment()
    {
        variables = new HashMap<>();
        procs = new HashMap<>();
    }

    /**
     * Sets the value of a variable. If the variable does not exist in this environment but does
     * exist in the parent environment, the variable is set in the parent environment.
     * @param name The name of the variable.
     * @param value The value of the variable.
     */
    public void setVariable(String name, Object value)
    {
        if (variables.containsKey(name))
        {
            variables.put(name, value);
        }
        else if (parent != null)
        {
            parent.setVariable(name, value);
        }
        else
        {
            variables.put(name, value);
        }
    }
    /**
     * Declares a variable to a given value in the current environment.
     * @param name The name of the variable.
     * @param value The value of the variable.
     */
    public void declareVariable(String name, Object value)
    {
        variables.put(name, value);
    }

    /**
     * Returns the value of a variable. If the variable does not exist in this environment but does
     * exist in the parent environment, the variable is returned from the parent environment.
     * @param name The name of the variable.
     * @return The value of the variable.
     */
    public Object getVariable(String name)
    {
        Object result = variables.get(name);
        if (result == null && parent != null)
        {
            result = parent.getVariable(name);
        }
        return result;
    }

    /**
     * Sets the value of a procedure.
     * @param name The name of the procedure.
     * @param value The value of the procedure.
     */
    public void setProcedure(String name, ProcedureDeclaration value)
    {
        procs.put(name, value);
    }

    /**
     * Returns the value of a procedure.
     * @param name The name of the procedure.
     * @return The value of the procedure.
     */
    public ProcedureDeclaration getProcedure(String name)
    {
        //System.out.println(name);
        ProcedureDeclaration result = procs.get(name);
        if (result == null && parent != null)
        {
            result = parent.getProcedure(name);
        }
        return result;
    }

    /**
     * Sets the parent of this environment.
     * @param  parent The parent of this environment.
     */
    public void setParent(Environment parent)
    {
        this.parent = parent;
    }

    /**
     * Gets the parent of this environment.
     * @return The parent of this environment.
     */
    public Environment getParent()
    {
        return parent;
    }
}
