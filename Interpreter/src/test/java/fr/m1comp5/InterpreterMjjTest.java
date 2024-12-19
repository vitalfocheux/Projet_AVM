package fr.m1comp5;

import fr.m1comp5.mjj.generated.*;
import fr.m1comp5.mjj.InterpreterMjj;

import junit.framework.Assert;
import org.junit.jupiter.api.Assertions;
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
        Assertions.assertDoesNotThrow(() -> {
            MiniJaja parser = new MiniJaja(new FileReader(filepath));
            SimpleNode rootNode = parser.start();
            rootNode.dump("");
            InterpreterMjj interpreter = new InterpreterMjj(rootNode);
            System.out.println(interpreter.interpret());
        });
    }

    static Stream<Arguments> fileProvider() throws IOException {
        return UtilsTest.fileProvider("src/main/resources/data/mjj/success");
    }
}
