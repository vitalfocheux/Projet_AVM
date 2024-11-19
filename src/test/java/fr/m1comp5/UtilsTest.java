package fr.m1comp5;

import org.junit.jupiter.params.provider.Arguments;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class UtilsTest {

    static Stream<org.junit.jupiter.params.provider.Arguments> fileProvider(String path) throws IOException {
        Stream.Builder<org.junit.jupiter.params.provider.Arguments> builder = Stream.builder();
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
