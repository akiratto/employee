package util;

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
        
        final Parser parser = new Parser();   
        
        ParserContext context = new ParserContext("aiueo kakikukeko sasisuseso");
        
        context = parser.parse(context, Matchers.string("aiueo"));
        assertEquals(" kakikukeko sasisuseso", context.getTarget());
        assertEquals(ParseResult.SUCCESS, context.getParseResult());
        
        context = parser.parse(context, Matchers.space());
        assertEquals("kakikukeko sasisuseso", context.getTarget());
        assertEquals(ParseResult.SUCCESS, context.getParseResult());
        
        context = parser.parse(context, Matchers.string("kakikukeko"));
        assertEquals(" sasisuseso", context.getTarget());
        assertEquals(ParseResult.SUCCESS, context.getParseResult());
        
        context = parser.parse(context, Matchers.space());
        assertEquals("sasisuseso", context.getTarget());
        assertEquals(ParseResult.SUCCESS, context.getParseResult());
        
        context = parser.parse(context, Matchers.string("sasisuseso"));
        assertEquals("", context.getTarget());
        assertEquals(ParseResult.SUCCESS, context.getParseResult());
    }
    
    @Test
    public void testParserFailure() {
        System.out.println("testParserFailure");
        
        final Parser parser = new Parser();
        ParserContext context = new ParserContext("aiueo KAKIKUKEKO sasisuseso");
        
        context = parser.parse(context, Matchers.string("aiueo"));
        assertEquals(" KAKIKUKEKO sasisuseso", context.getTarget());
        assertEquals(ParseResult.SUCCESS, context.getParseResult());
        
        context = parser.parse(context, Matchers.space());
        assertEquals("KAKIKUKEKO sasisuseso", context.getTarget());
        assertEquals(ParseResult.SUCCESS, context.getParseResult());
        
        context = parser.parse(context, Matchers.string("kakikukeko"));
        assertEquals("KAKIKUKEKO sasisuseso", context.getTarget());
        assertEquals(ParseResult.FAILURE, context.getParseResult());
        
        context = parser.parse(context, Matchers.space());
        assertEquals("KAKIKUKEKO sasisuseso", context.getTarget());
        assertEquals(ParseResult.FAILURE, context.getParseResult());
        
        context = parser.parse(context, Matchers.string("sasisuseso"));
        assertEquals("KAKIKUKEKO sasisuseso", context.getTarget());
        assertEquals(ParseResult.FAILURE, context.getParseResult());
    }
    
    @Test
    public void testParserDoubleQuoteString()
    {
        System.out.println("testParserDoubleQuoteString");

        final Parser parser = new Parser();

        ParserContext context = new ParserContext("\"aiueo\"");
        context = parser.parse(context, Matchers.string("\""));
       
        StringBuilder sb = new StringBuilder();
        context = parser.parse(context, Matchers.stringNot("\""));
        while(context.isSuccess()) {
            sb.append(context.getValue());
            context = parser.parse(context, Matchers.stringNot("\""));
        }
        
        context = parser.parse(context, Matchers.string("\""));
        
        assertEquals("", context.getTarget());
        assertEquals("aiueo", sb.toString());
        assertEquals(ParseResult.SUCCESS, context.getParseResult());
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
        final Parser parser = new Parser(() -> textReader.read());   
        
        ParserContext context = new ParserContext("");
        
        context = parser.parse(context, Matchers.string("aiueo"));
        assertEquals("", context.getTarget());
        assertEquals(ParseResult.SUCCESS, context.getParseResult());
        
        context = parser.parse(context, Matchers.space());
        assertEquals("kaki", context.getTarget());
        assertEquals(ParseResult.SUCCESS, context.getParseResult());
        
        context = parser.parse(context, Matchers.string("kakikukeko"));
        assertEquals(" sas", context.getTarget());
        assertEquals(ParseResult.SUCCESS, context.getParseResult());
        
        context = parser.parse(context, Matchers.space());
        assertEquals("sas", context.getTarget());
        assertEquals(ParseResult.SUCCESS, context.getParseResult());
        
        context = parser.parse(context, Matchers.string("sasisuseso"));
        assertEquals("", context.getTarget());
        assertEquals(ParseResult.SUCCESS, context.getParseResult());
    }
}
