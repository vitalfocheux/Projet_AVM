package fr.m1comp5.jjc;

import fr.m1comp5.custom.exception.VisitorException;
import fr.m1comp5.jjc.generated.*;
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
        visitorJcc.ActivesDebugger(true);
        int addr = 1;
        int count =0;
        StringBuilder sb = new StringBuilder();
        for(fr.m1comp5.jjc.generated.Node instr : instrs) {
            if (instr == null) {
                continue;
            }
            sb.append(count + " ");
            sb.append(instr.toString().toLowerCase());
            if (instr.jjtGetNumChildren() > 0) {
                sb.append("(");
                if (instr.jjtGetNumChildren() == 4) {
                    sb.append(((ASTJcIdent) instr.jjtGetChild(0)).jjtGetValue().toString().toLowerCase()).append(",");
                    sb.append(((ASTType) instr.jjtGetChild(1)).jjtGetValue().toString().toLowerCase()).append(",");
                    sb.append(((ASTSorte) instr.jjtGetChild(2)).jjtGetValue().toString().toLowerCase()).append(",");
                    sb.append(((ASTJcNbre) instr.jjtGetChild(3)).jjtGetValue().toString().toLowerCase());
                } else {
                    if (instr.jjtGetChild(0) instanceof ASTJcChaine) {
                        sb.append(((ASTJcChaine) instr.jjtGetChild(0)).jjtGetValue().toString().toLowerCase());
                    } else if (instr.jjtGetChild(0) instanceof ASTJcNbre) {
                        sb.append(((ASTJcNbre) instr.jjtGetChild(0)).jjtGetValue().toString().toLowerCase());
                    } else if (instr.jjtGetChild(0) instanceof ASTJcIdent) {
                        sb.append(((ASTJcIdent) instr.jjtGetChild(0)).jjtGetValue().toString().toLowerCase());
                    } else if (instr.jjtGetChild(0) instanceof ASTJcVrai) {
                        sb.append(((ASTJcVrai) instr.jjtGetChild(0)).jjtGetValue().toString().toLowerCase());
                    } else if (instr.jjtGetChild(0) instanceof ASTJcFalse) {
                        sb.append(((ASTJcFalse) instr.jjtGetChild(0)).jjtGetValue().toString().toLowerCase());
                    }
                }
                sb.append(")");
            }
            sb.append(";");
            sb.append("\n");
            count++;
        }
        System.out.println(sb.toString());
        while (addr < instrs.size()) {
            System.out.println(addr);
            instrs.get(addr-1).jjtAccept(visitorJcc, null);
            addr = visitorJcc.getAddr();
        }
        //instrs.get(instrs.size()-1).jjtAccept(visitorJcc, null);

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
