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
        
        final Parser parser = new Parser();
        
        ParseProcessor.start("aiueo kakikukeko sasisuseso")
                .parse(ctx -> parser.parse(ctx, Matchers.string("aiueo")))
                .peek (ctx -> {
                        assertEquals(" kakikukeko sasisuseso", ctx.getTarget());
                        assertEquals(ParseResult.SUCCESS, ctx.getParseResult());
                    })
                .parse(ctx -> parser.parse(ctx, Matchers.space()))
                .peek (ctx -> {
                        assertEquals("kakikukeko sasisuseso", ctx.getTarget());
                        assertEquals(ParseResult.SUCCESS, ctx.getParseResult());
                    })
                .parse(ctx -> parser.parse(ctx, Matchers.string("kakikukeko")))
                .peek (ctx -> {
                        assertEquals(" sasisuseso", ctx.getTarget());
                        assertEquals(ParseResult.SUCCESS, ctx.getParseResult());
                    })
                .parse(ctx -> parser.parse(ctx, Matchers.space()))
                .peek (ctx -> {
                        assertEquals("sasisuseso", ctx.getTarget());
                        assertEquals(ParseResult.SUCCESS, ctx.getParseResult());
                    })
                .parse(ctx -> parser.parse(ctx, Matchers.string("sasisuseso")))
                .peek (ctx -> {
                        assertEquals("", ctx.getTarget());
                        assertEquals(ParseResult.SUCCESS, ctx.getParseResult());
                 });
                
                        
                
                
        
//        Parser parser = new Parser();
//        ParserContext context = new ParserContext("aiueo kakikukeko sasisuseso");
//        
//        context = parser.parse(context, Matchers.string("aiueo"));
//        assertEquals(" kakikukeko sasisuseso", context.getTarget());
//        assertEquals(ParseResult.SUCCESS, context.getParseResult());
//        
//        context = parser.parse(context, Matchers.space());
//        assertEquals("kakikukeko sasisuseso", context.getTarget());
//        assertEquals(ParseResult.SUCCESS, context.getParseResult());
//        
//        context = parser.parse(context, Matchers.string("kakikukeko"));
//        assertEquals(" sasisuseso", context.getTarget());
//        assertEquals(ParseResult.SUCCESS, context.getParseResult());
//        
//        context = parser.parse(context, Matchers.space());
//        assertEquals("sasisuseso", context.getTarget());
//        assertEquals(ParseResult.SUCCESS, context.getParseResult());
//        
//        context = parser.parse(context, Matchers.string("sasisuseso"));
//        assertEquals("", context.getTarget());
//        assertEquals(ParseResult.SUCCESS, context.getParseResult());
    }
    
    @Test
    public void testParserFailure() {
        System.out.println("testParserFailure");
        
        Parser parser = new Parser();
        ParserContext context = new ParserContext("aiueo kakikukeko sasisuseso");
        
        
//        
//        List<String> parseResults = 
//            Parser.build("aiueo kakikukeko sasisuseso")
//                .match(Matchers.string("aiueo"))
//                .match(Matchers.space())
//                .match(Matchers.string("KAKIKUKEKO"))
//                .match(Matchers.space())
//                .match(Matchers.string("sasisuseso"))
//                .parse(ctx -> ctx.getMatchResults()
//                                    .stream()
//                                    .map(r -> r.getConsumeCharCount() + ":" + r.getMatchString() + ":" + r.getType().name())
//                                    .collect(Collectors.toList()));
//        System.out.println(String.join("\n", parseResults));
//        assertEquals(5, parseResults.size());
//        assertEquals("5:aiueo:SUCCESS", parseResults.get(0));
//        assertEquals("1: :SUCCESS", parseResults.get(1));
//        assertEquals("0::FAILURE", parseResults.get(2));    //★あるマッチングに失敗すると以降のマッチングもすべて失敗として扱う
//        assertEquals("0::FAILURE", parseResults.get(3));
//        assertEquals("0::FAILURE", parseResults.get(4));
    }
    
    @Test
    public void testParserDoubleQuoteString()
    {
        System.out.println("testParserDoubleQuoteString");
        
       final Parser parser = new Parser();
        
        ParseProcessor.start("\"aiueo\"")
                .parse(ctx -> parser.parse(ctx, Matchers.string("\"")))
                .parse(ctx -> {
                    StringBuilder sb = new StringBuilder();
                    ParserContext<String> context;
                    
                    context = parser.parse(ctx, Matchers.stringNot("\""));
                    while(context.isSuccess()) {
                        sb.append(context.getValue());
                        context = parser.parse(context, Matchers.stringNot("\""));
                    }
                    context = parser.parse(context, Matchers.string("\""));
                    return new ParserContext<>(context)
                                .replaceValue(sb.toString());
                })
                .peek (ctx -> {
                        assertEquals("", ctx.getTarget());
                        assertEquals("aiueo", ctx.getValue());
                        assertEquals(ParseResult.SUCCESS, ctx.getParseResult());
                    });
    }
    
    @Test
    public void testParserDoubleQuoteString2()
    {
        System.out.println("testParserDoubleQuoteString2");

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
}
