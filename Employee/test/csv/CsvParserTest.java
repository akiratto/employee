/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csv;

import util.csv.CsvParser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import type.Tuple;

/**
 *
 * @author Owner
 */
public class CsvParserTest {
    
    public CsvParserTest() {
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
     * Test of parseString method, of class CsvParser.
     */
    @Test
    public void testParseString() {
        System.out.println("parseString");
        String input = "";
        CsvParser instance = new CsvParser("aiueo,kakikukeko,sasisuseso", null);
        Tuple<Boolean, String> ret;
        ret = instance.parseString("aiueo,");
        assertEquals(true, ret._1);
        assertEquals("aiueo,", ret._2);
        assertEquals("kakikukeko,sasisuseso", instance.getTarget());
        
        ret = instance.parseString("kakikukeko,");
        assertEquals(true, ret._1);
        assertEquals("kakikukeko,", ret._2);
        assertEquals("sasisuseso", instance.getTarget());
        
        ret = instance.parseString("sasisuseso");
        assertEquals(true, ret._1);
        assertEquals("sasisuseso", ret._2);
        assertEquals("", instance.getTarget());
        
        ret = instance.parseString("tatituteto");
        assertEquals(false, ret._1);
        assertEquals("", ret._2);
        assertEquals("", instance.getTarget());
    }

    /**
     * Test of parseStringNot method, of class CsvParser.
     */
    @Test
    public void testParseStringNot() {
        System.out.println("parseStringNot");
        CsvParser instance = new CsvParser("\"aiueo\",\"kakikukeko\",\"sasisuseso\"", null);
        Tuple<Boolean, String> ret;
        
        ret = instance.parseString("\"");
        assertEquals(true, ret._1);
        
        StringBuilder sb = new StringBuilder();
        ret = instance.parseStringNot("\"");
        while(ret._1) {
            sb.append(ret._2);
            ret = instance.parseStringNot("\"");
        }
        
        assertEquals("aiueo", sb.toString());
        assertEquals(false, ret._1);
        assertEquals("",  ret._2);
        assertEquals("\",\"kakikukeko\",\"sasisuseso\"", instance.getTarget());
        
        ret = instance.parseString("\"");
        assertEquals(true, ret._1);
        assertEquals("\"", ret._2);
        assertEquals(",\"kakikukeko\",\"sasisuseso\"", instance.getTarget());
    }
    
    @Test
    public void testParseStringEnclosedInDoubleQuotes()
    {
        System.out.println("parseStringEnclosedInDoubleQuotes");
        CsvParser instance = new CsvParser("\"aiueo\",\"kakikukeko\",\"sasisuseso\"", null);
        Tuple<Boolean, String> ret;
        
        ret = instance.parseStringEnclosedInDoubleQuotes();
        assertEquals(true, ret._1);
        assertEquals("aiueo", ret._2);
        assertEquals(",\"kakikukeko\",\"sasisuseso\"", instance.getTarget());
        
        ret = instance.parseString(",");
        assertEquals(true, ret._1);
        assertEquals("\"kakikukeko\",\"sasisuseso\"", instance.getTarget());
        
        ret = instance.parseStringEnclosedInDoubleQuotes();
        assertEquals(true, ret._1);
        assertEquals("kakikukeko", ret._2);
        assertEquals(",\"sasisuseso\"", instance.getTarget());
        
        ret = instance.parseString(",");
        assertEquals(true, ret._1);
        assertEquals("\"sasisuseso\"", instance.getTarget());
        
        ret = instance.parseStringEnclosedInDoubleQuotes();
        assertEquals(true, ret._1);
        assertEquals("sasisuseso", ret._2);
        assertEquals("", instance.getTarget());
    }
}
