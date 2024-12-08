package fr.m1comp5;

import fr.m1comp5.jjc.generated.JajaCode;
import fr.m1comp5.jjc.generated.Node;

import fr.m1comp5.mjj.generated.MiniJaja;
import fr.m1comp5.mjj.generated.SimpleNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CompilerUnitTest {
    @ParameterizedTest
    @MethodSource("fileProvider")
    void testCompiler(String filepath) {
        Assertions.assertDoesNotThrow(() -> {
            MiniJaja mjj = new MiniJaja(new FileReader(filepath));
            SimpleNode root = mjj.start();
            Compiler compiler = new Compiler(root);
            compiler.compile();
            String javaCode = compiler.jjcToString();
            System.out.println(javaCode);
        });
    }

    static Stream<Arguments> fileProvider() throws IOException {
        return fileProvider("src/main/resources/data/mjj/success");
    }

    private static Stream<Arguments> fileProvider(String path) throws IOException {
        Stream.Builder<Arguments> builder = Stream.builder();
        List<String> filepaths = getAllFilePaths(path);
        for(String filepath : filepaths){
            builder.add(Arguments.of(filepath));
        }
        return builder.build();
    }

    private static List<String> getAllFilePaths(String directory) throws IOException {
        List<String> filepaths = new ArrayList<>();
        try(Stream<Path> paths = Files.walk(Paths.get(directory))){
            paths.forEach(path -> {
                if(Files.isRegularFile(path)){
                    filepaths.add(path.toString());
                }
            });
        }
        return filepaths;
    }
}
