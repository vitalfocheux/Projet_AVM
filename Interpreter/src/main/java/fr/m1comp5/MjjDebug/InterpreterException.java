package fr.m1comp5.MjjDebug;

import fr.m1comp5.custom.exception.VisitorException;

public class InterpreterException extends VisitorException
{
  private CallStack callStack;

  public InterpreterException(String message, int line, int column)
  {
    this(message, line, column, null);
  }

  public InterpreterException(String message, int line, int column, CallStack callStack)
  {
    super(message, line, column);
    this.callStack = callStack;
  }

  public CallStack getCallStack()
  {
    return callStack;
  }

}
