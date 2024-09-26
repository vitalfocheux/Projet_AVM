import fr.m1comp5.LexerParserGenerator.MiniJajaParser.MiniJaja;
import fr.m1comp5.LexerParserGenerator.MiniJajaParser.ParseException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParserLexerTest
{

    @ParameterizedTest
    @MethodSource("fileProvider")
    void testParser(String filepath) throws ParseException {
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
        List<String> filepathsAnd = getFilePaths("src/main/resources/data/boolean/and");
        for(String filepath : filepathsAnd){
            builder.add(Arguments.of(filepath));
        }
        List<String> filepathsEqual = getFilePaths("src/main/resources/data/boolean/equal");
        for(String filepath : filepathsEqual){
            builder.add(Arguments.of(filepath));
        }
        List<String> filepathsNot = getFilePaths("src/main/resources/data/boolean/not");
        for(String filepath : filepathsNot){
            builder.add(Arguments.of(filepath));
        }
        List<String> filepathsOr = getFilePaths("src/main/resources/data/boolean/or");
        for(String filepath : filepathsOr){
            builder.add(Arguments.of(filepath));
        }
        List<String> filepathsSuperior = getFilePaths("src/main/resources/data/boolean/superior");
        for(String filepath : filepathsSuperior){
            builder.add(Arguments.of(filepath));
        }
        List<String> filepathsAdd = getFilePaths("src/main/resources/data/operation/add");
        for(String filepath : filepathsAdd){
            builder.add(Arguments.of(filepath));
        }
        List<String> filepathsSub = getFilePaths("src/main/resources/data/operation/sub");
        for(String filepath : filepathsSub){
            builder.add(Arguments.of(filepath));
        }
        List<String> filepathsMul = getFilePaths("src/main/resources/data/operation/multiply");
        for(String filepath : filepathsMul){
            builder.add(Arguments.of(filepath));
        }
        List<String> filepathsDiv = getFilePaths("src/main/resources/data/operation/division");
        for(String filepath : filepathsDiv){
            builder.add(Arguments.of(filepath));
        }
        List<String> filepathsIncrement = getFilePaths("src/main/resources/data/operation/increment");
        for(String filepath : filepathsIncrement){
            builder.add(Arguments.of(filepath));
        }
        return builder.build();
    }

    public static List<String> getFilePaths(String dir){
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

}
