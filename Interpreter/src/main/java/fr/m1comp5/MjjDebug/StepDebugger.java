package fr.m1comp5.MjjDebug;

import fr.m1comp5.mjj.generated.Node;

/* 
* step-into debugging functionality. It pauses execution at each node
* Called when a node is visited by the interpreter.
*/
public class StepDebugger extends InterpreterDebugger { 
    @Override
    public synchronized void onNodeVisit(Node node) throws InterruptedException { // Renommé onVisit -> onNodeVisit
        super.onNodeVisit(node);
        // Trigger the event handler when a node is visited
        triggerEventHandler(false, node); 
        // Use a loop to pause the execution at each node
        while (paused) { 
            wait(); // make thread waiting
        }
    }
}
