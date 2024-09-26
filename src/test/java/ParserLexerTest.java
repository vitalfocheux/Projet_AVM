import org.junit.Test;
import static org.junit.Assert.assertEquals;

import fr.m1comp5.LexerParserGenerator.MiniJajaParser.MiniJaja;
import fr.m1comp5.LexerParserGenerator.MiniJajaParser.ParseException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParserLexerTest
{

    @ParameterizedTest
    @MethodSource("fileProvider")
    void testParser(String filepath) throws ParseException, FileNotFoundException {
        try(InputStream is = new FileInputStream(filepath)){
            Reader reader = new InputStreamReader(is);
            MiniJaja mjjparser = new MiniJaja(reader);
            mjjparser.classe();
        }catch (FileNotFoundException e){
            System.out.println("File: "+filepath+" not found");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    static Stream<org.junit.jupiter.params.provider.Arguments> fileProvider() throws IOException {
        Stream.Builder<org.junit.jupiter.params.provider.Arguments> builder = Stream.builder();
        List<String> filepaths = getFilePaths("src/main/resources/data/operation/add");
        for(String filepath : filepaths){
            builder.add(Arguments.of(filepath));
        }
        List<String> filepaths2 = getFilePaths("src/main/resources/data/operation/sub");
        for(String filepath : filepaths2){
            builder.add(Arguments.of(filepath));
        }
        return builder.build();
    }

    public static List<String> getFilePaths(String dir) throws IOException{
        try (Stream<Path> paths = Files.walk(Paths.get(dir))) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return List.of();
    }

//    @Test
//    public void t(){
//        try {
//            List<String> filePaths = getFilePaths("src/main/resources/data/operation/add");
//            filePaths.forEach(System.out::println);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
