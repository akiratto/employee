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
//            sentAroundContext = parse(sentAroundContext, Matchers.characterNot(enclosedCharacter));
//            if(sentAroundContext.isFailure()) break;
//            
//            ParserContext<Character> backContext = sentAroundContext;
//            char contentChar = sentAroundContext.getValue();
//            if(sentAroundContext.getValue() == escapeCharacter) {
//                sentAroundContext = parse(sentAroundContext, Matchers.character(escapeCharacter));
//                if(sentAroundContext.isSuccess()) {
//                    contentChar = escapeCharacter;
//                } else {
//                    sentAroundContext = backContext;
//                    sentAroundContext = parse(sentAroundContext, Matchers.character(enclosedCharacter));
//                    if(sentAroundContext.isSuccess()) {
//                        contentChar = enclosedCharacter;
//                    }
//                    else {
//                        sentAroundContext = backContext;
//                    }
//                }
//            }           
            ParseProcessor<Character,Character> parseProcessor
                = ParseProcessor
                    .begin(sentAroundContext, Character.class)
                    .parse(Matchers.characterNot(enclosedCharacter))
                        .setReturnIfSuccess(ParseContext<Character>::getValue)
                        .continueIfSuccess()
                        .continueIf(ctx -> ctx.getValue()==escapeCharacter)
                    .parse(Matchers.character(escapeCharacter))
                        .setReturnIfSuccess(escapeCharacter)
                        .continueIfFailure()
                        .backContext()
                    .parse(Matchers.character(enclosedCharacter))
                        .setReturnIfSuccess(enclosedCharacter)
                    .setReturnIfFailure((Character)null)
                    .end();
            
            sentAroundContext = parseProcessor.returnContext();
            if(sentAroundContext.isFailure()) break;
            
            sb.append(parseProcessor.returnValue());
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
