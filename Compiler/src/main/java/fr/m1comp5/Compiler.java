package fr.m1comp5;

import fr.m1comp5.jjc.generated.*;
import fr.m1comp5.mjj.generated.ASTIdent;
import fr.m1comp5.mjj.generated.Node;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Compiler {
    private Node root;
    private CompilerVisitor visitor;
    private List<fr.m1comp5.jjc.generated.Node> instrs;
    private int addr;

    public Compiler(Node root) {
        this.root = root;
        this.visitor = new CompilerVisitor();
        instrs = new ArrayList<>();
        addr = 1;
    }

    public void compile() {
        try {
            root.jjtAccept(visitor, new DataModel(addr,Mode.DEFAULT));
            instrs = visitor.getInstrs();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public String jjcToString() {
        int count = 1;
        StringBuilder sb = new StringBuilder();
        for(fr.m1comp5.jjc.generated.Node instr : instrs) {
            if (instr == null) {
                continue;
            }
            sb.append(count + " ");
            sb.append(instr.toString());
            if (instr.jjtGetNumChildren() > 0) {
                sb.append("(");
                if (instr.jjtGetChild(0) instanceof ASTJcChaine) {
                    sb.append(((ASTJcChaine) instr.jjtGetChild(0)).jjtGetValue());
                } else if (instr.jjtGetChild(0) instanceof ASTJcNbre) {
                    sb.append(((ASTJcNbre) instr.jjtGetChild(0)).jjtGetValue());
                } else if (instr.jjtGetChild(0) instanceof ASTJcIdent) {
                    sb.append(((ASTJcIdent) instr.jjtGetChild(0)).jjtGetValue());
                } else if (instr.jjtGetChild(0) instanceof ASTJcVrai) {
                    sb.append(((ASTJcVrai) instr.jjtGetChild(0)).jjtGetValue());
                } else if (instr.jjtGetChild(0) instanceof ASTJcFalse) {
                    sb.append(((ASTJcFalse) instr.jjtGetChild(0)).jjtGetValue());
                }
                sb.append(")");
            }
            sb.append("\n");
            count++;
        }
        return sb.toString();
    }

    public void writeJjcFile(String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("./bin/"+filename+".jjc"))) {
            bw.write(jjcToString());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
