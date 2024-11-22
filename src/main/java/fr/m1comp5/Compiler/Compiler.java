package fr.m1comp5.Compiler;

import fr.m1comp5.Analyzer.mjj.generated.Node;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class Compiler {
    private Node root;
    private CompilerVisitor visitor;
    private List<fr.m1comp5.Analyzer.jjc.generated.Node> instrs;

    public Compiler(Node root) {
        this.root = root;
        this.visitor = new CompilerVisitor();
    }

    public void compile() {
        try {
            root.jjtAccept(visitor, new DataModel(1,Mode.DEFAULT));
            instrs = visitor.getInstrs();
            System.out.println("Compiling to jajacode...");
            writeJjcFile();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public String jjcToString() {
        StringBuilder sb = new StringBuilder();
        for(fr.m1comp5.Analyzer.jjc.generated.Node instr : instrs) {
            sb.append(instr.toString());
            sb.append("\n");
        }
        return null;
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
