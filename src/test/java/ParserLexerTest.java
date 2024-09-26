import org.junit.Test;
import static org.junit.Assert.assertEquals;

import fr.m1comp5.LexerParserGenerator.MiniJajaParser.MiniJaja;
import fr.m1comp5.LexerParserGenerator.MiniJajaParser.ParseException;

import java.io.File;

public class ParserLexerTest
{
    final String pathToResources = "target/classes/data/";

    @Test
    public void basic() throws ParseException
    {
        MiniJaja mjjparser = MiniJaja.getInstance(pathToResources + "1.mjj");
        mjjparser.classe();
    }
}
