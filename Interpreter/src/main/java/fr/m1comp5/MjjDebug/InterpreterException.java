package fr.m1comp5.MjjDebug;

public class InterpreterException extends RuntimeException
{
    private int line;
    private int column;

    public InterpreterException(String message)
    {
        super(message);
    }

    public InterpreterException(String message, int line, int column)
    {
      super(message);
    }
}
