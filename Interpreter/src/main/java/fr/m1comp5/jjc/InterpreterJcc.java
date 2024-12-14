package fr.m1comp5.jjc;

import fr.m1comp5.custom.exception.VisitorException;
import fr.m1comp5.jjc.generated.ASTJajaCode;
import fr.m1comp5.jjc.generated.ASTJcnil;
import fr.m1comp5.jjc.generated.Node;
import fr.m1comp5.Memory;

import java.util.ArrayList;
import java.util.List;

public class InterpreterJcc {
    private Node root;
    private VisitorJcc visitorJcc;
    private List<Node> instrs;

    public InterpreterJcc(Node root, Memory mem, List<Node> instrs) {
        this.root = root;
        visitorJcc = new VisitorJcc(mem);
        this.instrs = instrs;
    }

    public String interpret() throws VisitorException
    {
        int addr = 1;
        while (addr < instrs.size()) {
            instrs.get(addr - 1).jjtAccept(visitorJcc, null);
            addr = visitorJcc.getAddr();
        }
        instrs.get(instrs.size()-1).jjtAccept(visitorJcc, null);
        return visitorJcc.toString();
    }

    public static List<Node> getInstrsFromRoot(Node r)
    {
        List<Node> instructions = new ArrayList<>();
        Node currentJjc = r.jjtGetChild(0);
        while (currentJjc instanceof ASTJajaCode)
        {
            instructions.add(currentJjc.jjtGetChild(1));
            currentJjc = currentJjc.jjtGetChild(2);
        }
        return instructions;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public Node getRoot() {
        return root;
    }
}
