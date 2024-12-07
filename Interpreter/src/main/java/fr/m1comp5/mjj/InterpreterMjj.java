package fr.m1comp5.mjj;

import fr.m1comp5.Debug.InterpreterDebugger;
import fr.m1comp5.mjj.generated.Node;

public class InterpreterMjj {
    private Node root;
    private VisitorMjj VisitorMjj;
    private InterpreterDebugger debugger;

    public InterpreterMjj(Node root) {
        this.root = root;
        VisitorMjj = new VisitorMjj();
    }

    public String interpret() {
        System.out.println("Activating debugger...");
        VisitorMjj.ActiverDebugger(true);
        System.out.println("Starting interpretation...");
        root.jjtAccept(VisitorMjj, null);
        if (debugger != null) {
            debugger.triggerEventHandler(true, root);}
        return VisitorMjj.toString();
    }

    public void setDebugger(InterpreterDebugger debugger ) {
        this.debugger = debugger;
        VisitorMjj.setDebugger(debugger);
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public Node getRoot() {
        return root;
    }

    public String getStdout()
    {
        return VisitorMjj.toString();
    }
}
