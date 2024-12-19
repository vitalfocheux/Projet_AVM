package fr.m1comp5;

import fr.m1comp5.Compiler;
import fr.m1comp5.jjc.InterpreterJjc;
import fr.m1comp5.mjj.InterpreterMjj;
import fr.m1comp5.mjj.generated.MiniJaja;
import fr.m1comp5.mjj.generated.SimpleNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class IntegrationCompMjj {
    @ParameterizedTest
    @MethodSource("fileProvider")
    void testInterpreterMiniJaja(String filepath) {
        Assertions.assertDoesNotThrow(() -> {
            MiniJaja parser = new MiniJaja(new FileReader(filepath));
            SimpleNode rootNode = parser.start();
            Compiler compiler = new Compiler(rootNode);
            try
            {
                InterpreterJjc interpreter = new InterpreterJjc(null, new Memory(), compiler.compile());
                System.out.println(interpreter.interpret());
            }
            catch (Exception e)
            {
                StackTraceElement[] traces = e.getStackTrace();
                for (StackTraceElement ste : traces)
                {
                    System.err.println(ste);
                }
                throw new Exception("TEST");
            }

        });
    }

    static Stream<Arguments> fileProvider() throws IOException {
        Stream<Arguments> res = UtilsTest.fileProvider("src/main/resources/data/mjj/success");

        res = res.filter(arg -> {
            String filepath = (String) arg.get()[0];
            filepath = Paths.get(filepath).getFileName().toString();
            return !(filepath.contains("quick_sort.mjj") | filepath.contains("sum_fct.mjj"));
        });

        return res;
    }
}
