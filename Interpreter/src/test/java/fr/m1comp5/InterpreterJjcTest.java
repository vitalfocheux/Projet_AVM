package fr.m1comp5;

import fr.m1comp5.jjc.InterpreterJjc;
import fr.m1comp5.jjc.generated.JajaCode;
import fr.m1comp5.jjc.generated.Node;

import fr.m1comp5.mjj.generated.MiniJaja;
import fr.m1comp5.mjj.generated.SimpleNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.script.Compilable;
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
            MiniJaja parser = new MiniJaja(new FileReader(filepath));
            SimpleNode rootNode = parser.start();
            Compiler mjjComp = new Compiler(rootNode);
            List<Node> instrs = mjjComp.compile();
            Memory mem = new Memory();
            InterpreterJjc interpreter = new InterpreterJjc(instrs.get(0).jjtGetParent(), mem, instrs);
            System.out.println(interpreter.interpret());
        });
    }

    static Stream<Arguments> fileProvider() throws IOException {
        return UtilsTest.fileProvider("src/main/resources/data/mjj/success");
    }
}
