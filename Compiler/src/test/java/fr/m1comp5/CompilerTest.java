package fr.m1comp5;

import fr.m1comp5.mjj.generated.MiniJaja;
import fr.m1comp5.mjj.generated.ParseException;
import fr.m1comp5.mjj.generated.SimpleNode;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class CompilerTest {

    @Test
    public void testCompiler() throws FileNotFoundException, ParseException {
        MiniJaja parser = new MiniJaja(new FileReader("./main/resources/data/mjj/success/simpleWrite.mjj"));
        SimpleNode rootNode = parser.start();
        rootNode.dump("");

        Compiler compiler = new Compiler(rootNode);
        compiler.compile();
        System.out.println(compiler.jjcToString());
    }

    @Test
    public void testCompilerOPadd() throws FileNotFoundException, ParseException {
        MiniJaja parser = new MiniJaja(new FileReader("./main/resources/data/mjj/success/operation/add/simple_add_1.mjj"));
        SimpleNode rootNode = parser.start();
        rootNode.dump("");

        Compiler compiler = new Compiler(rootNode);
        compiler.compile();
        System.out.println(compiler.jjcToString());
    }

}
