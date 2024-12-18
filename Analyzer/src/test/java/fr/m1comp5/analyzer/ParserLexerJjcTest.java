package fr.m1comp5.analyzer;

import java.io.IOException;

import fr.m1comp5.jjc.generated.JajaCode;
import fr.m1comp5.jjc.generated.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.*;
import java.util.stream.Stream;

public class ParserLexerJjcTest
{

    @ParameterizedTest
    @MethodSource("fileProvider")
    void testParser(String filepath) throws ParseException {
        Assertions.assertDoesNotThrow(() -> {
            try(InputStream is = new FileInputStream(filepath)){
                Reader reader = new InputStreamReader(is);
                JajaCode jjcparser = new JajaCode(reader);
                jjcparser.start();
            }catch (FileNotFoundException e){
                System.out.println("File: "+filepath+" not found");
            }catch (IOException e){
                e.printStackTrace();
            }
        });
    }
/*
    @ParameterizedTest
    @MethodSource("fileProviderKO")
    void testParserKO(String filepath) throws ParseException {
        Assertions.assertThrows(ParseException.class, () -> {
            JajaCode jjcparser = new JajaCode(new InputStreamReader(new FileInputStream(filepath)));
            jjcparser.start();
        });
    }

    static Stream<org.junit.jupiter.params.provider.Arguments> fileProviderKO() throws IOException {
        return UtilsTest.fileProvider("src/main/resources/data/jjc/error");
    }*/

    static Stream<org.junit.jupiter.params.provider.Arguments> fileProvider() throws IOException {
        return UtilsTest.fileProvider("src/main/resources/data/jjc/success");
    }


}