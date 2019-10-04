package util;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Owner
 */
public class ParserTest {
    
    public ParserTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of nextParser method, of class Parser.
     */
    @Test
    public void testParserAllSuccess() {
        System.out.println("testParserAllSuccess");
         
        
        ParseContext context = new ParseContext("aiueo kakikukeko sasisuseso");
        
        context = Parser.parse(context, Matchers.string("aiueo"));
        assertEquals(" kakikukeko sasisuseso", context.getTarget());
        assertEquals(ParseResult.SUCCESS, context.getParseResult());
        
        context = Parser.parse(context, Matchers.space());
        assertEquals("kakikukeko sasisuseso", context.getTarget());
        assertEquals(ParseResult.SUCCESS, context.getParseResult());
        
        context = Parser.parse(context, Matchers.string("kakikukeko"));
        assertEquals(" sasisuseso", context.getTarget());
        assertEquals(ParseResult.SUCCESS, context.getParseResult());
        
        context = Parser.parse(context, Matchers.space());
        assertEquals("sasisuseso", context.getTarget());
        assertEquals(ParseResult.SUCCESS, context.getParseResult());
        
        context = Parser.parse(context, Matchers.string("sasisuseso"));
        assertEquals("", context.getTarget());
        assertEquals(ParseResult.SUCCESS, context.getParseResult());
    }
    
    @Test
    public void testParserFailure() {
        System.out.println("testParserFailure");
        
        ParseContext context = new ParseContext("aiueo KAKIKUKEKO sasisuseso");
        
        context = Parser.parse(context, Matchers.string("aiueo"));
        assertEquals(" KAKIKUKEKO sasisuseso", context.getTarget());
        assertEquals(ParseResult.SUCCESS, context.getParseResult());
        
        context = Parser.parse(context, Matchers.space());
        assertEquals("KAKIKUKEKO sasisuseso", context.getTarget());
        assertEquals(ParseResult.SUCCESS, context.getParseResult());
        
        context = Parser.parse(context, Matchers.string("kakikukeko"));
        assertEquals("KAKIKUKEKO sasisuseso", context.getTarget());
        assertEquals(ParseResult.FAILURE, context.getParseResult());
        
        context = Parser.parse(context, Matchers.space());
        assertEquals("KAKIKUKEKO sasisuseso", context.getTarget());
        assertEquals(ParseResult.FAILURE, context.getParseResult());
        
        context = Parser.parse(context, Matchers.string("sasisuseso"));
        assertEquals("KAKIKUKEKO sasisuseso", context.getTarget());
        assertEquals(ParseResult.FAILURE, context.getParseResult());
    }
    
    @Test
    public void testParserDoubleQuoteString()
    {
        System.out.println("testParserDoubleQuoteString");

        ParseContext<String> context = new ParseContext("\"aiueo\"");
        context = Parsers.parseStringEnclosed(context, '"', '\\');
        
        assertEquals("", context.getTarget());
        assertEquals("aiueo", context.getValue());
        assertEquals(ParseResult.SUCCESS, context.getParseResult());
    }
    
    @Test
    public void testParserDoubleQuoteString2()
    {
        System.out.println("testParserDoubleQuoteString2");
        
        ParseContext<String> context = new ParseContext("\"\\\\AIUEO\\\\\\\"\""); //"\\AIUEO\\\"" => \AIUEO\"
        context = Parsers.parseStringEnclosed(context, '"', '\\');
        
        assertEquals("", context.getTarget());
        assertEquals("\\AIUEO\\\"", context.getValue());
        assertEquals(ParseResult.SUCCESS, context.getParseResult());
    }
    
    @Test
    public void testParserCsvLine()
    {
        System.out.println("testParserCsvLine");
        
        final String csvLine = "\"aiueo\",\"kakikukeko\",\"sasisuseso\"";
        ParseContext<String> context = new ParseContext(csvLine);
        ParseContext<List<String>> newContext 
                = Parsers.manyDelimiter(context, 
                                        Parsers.parseFuncStringEnclosed('"', '\\'), 
                                        Parser.parseFunction(Matchers.string(",")));
        
        assertEquals(ParseResult.SUCCESS, newContext.getParseResult());
        assertEquals("", newContext.getTarget());
        assertEquals("aiueo", newContext.getValue().get(0));
        assertEquals("kakikukeko", newContext.getValue().get(1));
        assertEquals("sasisuseso", newContext.getValue().get(2));
        
        
    }
            
    
    
    static class TextReader {
        private String allString = "aiueo kakikukeko sasisuseso";
        private int index = 0;
        private final int LENGTH = 5;
        
        //LENGTHの文字数ずつ読み込む。すべて読み込み済みの場合はnullを返す。
        public String read()
        {
            if(allString.length() <= index) {
                return null;
            }
            int len = index + LENGTH > allString.length() 
                        ? allString.length() - index 
                        : LENGTH;
            String s = allString.substring(index, index+len);
            index = index + LENGTH;
            System.out.println("  s=" + s);
            return s;
        }
        
    };
    
    @Test
    public void testParserAllSuccessWithReader() {
        System.out.println("testParserAllSuccessWithReader");
        
        final TextReader textReader = new TextReader();
//        final Parser parser = new Parser(() -> textReader.read());   
        
        ParseContext context = new ParseContext("", () -> textReader.read());
        
        context = Parser.parse(context, Matchers.string("aiueo"));
        assertEquals("", context.getTarget());
        assertEquals(ParseResult.SUCCESS, context.getParseResult());
        
        context = Parser.parse(context, Matchers.space());
        assertEquals("kaki", context.getTarget());
        assertEquals(ParseResult.SUCCESS, context.getParseResult());
        
        context = Parser.parse(context, Matchers.string("kakikukeko"));
        assertEquals(" sas", context.getTarget());
        assertEquals(ParseResult.SUCCESS, context.getParseResult());
        
        context = Parser.parse(context, Matchers.space());
        assertEquals("sas", context.getTarget());
        assertEquals(ParseResult.SUCCESS, context.getParseResult());
        
        context = Parser.parse(context, Matchers.string("sasisuseso"));
        assertEquals("", context.getTarget());
        assertEquals(ParseResult.SUCCESS, context.getParseResult());
    }
}
