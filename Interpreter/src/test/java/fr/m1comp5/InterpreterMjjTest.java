package fr.m1comp5;

import fr.m1comp5.Analyzer.mjj.generated.MiniJaja;
import fr.m1comp5.Analyzer.mjj.generated.Node;
import fr.m1comp5.Analyzer.mjj.generated.SimpleNode;
import fr.m1comp5.Interpreter.mjj.InterpreterMjj;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class InterpreterMjjTest {
    @ParameterizedTest
    @MethodSource("fileProvider")
    void testInterpreterMiniJaja(String filepath) {
        boolean exceptionCaught = false;
        try {
            // Assuming you already have a parser that creates the AST
            //System.out.println(filepath);
            MiniJaja parser = new MiniJaja(new FileReader(filepath));

            SimpleNode rootNode = parser.start();// Parse and get the AST root

            rootNode.dump("");

            // Initialize the interpreter with the AST root
            InterpreterMjj interpreter = new InterpreterMjj(rootNode);

            // Start interpreting the AST
            interpreter.interpret();

        } catch (Exception e) {
            exceptionCaught = true;
            System.err.println("Error during interpretation: " + e.getMessage());
        }
        Assertions.assertFalse(exceptionCaught);
    }

    static Stream<Arguments> fileProvider() throws IOException {
        Stream<Arguments> res = UtilsTest.fileProvider("src/main/resources/data/mjj/success");

//        res = res.filter(arg -> {
//            String filepath = (String) arg.get()[0];
//            filepath = Paths.get(filepath).getFileName().toString();
//            return !(filepath.contains("1.mjj") || filepath.contains("tas.mjj") || filepath.contains("synomynie.mjj") || filepath.contains("fact.mjj"));
//        });

        return res;
    }
}
