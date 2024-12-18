package fr.m1comp5.jjc;

import fr.m1comp5.custom.exception.VisitorException;
import fr.m1comp5.jjc.generated.ASTJajaCode;
import fr.m1comp5.jjc.generated.Node;
import fr.m1comp5.Memory;
import fr.m1comp5.Debug.InterpreterDebugger;

import java.util.ArrayList;
import java.util.List;

public class InterpreterJjc {
    private Node root;
    private VisitorJjc visitorJcc;
    private List<Node> instrs;
    private InterpreterDebugger debugger;


    public InterpreterJjc(Node root, Memory mem, List<Node> instrs) {
        this.root = root;
        visitorJcc = new VisitorJjc(mem);
        this.instrs = instrs;
    }

    public String interpret() throws VisitorException
    {
        System.out.println("Activating debugger...");
        visitorJcc.ActiverDebugger(true);
        int addr = 1;
        while (addr < instrs.size()) {
            System.out.println(addr);
            instrs.get(addr - 1).jjtAccept(visitorJcc, null);
            addr = visitorJcc.getAddr();
        }
        instrs.get(instrs.size()-1).jjtAccept(visitorJcc, null);

        if (debugger != null) {
            debugger.triggerEventHandler(true, root);}

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

    public void setDebugger(InterpreterDebugger debugger ) {
        this.debugger = debugger;
        visitorJcc.setDebugger(debugger);
    }


    public void setRoot(Node root) {
        this.root = root;
    }

    public Node getRoot() {
        return root;
    }
}
