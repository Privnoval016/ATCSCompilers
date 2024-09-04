package ast;

/**
 * A Variable class is an expression that represents a variable. It has a name, and it can
 * retrieve the value of the variable from the environment.
 *
 * @author  Pranav Sukesh
 * @version 10/4/2023
 */
public class Variable extends Expression
{
    private final String name;

    /**
     * Construct a variable with the given name.
     * @param name The name of the variable.
     */
    public Variable(String name)
    {
        this.name = name;
    }

    /**
     * Returns the name of the variable.
     * @return The name of the variable.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the value of the variable in the given environment.
     * @param env   The environment in which to evaluate the expression.
     * @return      The value of the variable (null if the variable is not in the environment).
     */
    public Object eval(environment.Environment env)
    {
        return env.getVariable(name) != null ? env.getVariable(name) : 0;
    }

    /**
     * Compiles the variable into MIPS code. It loads the variable's value into $v0.
     * @param e The Emitter that emits the MIPS code.
     */
    public void compile(Emitter e)
    {

        if (e.isLocalVariable(name))
        {
            e.emit("lw $v0 " + e.getOffset(name) + "($sp)   # load local variable " + name);
        }
        else
        {
            e.emit("lw $v0 var" + name  + "   # load global variable " + name);
        }
    }

}
