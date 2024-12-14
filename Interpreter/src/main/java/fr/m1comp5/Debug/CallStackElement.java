package fr.m1comp5.Debug;

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
