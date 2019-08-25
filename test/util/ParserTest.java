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
    
    public void testParser() {
        
    }
}
