package ast;

import java.io.*;

public class Emitter
{
    private PrintWriter out;
    private int labelCount = 0;
    private int excessStackHeight = 0;

    private ProcedureDeclaration currentProcedure = null;

    //creates an emitter for writing to a new file with given name
    public Emitter(String outputFileName)
    {
        try
        {
            out = new PrintWriter(new FileWriter(outputFileName), true);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    //prints one line of code to file (with non-labels indented)
    public void emit(String code)
    {
        if (!code.endsWith(":"))
            code = "\t" + code;
        out.println(code);
    }

    /**
     * Pushes the given register onto the stack.
     *
     * @param reg The register to push onto the stack.
     */
    public void emitPush(String reg)
    {
        emit("subu $sp $sp 4 #pushes " + reg + " onto the stack");
        emit("sw " + reg + " ($sp)");
        excessStackHeight += 4;
    }

    /**
     * Pops the top of the stack into the given register.
     *
     * @param reg The register to pop into.
     */
    public void emitPop(String reg)
    {
        emit("lw " + reg + " ($sp) #pops the top of the stack into " + reg);
        emit("addu $sp $sp 4");
        excessStackHeight -= 4;

    }

    /**
     * Returns the next label number.
     *
     * @return The next label number.
     */
    public int nextLabelID()
    {
        return labelCount++;
    }

    /**
     * Sets the current procedure to null
     */
    public void clearProcedureContext()
    {
        currentProcedure = null;
    }

    /**
     * Sets the current procedure to the given procedure.
     *
     * @param proc The procedure to set the current procedure to.
     */
    public void setProcedureContext(ProcedureDeclaration proc)
    {
        currentProcedure = proc;
        excessStackHeight = 0;
    }

    /**
     * Returns true if the given variable corresponds to a local variable or a .
     *
     * @param name The name of the variable.
     * @return True if the given variable corresponds to a local variable.
     */
    public boolean isLocalVariable(String name)
    {

        if (currentProcedure != null &&
                (currentProcedure.getParameters().contains(name)
                        || name.equals(currentProcedure.getName())))
        {
            return true;
        }
        if (currentProcedure != null)
        {
            for (Variable localVar : currentProcedure.getLocalVariables())
            {
                if (localVar.getName().equals(name))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines the offset from $sp for the given local variable.
     *
     * @param localVarName The name of the local variable.
     * @return The offset from $sp for the given local variable.
     * @precondition The given variable corresponds to a local variable.
     */
    public int getOffset(String localVarName)
    {
        int offset = excessStackHeight;

        for (int i = 0; i < currentProcedure.getLocalVariables().size(); i++)
        {
            offset -= 4;
            if (currentProcedure.getLocalVariables().get(i).getName().equals(localVarName))
            {
                return offset;
            }
        }

        if (currentProcedure.getName().equals(localVarName))
        {
            return excessStackHeight;
        }

        offset = 4 + excessStackHeight;

        for (int i = currentProcedure.getParameters().size() - 1; i >= 0; i--)
        {
            if (currentProcedure.getParameters().get(i).equals(localVarName))
            {
                return offset;
            }
            offset += 4;
        }

        return offset;

    }


    //closes the file.  should be called after all calls to emit.
    public void close()
    {
        out.close();
    }
}