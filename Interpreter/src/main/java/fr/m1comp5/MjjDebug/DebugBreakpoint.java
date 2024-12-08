package fr.m1comp5.MjjDebug;

import fr.m1comp5.mjj.generated.Node;
/**
 * breakpoint functionality. It pauses execution when a breakpoint is hit.
 */

public class DebugBreakpoint extends InterpreterDebugger {
    @Override
    public synchronized void onNodeVisit(Node node) throws InterruptedException {
        if (breakpointNodes.contains(node)) {
            super.onNodeVisit(node);
            // Trigger when a node is visited and it is a breakpoint node
            triggerEventHandler(false, node);
            // Use a loop to pause the execution at the breakpoint node
            while (paused) {
                wait(); // make thread waiting
            }
        }
    }
}