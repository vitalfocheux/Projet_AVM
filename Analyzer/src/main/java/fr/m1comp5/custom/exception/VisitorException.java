package fr.m1comp5.custom.exception;

public class VisitorException extends Exception
{
    private final int line;
    private final int column;

    public VisitorException(String message, int line, int column)
    {
        super(message);
        this.line = line;
        this.column = column;
    }

    public int getExceptionColumn()
    {
        return column;
    }

    public int getExceptionLine()
    {
        return line;
    }
}
