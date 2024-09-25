import org.junit.Test;
import static org.junit.Assert.assertEquals;

import fr.m1comp5.LexerParserGenerator.MiniJajaParser.MiniJajaParser;
import fr.m1comp5.LexerParserGenerator.MiniJajaParser.ParseException;

import java.io.File;

public class ParserLexerTest
{

    public void basic() throws ParseException
    {
        MiniJajaParser mjjparser = MiniJajaParser.getInstance("data/1.mjj");
        if (mjjparser != null)
        {
            mjjparser.classe();
        }
    }
}
