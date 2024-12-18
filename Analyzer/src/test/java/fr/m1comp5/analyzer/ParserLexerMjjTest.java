package fr.m1comp5.analyzer;

import java.io.IOException;

import fr.m1comp5.mjj.generated.MiniJaja;
import fr.m1comp5.mjj.generated.ParseException;
import fr.m1comp5.mjj.generated.SimpleNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ParserLexerMjjTest
{

    @ParameterizedTest
    @MethodSource("fileProvider")
    void testParser(String filepath) throws ParseException {
        Assertions.assertDoesNotThrow(() -> {
            try(InputStream is = new FileInputStream(filepath)){
                Reader reader = new InputStreamReader(is);
                MiniJaja mjjparser = new MiniJaja(reader);
                SimpleNode n = mjjparser.start();
            }catch (FileNotFoundException e){
                System.out.println("File: "+filepath+" not found");
            }catch (IOException e){
                e.printStackTrace();
            }
        });
    }

    @ParameterizedTest
    @MethodSource("fileProviderKO")
    void testParserKO(String filepath) {
        Assertions.assertThrows(ParseException.class, () -> {
            try (InputStream is = new FileInputStream(filepath)) {
                Reader reader = new InputStreamReader(is);
                MiniJaja mjjparser = new MiniJaja(reader);
                mjjparser.start();
            } catch (FileNotFoundException e) {
                System.out.println("File: " + filepath + " not found");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    static Stream<org.junit.jupiter.params.provider.Arguments> fileProviderKO() throws IOException {
        return UtilsTest.fileProvider("src/main/resources/data/mjj/error/parser_lexer");
    }

    static Stream<org.junit.jupiter.params.provider.Arguments> fileProvider() throws IOException {
        return UtilsTest.fileProvider("src/main/resources/data/mjj/success");
    }


}