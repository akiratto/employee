package util;

import static util.Parser.parse;

/**
 *
 * @author Owner
 */
public class Parsers {
    public static <T> ParseContext<String> parseStringEnclosed(ParseContext<T> context, char enclosedCharacter, char escapeCharacter)
    {
        ParseProcessor<Character,Character> parseProcessor
            = ParseProcessor
                .begin(context, Character.class)
                .parse(Matchers.character(enclosedCharacter))
                .loopUntilFailure(processor -> 
                    processor
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
                )
                .parse(Matchers.character(enclosedCharacter))
                .end();
           
    }
}
