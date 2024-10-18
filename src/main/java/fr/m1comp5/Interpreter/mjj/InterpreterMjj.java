package fr.m1comp5.Interpreter.mjj;
import fr.m1comp5.Analyzer.generated.Node;

public class InterpreterMjj {
    private Node root;
    private VisitorMjj VisitorMjj;

    public InterpreterMjj(Node root) {
        this.root = root;
        VisitorMjj = new VisitorMjj();
    }

    public void interpret() {
        if (root != null) {
            System.out.println("Starting interpretation...");
            root.jjtAccept(VisitorMjj, null);
        } else {
            System.err.println("Error! Root node is null, cannot interpret.");
        }
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public Node getRoot() {
        return root;
    }
}
