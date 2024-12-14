package fr.m1comp5.Debug;

import java.util.EmptyStackException;
import java.util.Stack;

public class CallStack
{
    private Stack<CallStackElement> calls;

    public CallStack()
    {
        calls = new Stack<>();
    }

    public void pushFunction(String methID, int line, int column)
    {
        calls.push(new CallStackElement(methID, line, column));
    }

    public void tryPopFunction()
    {
        try
        {
            calls.pop();
        }
        catch (EmptyStackException ignored)
        {
        }
    }
}
