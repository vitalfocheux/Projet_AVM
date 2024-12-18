package fr.m1comp5.Debug;

public class DebugBreakpoint extends InterpreterDebugger {

    public void addBreakpointMjj(fr.m1comp5.mjj.generated.Node node) {
        breakpointNodesM.add(node);
    }

    public void removeBreakpointMjj(fr.m1comp5.mjj.generated.Node node) {
        breakpointNodesM.remove(node);
    }

    public void addBreakpointJjc(fr.m1comp5.jjc.generated.Node node) {
        breakpointNodesC.add(node);
    }

    public void removeBreakpointJjc(fr.m1comp5.jjc.generated.Node node) {
        breakpointNodesC.remove(node);
    }

    @Override
    public synchronized void onNodeVisitMJJ(fr.m1comp5.mjj.generated.Node node) throws InterruptedException {
        if (breakpointNodesM.contains(node)) {
            paused = true;
            if (eventHandler != null) {
                eventHandler.handle(false, node);
            }
            while (paused) {
                wait();
            }
        }
    }

    @Override
    public synchronized void onNodeVisitJJC(fr.m1comp5.jjc.generated.Node node) throws InterruptedException {
        if (breakpointNodesC.contains(node)) {
            paused = true;
            if (eventHandler != null) {
                eventHandler.handle(false, node);
            }
            while (paused) {
                wait();
            }
        }
    }
}