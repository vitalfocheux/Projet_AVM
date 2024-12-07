package fr.m1comp5.MjjDebug;

import java.util.ArrayList;
import java.util.List;

import fr.m1comp5.mjj.generated.Node;

/**
 * Abstract class for debugging the interpreter.
 * Manages breakpoints, event handling, and interpreter control.
 */
public abstract class InterpreterDebugger {
    protected Debugger interpreter;
    protected List<Node> breakpointNodes;
    protected DebugEventHandler eventHandler;
    protected Thread interpreterThread;
    protected boolean paused = false;
    protected Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    /**
     * Constructor initializes the list of breakpoint nodes.
     */
    public InterpreterDebugger() {
        breakpointNodes = new ArrayList<>();
    }

    /**
     * Sets the interpreter and clears existing breakpoints.
     * @param interpreter the interpreter to be debugged
     */
    public void setInterpreter(Debugger interpreter) {
        this.interpreter = interpreter;
        interpreter.setDebugger(this);
        breakpointNodes.clear();
    }

    /**
     * Starts the interpreter in a new thread.
     */
    public void startInterpreter() {
        interpreterThread = new Thread(interpreter);
        if (uncaughtExceptionHandler != null) {
            interpreterThread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        }
        interpreterThread.start();
    }

    /**
     * Stops the interpreter by interrupting its thread.
     */
    public void stopInterpreter() {
        if (interpreterThread != null) {
            interpreterThread.interrupt();
        }
    }

    /**
     * Resumes the interpreter execution from a paused state.
     */
    public synchronized void nextInterpret() {
        paused = false;
        notify();
    }

    /**
     * Adds a breakpoint at the specified node.
     * @param breakpointNode the node where the breakpoint is set
     */
    public void addBreakpoint(Node breakpointNode) {
        breakpointNodes.add(breakpointNode);
    }

    /**
     * Removes a breakpoint from the specified node.
     * @param breakpointNode the node where the breakpoint is removed
     */
    public void removeBreakpoint(Node breakpointNode) {
        breakpointNodes.remove(breakpointNode);
    }

    /**
     * Sets the event handler for debug events.
     * @param eventHandler the event handler to be set
     */
    public void setEventHandler(DebugEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    /**
     * Sets the uncaught exception handler for the interpreter thread.
     * @param exceptionHandler the exception handler to be set
     */
    public void setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler exceptionHandler) {
        this.uncaughtExceptionHandler = exceptionHandler;
    }

    /**
     * Triggers the event handler with the specified parameters.
     * @param isFinished indicates if the interpreter has finished execution
     * @param node the current node being visited
     */
    public void triggerEventHandler(boolean isFinished, Node node) {
        if (eventHandler != null) {
            eventHandler.handle(isFinished, node);
        }
    }

    /**
     * Called when a node is visited by the interpreter.
     * Pauses execution if the node is a breakpoint.
     * @param node the node being visited
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public synchronized void onNodeVisit(Node node) throws InterruptedException {
        if (breakpointNodes.contains(node)) {
            paused = true;
            triggerEventHandler(false, node);
            while (paused) {
                wait();
            }
        }
    }
}