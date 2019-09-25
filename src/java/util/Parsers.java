package util;

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
}
