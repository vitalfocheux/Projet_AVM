package fr.m1comp5.Interpreter.mjj;

import fr.m1comp5.Analyzer.mjj.Node;

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
        System.out.println("Value of interpretation is : " + VisitorMjj.toString());
        return VisitorMjj.toString();
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public Node getRoot() {
        return root;
    }
}
