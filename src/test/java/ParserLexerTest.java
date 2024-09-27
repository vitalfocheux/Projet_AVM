import org.junit.Test;

import fr.m1comp5.LexerParserGenerator.MiniJajaParser.MiniJaja;
import fr.m1comp5.LexerParserGenerator.MiniJajaParser.ParseException;

import java.io.File;
import java.io.StringReader;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class ParserLexerTest {
    @Test
    public void testValidInput() {
        String path = "target/classes/data/1.mjj";
        displayFileContents(path);
        MiniJaja parser = null;

        try {
            parser = MiniJaja.getInstance(path);
            assert parser != null;
            parser.classe();
            assertTrue(true);
        } catch (ParseException e) {
            fail("Parse exception was thrown for valid input " + e.getMessage());
        }
    }

    private void displayFileContents(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            System.out.println("Contents of " + filePath + ":");
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }
}
