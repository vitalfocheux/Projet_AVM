package fr.m1comp5;

import fr.m1comp5.jjc.generated.JajaCode;
import fr.m1comp5.jjc.generated.*;
import fr.m1comp5.jjc.InterpreterJjc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Stream;


public class InterpreterJjcTest {
    @ParameterizedTest
    @MethodSource("fileProvider")
    void testInterpreterMiniJaja(String filepath) {
        boolean exceptionCaught = false;
        try {
            // Assuming you already have a parser that creates the AST
            //System.out.println(filepath);
            JajaCode parser = new JajaCode(new FileReader(filepath));

            SimpleNode rootNode = parser.start();// Parse and get the AST root

            rootNode.dump("");

            InterpreterJjc interpreter = new InterpreterJjc(rootNode, new Memory(), InterpreterJjc.getInstrsFromRoot(rootNode));
            interpreter.interpret();
            // Initialize the interpreter with the AST root

        } catch (Exception e) {
            exceptionCaught = true;
            System.err.println("Error during interpretation: " + e.getMessage());
        }
        Assertions.assertFalse(exceptionCaught);
    }

    static Stream<Arguments> fileProvider() throws IOException {
        Stream<Arguments> res = UtilsTest.fileProvider("src/main/resources/data/jjc/success");

//        res = res.filter(arg -> {
//            String filepath = (String) arg.get()[0];
//            filepath = Paths.get(filepath).getFileName().toString();
//            return !(filepath.contains("1.mjj") || filepath.contains("tas.mjj") || filepath.contains("synomynie.mjj") || filepath.contains("fact.mjj"));
//        });

        return res;
    }
}