package fr.m1comp5.custom.exception;

public class VisitorException extends RuntimeException
{
    private int line;
    private int column;

    public VisitorException(String message, int line, int column)
    {
        super(message);
        this.line = line;
        this.column = column;
    }

    private int getExceptionColumn()
    {
        return column;
    }

    private int getExceptionLine()
    {
        return line;
    }
}
