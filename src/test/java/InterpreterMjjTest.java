import fr.m1comp5.Analyzer.mjj.bin.MiniJaja;
import fr.m1comp5.Analyzer.mjj.bin.Node;
import fr.m1comp5.Interpreter.mjj.InterpreterMjj;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Stream;

public class InterpreterMjjTest {
    @ParameterizedTest
    @MethodSource("fileProvider")
    void testInterpreterMiniJaja(String filepath) throws IOException {
        boolean exceptionCaught = false;
        try {
            // Assuming you already have a parser that creates the AST
            System.out.println(filepath);
            MiniJaja parser = new MiniJaja(new FileReader(filepath));
            Node rootNode = parser.start(); // Parse and get the AST root

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
        return UtilsTest.fileProvider("src/main/resources/data/success");
    }
}
