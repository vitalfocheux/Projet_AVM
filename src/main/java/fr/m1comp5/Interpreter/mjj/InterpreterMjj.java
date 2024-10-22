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
        try {
            System.out.println("Starting interpretation...");
            root.jjtAccept(VisitorMjj, null);
            return VisitorMjj.toString();
        } catch (Exception e) {
            System.err.println("Error in interpretation visitor: " + e.getMessage());
        }
        return null;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public Node getRoot() {
        return root;
    }
}
