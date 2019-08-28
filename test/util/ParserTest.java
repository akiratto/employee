package util;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
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
        
        List<String> parseResults = 
            Parser.build("aiueo kakikukeko sasisuseso")
                .match(Matchers.string("aiueo"))
                .match(Matchers.space())
                .match(Matchers.string("kakikukeko"))
                .match(Matchers.space())
                .match(Matchers.string("sasisuseso"))
                .parse(ctx -> ctx.getMatchResults()
                                    .stream()
                                    .map(r -> r.getConsumeCharCount() + ":" + r.getMatchString() + ":" + r.getType().name())
                                    .collect(Collectors.toList()));
        System.out.println(String.join("\n", parseResults));
        assertEquals(5, parseResults.size());
        assertEquals("5:aiueo:SUCCESS", parseResults.get(0));
        assertEquals("1: :SUCCESS", parseResults.get(1));
        assertEquals("10:kakikukeko:SUCCESS", parseResults.get(2));
        assertEquals("1: :SUCCESS", parseResults.get(3));
        assertEquals("10:sasisuseso:SUCCESS", parseResults.get(4));
    }
    
    @Test
    public void testParserFailure() {
        System.out.println("testParserFailure");
        
        List<String> parseResults = 
            Parser.build("aiueo kakikukeko sasisuseso")
                .match(Matchers.string("aiueo"))
                .match(Matchers.space())
                .match(Matchers.string("KAKIKUKEKO"))
                .match(Matchers.space())
                .match(Matchers.string("sasisuseso"))
                .parse(ctx -> ctx.getMatchResults()
                                    .stream()
                                    .map(r -> r.getConsumeCharCount() + ":" + r.getMatchString() + ":" + r.getType().name())
                                    .collect(Collectors.toList()));
        System.out.println(String.join("\n", parseResults));
        assertEquals(5, parseResults.size());
        assertEquals("5:aiueo:SUCCESS", parseResults.get(0));
        assertEquals("1: :SUCCESS", parseResults.get(1));
        assertEquals("0::FAILURE", parseResults.get(2));    //★あるマッチングに失敗すると以降のマッチングもすべて失敗として扱う
        assertEquals("0::FAILURE", parseResults.get(3));
        assertEquals("0::FAILURE", parseResults.get(4));
    }
    
    @Test
    public void testParserDoubleQuoteString()
    {
        System.out.println("testParserFailure");
        
        Parser p;
        ParserContext ctx;
        p = Parser.build("\"aiueo\"");
        ctx = p.parse(null,Matchers.string("\""));
        ctx = p.parse(ctx, Matchers.stringNot("\""));
        while(ctx.lastMatch()) {
            ctx = p.parse(ctx, Matchers.stringNot("\""));
        }
        ctx = p.parse(ctx, Matchers.string("\""));
        assertTrue(ctx.allMatch());
    }
}
