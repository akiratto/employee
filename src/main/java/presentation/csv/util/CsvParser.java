package presentation.csv.util;

import java.util.function.Supplier;
import util.Tuple;

/**
 *
 * @author Owner
 */
public class CsvParser {
    private String target;
    private Supplier<String> reader;
    
    public CsvParser(String readedString, Supplier<String> reader)
    {
        this.target = readedString;
        this.reader = reader;
    }

    public String getTarget() {
        return target;
    }

    public Supplier<String> getReader() {
        return reader;
    }
    
    private String[] parseCsvLine()
    {
        
        return null;
    }
    
    private String parseStringUntilComma()
    {
        Tuple<Boolean, String> ret;
        
        
        
        int index = 0;
        StringBuilder sb = new StringBuilder();
        char c = target.charAt(index);
        while(c != ',') {
            sb.append(c);
            index++;
            c = target.charAt(index);
        }
        return sb.toString();
    }
    
    public Tuple<Boolean,String> parseStringEnclosedInDoubleQuotes()
    {
        Tuple<Boolean, String> ret;
        
        if(parseString("\"")._1==false) {
            return new Tuple<>(false, "");
        }
        
        StringBuilder sb = new StringBuilder();
        ret = parseStringNot("\"");
        while(ret._1) {
            sb.append(ret._2);
            ret = parseStringNot("\"");
        }
        
        if(parseString("\"")._1==false) {
            return new Tuple<>(false, "");
        }
        return new Tuple<>(true, sb.toString());
    }
    
    public Tuple<Boolean, String> parseString(String input)
    {
        Boolean match = false;
        if(input.length() > target.length()) {
            match = false;
            input = "";
            target = "";
            
        } else if(target.indexOf(input)==0) {
            match = true;
            target = target.substring(input.length());
        }
        return new Tuple<>(match, input);
    }
    
    public Tuple<Boolean, String> parseStringNot(String input)
    {
        Boolean match = false;
        String notInput = "";
        
        if(input.length() > target.length()) {
            match = true;
            notInput = target;
            target = "";
            
        } else if(target.indexOf(input)!=0) {
            match = true;
            notInput = target.substring(0, input.length());
            target = target.substring(input.length());
        }
        return new Tuple<>(match, notInput);
    }
}
