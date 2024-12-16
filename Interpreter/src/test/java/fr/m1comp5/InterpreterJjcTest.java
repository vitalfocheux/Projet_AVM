package fr.m1comp5;

import fr.m1comp5.jjc.InterpreterJjc;
import fr.m1comp5.jjc.generated.JajaCode;
import fr.m1comp5.jjc.generated.Node;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class InterpreterJjcTest {
    @ParameterizedTest
    @MethodSource("fileProvider")
    public void testInterpreterJajaCode(String filepath) {
        Assertions.assertDoesNotThrow(() -> {
            JajaCode jjc = new JajaCode(new FileReader(filepath));
            Node root = jjc.start();
            Memory mem = new Memory();
            List<Node> instrs = new ArrayList<>();
            for(int i = 0; i < root.jjtGetNumChildren(); i++) {
                System.out.println(root.jjtGetChild(i));
                instrs.add(root.jjtGetChild(i));
            }
            InterpreterJjc interpreter = new InterpreterJjc(root, mem, instrs);
            System.out.println(interpreter.interpret());
        });
    }

    static Stream<Arguments> fileProvider() throws IOException {
        return UtilsTest.fileProvider("src/main/resources/data/jjc/success");
    }
}
