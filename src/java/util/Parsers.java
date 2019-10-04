package util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import static util.Parser.parse;

/**
 *
 * @author Owner
 */
public class Parsers {
    public static <T> ParseContext<String> parseStringEnclosed(ParseContext<T> context, char enclosedCharacter, char escapeCharacter)
    {
        ParseContext<Character> sentAroundContext;
        
        sentAroundContext = parse(context, Matchers.character(enclosedCharacter));
        if(sentAroundContext.isFailure()) return sentAroundContext.map(c -> String.valueOf(c));
        
        StringBuilder sb = new StringBuilder();
        do {
            sentAroundContext = parse(sentAroundContext, Matchers.characterNot(enclosedCharacter));
            if(sentAroundContext.isFailure()) break;
            
            ParseContext<Character> backContext = sentAroundContext;
            char contentChar = sentAroundContext.getValue();
            if(sentAroundContext.getValue() == escapeCharacter) {
                sentAroundContext = parse(sentAroundContext, Matchers.character(escapeCharacter));
                if(sentAroundContext.isSuccess()) {
                    contentChar = escapeCharacter;
                } else {
                    sentAroundContext = backContext;
                    sentAroundContext = parse(sentAroundContext, Matchers.character(enclosedCharacter));
                    if(sentAroundContext.isSuccess()) {
                        contentChar = enclosedCharacter;
                    }
                    else {
                        sentAroundContext = backContext;
                    }
                }
            }                      
            sb.append(contentChar);
        } while(sentAroundContext.isSuccess());
        
        sentAroundContext = sentAroundContext.clearResult(); //パース結果をクリア
        sentAroundContext = parse(sentAroundContext, Matchers.character(enclosedCharacter));
        if(sentAroundContext.isFailure()) return sentAroundContext.map(c -> String.valueOf(c));;
        
        String matchedPart = sb.toString();
        
        return new ParseContext<>(
                sentAroundContext.getTarget(),
                sentAroundContext.getProvider(),
                matchedPart,
                ParseResult.SUCCESS,
                "",
                ""
        );
    }
    
    public static <T> ParseFunction<T,String> parseFuncStringEnclosed(char enclosedCharacter, char escapeCharacter)
    {
        return ctx -> parseStringEnclosed(ctx, enclosedCharacter, escapeCharacter);
    }
    
    public static <T> ParseContext<List<String>> manyDelimiter(ParseContext<T> context, ParseFunction<String,String> function, ParseFunction<String,String> delimiter)
    {
        if(context.isFailure())
            return context.map(v -> null);
        
        List<String> strings = new ArrayList<>();
        ParseContext<String> sentAroundContext = context.map(v -> ""); 
        do {
            sentAroundContext = function.apply(sentAroundContext);
            if(sentAroundContext.isSuccess()) {
                strings.add(sentAroundContext.getValue());
            }
            sentAroundContext = delimiter.apply(sentAroundContext);
        } while(sentAroundContext.isSuccess());
        
        return new ParseContext<>(
                sentAroundContext.getTarget(),
                sentAroundContext.getProvider(),
                strings,
                ParseResult.SUCCESS,
                "",
                ""
        );
    }
    
    public static <T> ParseContext<List<String>> parseCsvLine(ParseContext<T> context)
    {
        return Parsers.manyDelimiter(context, 
                                        Parsers.parseFuncStringEnclosed('"', '\\'), 
                                        Parser.parseFunction(Matchers.string(",")));
    }
    
//    public static <T> ParseContext<T> skipSpaces(ParseContext<T> context)
//    {
//       
//    }
}
