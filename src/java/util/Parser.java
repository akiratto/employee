package util;

import java.util.function.Function;
import java.util.function.Supplier;
import util.ParseResult;

/**
 *
 * @author Owner
 */
public class Parser {   
    public static <T,R> ParseContext<R> parse(ParseContext<T> context, Matcher<R> matcher)
    {
        final String target = context.getTarget();
        final Supplier<String> provider = context.getProvider();
        
        final StringBuilder targetSb = new StringBuilder(target);
        final StringBuilder supplyStringSb = new StringBuilder();
        
        MatchResult<R> matchResult = matcher.match(target);
        while(matchResult.getType()==MatchResult.Type.SUPPLY) {
            final String supplyStringPart = provider.get();
            if(supplyStringPart == null) {
                matchResult = MatchResult.failure();
                break;
            }
            targetSb.append(supplyStringPart);
            supplyStringSb.append(supplyStringPart);

            matchResult = matcher.match(target);
        }
        final String newTarget = matchResult.isSuccess()
                    ? targetSb.substring(matchResult.getConsumeCharCount())
                    : targetSb.toString();
        
        final String newConsumedTarget = matchResult.isSuccess()
                            ? targetSb.substring(0, matchResult.getConsumeCharCount())
                            : "";

        final R newMatchedPart = matchResult.getMatchedPart();
        
        final ParseResult newParseResult
                = matchResult.getType() == MatchResult.Type.SUCCESS
                    ? ParseResult.SUCCESS 
                    : ParseResult.FAILURE;
        
        final String newSupplyString = supplyStringSb.toString();
        
        return new ParseContext<>(newTarget, provider, newMatchedPart, newParseResult, newConsumedTarget, newSupplyString);
    }
}
