package ast;

import java.util.*;

/**
 * A Program class represents the root of the AST. It has a block of statements and a list of
 * procedure declarations. Before the program is run, all procedure declarations are stored in the
 * environment.
 *
 * @author  Pranav Sukesh
 * @version 10/4/2023
 */
public class Program
{
    private final Statement block;
    private final List<ProcedureDeclaration> procs;

    private final List<Variable> vars;

    /**
     * Constructs a program with the given block of statements and the given list of procedures.
     * @param block The block of statements.
     * @param procs The list of procedures.
     * @param vars  The list of variables.
     */
    public Program(Statement block, List<ProcedureDeclaration> procs, List<Variable> vars)
    {
        //System.out.println(block);
        this.block = block;
        this.procs = procs;
        this.vars = vars;

    }

    /**
     * Constructs a program with the given block of statements and the given list of procedures.
     * @param block The block of statements.
     * @param procs The list of procedures.
     */
    public Program(Statement block, List<ProcedureDeclaration> procs)
    {
        //System.out.println(block);
        this.block = block;
        this.procs = procs;
        this.vars = null;

    }

    /**
     * Executes the program, which is a block of statements. Before the program is run, all
     * procedure declarations are stored in the environment.
     * @param env   The environment in which to execute the statement.
     */
    public void exec(environment.Environment env)
    {
        for (ProcedureDeclaration proc : procs)
        {
            env.setProcedure(proc.getName(), proc);
        }
        if (vars != null)
        {
            for (Variable var : vars)
            {
                env.setVariable(var.getName(), 0);
            }
        }
        block.exec(env);
    }

    /**
     * The compile method takes in an output file and uses the Emitter class to write the basic
     * components of a MIPS file to the output file. It then compiles the block of statements and
     * writes the MIPS code to the output file.
     *
     * @param outputFileName The name of the output file.
     */
    public void compile(String outputFileName)
    {
        Emitter e = new Emitter(outputFileName);
        e.emit("# This is a MIPS converted version of PASCAL source code that has been run");
        e.emit("# through a compiler in Java.");
        e.emit("# @author Pranav Sukesh");
        e.emit("# @date   " + java.time.LocalDate.now());
        e.emit(".data");
        e.emit("newline: .asciiz \"\\n\"");
        if (vars != null)
        {
            for (Variable var : vars)
            {
                e.emit("var" + var.getName() + ": .word 0");
            }
        }
        e.emit(".text");
        e.emit(".globl main");
        e.emit("main: #QTSPIM will automatically look for main");
        block.compile(e);
        e.emit("li $v0 10");
        e.emit("syscall #halt");
        e.emit("#procedures");
        for (ProcedureDeclaration proc : procs)
        {
            proc.compile(e);
        }
        e.close();
    }
}
