package fr.m1comp5.Debug;

import java.util.ArrayList;
import java.util.List;

/**
 *  class for debugging the interpreter.
 * Manages breakpoints, event handling, and interpreter control.
 */
public class InterpreterDebugger {
    protected Debugger interpreter;
    protected List<fr.m1comp5.mjj.generated.Node> breakpointNodesM;
    protected List<fr.m1comp5.jjc.generated.Node> breakpointNodesC;
    protected DebugEventHandler eventHandler;
    protected Thread interpreterThread;
    protected boolean paused = false;
    protected Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    /**
     * Constructor initializes the list of breakpoint nodes.
     */
    public InterpreterDebugger() {
        breakpointNodesM = new ArrayList<>();
        breakpointNodesC = new ArrayList<>();
    }

    /**
     * Sets the interpreter and clears existing breakpoints.
     * @param interpreter the interpreter to be debugged
     */
    public void setInterpreter(Debugger interpreter) {
        this.interpreter = interpreter;
        interpreter.setDebugger(this);
        breakpointNodesM.clear();
        breakpointNodesC.clear();
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
    public void addBreakpointMjj(fr.m1comp5.mjj.generated.Node breakpointNode) {
        breakpointNodesM.add(breakpointNode);
    }

    public void addBreakpointjjc(fr.m1comp5.jjc.generated.Node breakpointNode) {
        breakpointNodesC.add(breakpointNode);
    }

    /**
     * Removes a breakpoint from the specified node.
     * @param //breakpointNode the node where the breakpoint is removed
     */
    public void removeBreakpointMjj(fr.m1comp5.mjj.generated.Node n) {
        breakpointNodesM.remove(n);
    }

    public void removeBreakpointjcc(fr.m1comp5.jjc.generated.Node n) {
        breakpointNodesC.remove(n);
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
    public void triggerEventHandler(boolean isFinished, fr.m1comp5.jjc.generated.Node  node) {
        if (eventHandler != null) {
            eventHandler.handle(isFinished, node);
        }
    }
    public void triggerEventHandler(boolean isFinished, fr.m1comp5.mjj.generated.Node  node) {
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
    public synchronized void onNodeVisitJJC(fr.m1comp5.jjc.generated.Node node) throws InterruptedException {
        if (breakpointNodesC.contains(node)) {
            paused = true;
            triggerEventHandler(false, node);
            while (paused) {
                wait();
            }
        }
    }
    public synchronized void onNodeVisitMJJ(fr.m1comp5.mjj.generated.Node node) throws InterruptedException {
        if (breakpointNodesM.contains(node)) {
            paused = true;
            triggerEventHandler(false, node);
            while (paused) {
                wait();
            }
        }
    }
}