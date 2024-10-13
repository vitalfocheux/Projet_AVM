import fr.m1comp5.LexersParsers.MiniJaja;
import fr.m1comp5.LexersParsers.ParseException;

import fr.m1comp5.LexersParsers.SimpleNode;

import java.io.IOException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ParserLexerTest
{

    @ParameterizedTest
    @MethodSource("fileProvider")
    void testParser(String filepath) throws ParseException {
        try(InputStream is = new FileInputStream(filepath)){
            Reader reader = new InputStreamReader(is);
            MiniJaja mjjparser = new MiniJaja(reader);
            try
            {
                System.out.println(filepath);
                SimpleNode n = mjjparser.start();
                n.dump("");
            }
            catch (ParseException e)
            {
                System.out.println(e.getMessage());
            }
        }catch (FileNotFoundException e){
            System.out.println("File: "+filepath+" not found");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    static Stream<org.junit.jupiter.params.provider.Arguments> fileProvider() throws IOException {
        Stream.Builder<org.junit.jupiter.params.provider.Arguments> builder = Stream.builder();
        List<String> filepaths = getAllFilePaths("src/main/resources/data");
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
