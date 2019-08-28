package util;

/**
 *
 * @author Owner
 */
public class Matchers {
    public static Matcher string(String input)
    {
        return target -> {
            if(target.length() < input.length()) return MatchResult.supply();
            if(target.indexOf(input)!=0) return MatchResult.failure();

            int consumeCharCount = input.length();
            String matchString   = input;
            return MatchResult.success(consumeCharCount, matchString);
        };
    }
    
    public static Matcher stringNot(String input)
    {
        return target -> {
            if(target.length() < input.length()) return MatchResult.supply();
            if(target.indexOf(input)==0) return MatchResult.failure();
            
            int consumeCharCount = input.length();
            String matchString   = target.substring(0, input.length());
            return MatchResult.success(consumeCharCount, matchString);
        };
    }
    
    public static Matcher space()
    {
        return string(" ");
    }
}
