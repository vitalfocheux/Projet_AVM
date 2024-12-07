package fr.m1comp5.jjc;

import fr.m1comp5.jjc.generated.ASTJajaCode;
import fr.m1comp5.jjc.generated.Node;
import fr.m1comp5.mjj.VisitorMjj;
import fr.m1comp5.Memory;
import fr.m1comp5.Debug.InterpreterDebugger;

import java.util.ArrayList;
import java.util.List;

public class InterpreterJcc {
    private Node root;
    private VisitorJcc VisitorJcc;
    private List<Node> instrs;
    private InterpreterDebugger debugger;


    public InterpreterJcc(Node root, Memory mem, List<Node> instrs) {
        this.root = root;
        VisitorJcc = new VisitorJcc(mem);
        this.instrs = instrs;
    }

    public String interpret() {
        System.out.println("Activating debugger...");
        VisitorJcc.ActiverDebugger(true);
        int addr = 1;
        while (addr < instrs.size()) {
            instrs.get(addr-1).jjtAccept(VisitorJcc, null);
            addr++;
        }
        instrs.get(instrs.size()-1).jjtAccept(VisitorJcc, null);
        if (debugger != null) {
            debugger.triggerEventHandler(true, root);}
        
        return VisitorJcc.toString();
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
        VisitorJcc.setDebugger(debugger);
    }


    public void setRoot(Node root) {
        this.root = root;
    }

    public Node getRoot() {
        return root;
    }
}
