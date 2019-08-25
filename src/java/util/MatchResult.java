package util;

/**
 *
 * @author Owner
 */
public class MatchResult {
    public enum Type {
        SUPPLY,
        SUCCESS,
        FAILURE;
    }
    
    private Type type;
    private int consumeCharCount;
    private String matchString;

    public MatchResult(Type type, int consumeCharCount, String matchString)
    {
        this.type = type;
        this.consumeCharCount = consumeCharCount;
        this.matchString = matchString;
    }
    
    public Type   getType()               { return type;}
    public int    getConsumeCharCount()  { return consumeCharCount; }
    public String getMatchString()       { return matchString; }
    
    public static MatchResult success(int consumeCharCount, String matchString)
    {
        return new MatchResult(Type.SUCCESS, consumeCharCount, matchString);
    }
    
    public static MatchResult failure()
    {
        return new MatchResult(Type.FAILURE, 0, "");
    }
    
    public static MatchResult supply()
    {
        return new MatchResult(Type.SUPPLY, 0, "");
    }
}
