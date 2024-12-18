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

    public List<fr.m1comp5.jjc.generated.Node> compile() {
        try {
            root.jjtAccept(visitor, new DataModel(addr,Mode.DEFAULT));
            instrs = visitor.getInstrs();
            return instrs;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
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
        return sb.toString();
    }

    public void writeJjcFile(String filename) {
        //src/main/resources/data/mjj/success
        //target/bin/
        File file = new File("./bin/"+filename+".jjc");
        file.getParentFile().mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("./bin/"+filename+".jjc"))) {
            bw.write(jjcToString());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
