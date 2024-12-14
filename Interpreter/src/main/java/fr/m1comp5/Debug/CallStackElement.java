package fr.m1comp5.MjjDebug;

import fr.m1comp5.mjj.generated.ASTIdent;
import fr.m1comp5.mjj.generated.ASTMethode;

public class CallStackElement
{
    private final String functionName;
    private final int line;
    private final int column;

    public CallStackElement(String functionName, int line, int column)
    {
        this.functionName = functionName;
        this.line = line;
        this.column = column;
    }

    public String getFunctionName()
    {
        return functionName;
    }

    public int getLine()
    {
        return line;
    }

    public int getColumn()
    {
        return column;
    }
}
