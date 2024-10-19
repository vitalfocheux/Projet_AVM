import fr.m1comp5.Analyzer.mjj.MiniJaja;
import fr.m1comp5.Analyzer.mjj.ParseException;

import fr.m1comp5.Analyzer.mjj.SimpleNode;
import java.io.IOException;
import static org.junit.Assert.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ParserLexerMjjTest
{
    //TODO: Comparer les fichiers de sortie avec les fichiers de référence grâce à la commande diff
    private void testASTParser(String path, SimpleNode root){
        List<String> paths = Arrays.stream(path.split("\\\\")).toList();
        boolean exceptionCaught = false;

        File outputFile = new File("target/resources/ast/"+paths.get(paths.size()-1));
        outputFile.getParentFile().mkdirs();
        try (PrintStream out = new PrintStream(new FileOutputStream(outputFile))) {
            System.setOut(out);
            root.dump("");
        } catch (FileNotFoundException e) {
            exceptionCaught = true;
            e.printStackTrace();
        } finally {
            System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        }
        Assertions.assertFalse(exceptionCaught);
    }

    @ParameterizedTest
    @MethodSource("fileProvider")
    void testParser(String filepath) throws ParseException {
        try(InputStream is = new FileInputStream(filepath)){
            Reader reader = new InputStreamReader(is);
            MiniJaja mjjparser = new MiniJaja(reader);

            boolean exceptionCaught = false;
            try
            {
                //System.out.println(filepath);
                SimpleNode n = mjjparser.start();
                testASTParser(filepath, n);
                //n.dump("");
            }
            catch (ParseException e)
            {
                exceptionCaught = true;
                System.out.println(e.getMessage());
            }
            Assertions.assertFalse(exceptionCaught);
        }catch (FileNotFoundException e){
            System.out.println("File: "+filepath+" not found");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @MethodSource("fileProviderKO")
    void testParserKO(String filepath) {
        assertThrows(ParseException.class, () -> {
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
        return UtilsTest.fileProvider("src/main/resources/data/error/parser_lexer");
    }

    static Stream<org.junit.jupiter.params.provider.Arguments> fileProvider() throws IOException {
        return UtilsTest.fileProvider("src/main/resources/data/success");
    }


}
