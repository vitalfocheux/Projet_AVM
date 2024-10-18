import fr.m1comp5.Analyzer.MiniJaja; // Your parser
import fr.m1comp5.Interpreter.mjj.InterpreterMjj;
import org.junit.jupiter.api.Test;

import java.io.FileReader;

public class InterpreterMjjTest {
    @Test
    void testInterpreterMiniJaja() {
        try {
            // Assuming you already have a parser that creates the AST
            MiniJaja parser = new MiniJaja(new FileReader("src/main/resources/data/operation/add/simple_add_1.mjj"));
            Node rootNode = parser.start(); // Parse and get the AST root

            // Initialize the interpreter with the AST root
            InterpreterMjj interpreter = new InterpreterMjj(rootNode);

            // Start interpreting the AST
            interpreter.interpret();

        } catch (Exception e) {
            System.err.println("Error during interpretation: " + e.getMessage());
        }
    }
}
