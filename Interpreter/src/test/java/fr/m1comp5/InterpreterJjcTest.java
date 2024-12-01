package fr.m1comp5;

import fr.m1comp5.jjc.InterpreterJcc;
import fr.m1comp5.jjc.generated.JajaCode;
import fr.m1comp5.jjc.generated.Node;

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
        Assertions.assertDoesNotThrow(() -> {
            JajaCode jjc = new JajaCode(new FileReader(filepath));
            Node root = jjc.start();
            Memory mem = new Memory(new SymbolTable());
            InterpreterJcc interpreter = new InterpreterJcc(root, mem);
            System.out.println(interpreter.interpret());
        });
    }

    static Stream<Arguments> fileProvider() throws IOException {
        return UtilsTest.fileProvider("src/main/resources/data/jjc/success");
    }
}
