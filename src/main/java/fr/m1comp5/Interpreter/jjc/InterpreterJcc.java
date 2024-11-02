package fr.m1comp5.Interpreter.jjc;

import fr.m1comp5.Analyzer.jjc.generated.Node;
import fr.m1comp5.Interpreter.mjj.VisitorMjj;
import fr.m1comp5.Memory.Memory;

import java.util.List;

public class InterpreterJcc {
    private Node root;
    private VisitorJcc VisitorJcc;
    private List<Node> instrs;

    public InterpreterJcc(Node root, Memory mem) {
        this.root = root;
        VisitorJcc = new VisitorJcc(mem);
    }

    public String interpret() {
        int addr = 1;
        while (addr < instrs.size()) {
            instrs.get(addr-1).jjtAccept(VisitorJcc, null);
            addr++;
        }
        instrs.get(instrs.size()-1).jjtAccept(VisitorJcc, null);
        return VisitorJcc.toString();
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public Node getRoot() {
        return root;
    }
}
