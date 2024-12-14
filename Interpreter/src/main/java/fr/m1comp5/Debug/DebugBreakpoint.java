package fr.m1comp5.Debug;


/**
 * breakpoint functionality. It pauses execution when a breakpoint is hit.
 */

public class DebugBreakpoint extends InterpreterDebugger {
    @Override
    public synchronized void onNodeVisitJJC(fr.m1comp5.jjc.generated.Node node) throws InterruptedException {
        if (breakpointNodesC.contains(node)) {
            super.onNodeVisitJJC(node);
            // Trigger when a node is visited and it is a breakpoint node
            triggerEventHandler(false, node);
            // Use a loop to pause the execution at the breakpoint node
            while (paused) {
                wait(); // make thread waiting
            }
        }
    }
    @Override
    public synchronized void onNodeVisitMJJ(fr.m1comp5.mjj.generated.Node node) throws InterruptedException {
        if (breakpointNodesM.contains(node)) {
            super.onNodeVisitMJJ(node);
            // Trigger when a node is visited and it is a breakpoint node
            triggerEventHandler(false, node);
            // Use a loop to pause the execution at the breakpoint node
            while (paused) {
                wait(); // make thread waiting
            }
        }
    }
}