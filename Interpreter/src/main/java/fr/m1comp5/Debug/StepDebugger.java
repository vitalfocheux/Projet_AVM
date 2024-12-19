package fr.m1comp5.Debug;

public class StepDebugger extends InterpreterDebugger {
    @Override
    public synchronized void onNodeVisitMJJ(fr.m1comp5.mjj.generated.Node node) throws InterruptedException {
        super.onNodeVisitMJJ(node);
        paused = true;
        if (eventHandler != null) {
            eventHandler.handle(false, node);
        }
        while (paused) {
            wait();
        }
    }

    @Override
    public synchronized void onNodeVisitJJC(fr.m1comp5.jjc.generated.Node node) throws InterruptedException {
        super.onNodeVisitJJC(node);
        paused = true;
        if (eventHandler != null) {
            eventHandler.handle(false, node);
        }
        while (paused) {
            wait();
        }
    }
}