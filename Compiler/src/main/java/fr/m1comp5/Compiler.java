package fr.m1comp5;

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


        public Compiler(Node root) {
            this.root = root;
            this.visitor = new CompilerVisitor();
            this.instrs = new ArrayList<>();  // Initialiser la liste
        }

        public void compile() {
            try {
                if (root == null) {
                    throw new IllegalStateException("AST root is null");
                }
                if (visitor == null) {
                    visitor = new CompilerVisitor();
                }

                root.jjtAccept(visitor, new DataModel(1, Mode.DEFAULT));
                instrs = visitor.getInstrs();

                if (instrs == null || instrs.isEmpty()) {
                    System.out.println("Warning: No instructions generated");
                    instrs = new ArrayList<>();  // Ensure it's not null
                } else {
                    System.out.println("Compiling to jajacode...");
                    writeJjcFile();
                }
            } catch (Exception e) {
                System.err.println("Compilation error: " + e.getMessage());
                e.printStackTrace();
                instrs = new ArrayList<>();  // Ensure it's not null even after error
            }
        }

        public String jjcToString() {
            StringBuilder sb = new StringBuilder();
            if (instrs == null || instrs.isEmpty()) {
                return "// No instructions generated";
            }
            for (fr.m1comp5.jjc.generated.Node instr : instrs) {
                if (instr != null) {
                    sb.append(instr.toString());
                    sb.append("\n");
                }
            }
            return sb.toString();
        }

    public void writeJjcFile() {
        File dir = new File("CompilerCode");
        int filecount = 0;
        if (dir.isDirectory()) {
            filecount = dir.listFiles().length;
        }
        filecount++;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("./CompiledCode/temp"+filecount+".jjc"))) {
            bw.write(jjcToString());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
