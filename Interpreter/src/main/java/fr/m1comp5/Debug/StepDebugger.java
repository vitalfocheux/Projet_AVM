package fr.m1comp5.Debug;

/* 
* step-into debugging functionality. It pauses execution at each node
* Called when a node is visited by the interpreter.
*/
public class StepDebugger extends InterpreterDebugger { 
    @Override
    public synchronized void onNodeVisitMJJ(fr.m1comp5.mjj.generated.Node node) throws InterruptedException { // Renommé onVisit -> onNodeVisit
        super.onNodeVisitMJJ(node);
        // Trigger the event handler when a node is visited
        triggerEventHandler(false, node); 
        // Use a loop to pause the execution at each node
        while (paused) { 
            wait(); // make thread waiting
        }
    }

    @Override
    public synchronized void onNodeVisitJJC(fr.m1comp5.jjc.generated.Node node) throws InterruptedException { // Renommé onVisit -> onNodeVisit
        super.onNodeVisitJJC(node);
        // Trigger the event handler when a node is visited
        triggerEventHandler(false, node); 
        // Use a loop to pause the execution at each node
        while (paused) { 
            wait(); // make thread waiting
        }
    }
}
