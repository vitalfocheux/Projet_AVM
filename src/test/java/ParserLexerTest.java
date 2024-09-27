import org.junit.Test;

import fr.m1comp5.LexerParserGenerator.MiniJajaParser.MiniJaja;
import fr.m1comp5.LexerParserGenerator.MiniJajaParser.ParseException;

import java.io.File;
import java.io.StringReader;

import static org.junit.Assert.*;

public class ParserLexerTest {
    @Test
    public void testValidInput() {
        String input = "class Test { final int x = 10; main{ int x = 3+4;} }";
        MiniJaja parser = new MiniJaja(new StringReader(input));

        try {
            parser.classe();
            assertTrue(true);
        } catch (ParseException e) {
            fail("Parse exception was thrown for valid input" + e.getMessage());
        }
    }
}
