package util;

import java.util.function.Function;
import java.util.function.Supplier;
import util.ParseResult;

/**
 *
 * @author Owner
 */
public class Parser {
    private Supplier<String> provider;
    
    public Parser()
    {
        this.provider = () -> null;
    }
    
    public Parser(Supplier<String> provider)
    {
        this.provider = provider;
    }
    
    public <T> ParserContext<String> parse(ParserContext<T> context, Matcher matcher)
    {
        String target = context.getTarget();

        MatchResult matchResult = matcher.match(target);
        while(matchResult.getType()==MatchResult.Type.SUPPLY) {
            String addInput = provider.get();
            if(addInput == null) {
                matchResult = MatchResult.failure();
                break;
            }
            target += addInput;

            matchResult = matcher.match(target);
        }
        target = target.substring(matchResult.getConsumeCharCount());
        
        String matchString = matchResult.getMatchString();
        
        ParseResult parseResult
                = matchResult.getType() == MatchResult.Type.SUCCESS
                    ? ParseResult.SUCCESS 
                    : ParseResult.FAILURE;
        return new ParserContext<>(target, matchString, parseResult);
    }
}
