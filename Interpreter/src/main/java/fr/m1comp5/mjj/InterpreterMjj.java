package fr.m1comp5.mjj;

import fr.m1comp5.mjj.generated.Node;

public class InterpreterMjj {
    private Node root;
    private VisitorMjj VisitorMjj;

    public InterpreterMjj(Node root) {
        this.root = root;
        VisitorMjj = new VisitorMjj();
    }

    public String interpret() {
        System.out.println("Starting interpretation...");
        root.jjtAccept(VisitorMjj, null);
        return VisitorMjj.toString();
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
