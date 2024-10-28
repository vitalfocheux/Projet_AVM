package fr.m1comp5.Compiler;

import fr.m1comp5.Analyzer.jjc.generated.Node;

import java.util.ArrayList;
import java.util.List;

public class CompileToJjcFile {
    List<Node> instrs;

    public CompileToJjcFile(List<Node> instrs) {
        this.instrs = instrs;
    }

    public String jjcToString() {
        return null;
    }

    public void writeJjcFile() {
    }
}
