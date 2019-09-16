package util;

/**
 *
 * @author Owner
 */
public class Matchers {
    public static Matcher<Character> character(char input)
    {
        return target -> {
            if(target.length() < 1) return MatchResult.supply();
            if(target.charAt(0)!=input) return MatchResult.failure();
            
            return MatchResult.success(1, input);
        };
    }
    
    public static Matcher<Character> characterNot(char input)
    {
        return target -> {
            if(target.length() < 1) return MatchResult.supply();
            if(target.charAt(0)==input) return MatchResult.failure();
            
            return MatchResult.success(1, target.charAt(0));
        };
    }
    
    public static Matcher<String> string(String input)
    {
        return target -> {
            if(target.length() < input.length()) return MatchResult.supply();
            if(target.indexOf(input)!=0) return MatchResult.failure();

            int consumeCharCount = input.length();
            String matchString   = input;
            return MatchResult.success(consumeCharCount, matchString);
        };
    }
    
    public static Matcher<String> stringNot(String input)
    {
        return target -> {
            if(target.length() < input.length()) return MatchResult.supply();
            if(target.indexOf(input)==0) return MatchResult.failure();
            
            int consumeCharCount = input.length();
            String matchString   = target.substring(0, input.length());
            return MatchResult.success(consumeCharCount, matchString);
        };
    }
    
    public static Matcher<Character> space()
    {
        return character(' ');
    }
}
