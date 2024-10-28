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
            root.jjtAccept(visitor, null);
            instrs = visitor.getInstrs();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public String jjcToString() {
        return null;
    }

    public void writeJjcFile() {
        File dir = new File("CompilerCode");
        int filecount = 0;
        if (dir.isDirectory()) {
            filecount = dir.listFiles().length;
        }
        filecount++;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("./temp"+filecount+".jjc"))) {
            bw.write(jjcToString());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
