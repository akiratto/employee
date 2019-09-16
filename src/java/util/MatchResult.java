package util;

/**
 *
 * @author Owner
 */
public class MatchResult<T> {
    public enum Type {
        SUPPLY,
        SUCCESS,
        FAILURE;
    }
    
    private Type type;
    private int consumeCharCount;
    private T matchedPart;

    public MatchResult(Type type, int consumeCharCount, T matchedPart)
    {
        this.type = type;
        this.consumeCharCount = consumeCharCount;
        this.matchedPart = matchedPart;
    }
    
    public Type   getType()               { return type;}
    public int    getConsumeCharCount()  { return consumeCharCount; }
    public T      getMatchedPart()       { return matchedPart; }
    
    public boolean isSuccess() { return type == Type.SUCCESS; }
    public boolean isFailure() { return type == Type.FAILURE; }
    public boolean isSupply() { return type == Type.SUPPLY; }
    
    public static <T> MatchResult<T> success(int consumeCharCount, T matchedPart)
    {
        return new MatchResult(Type.SUCCESS, consumeCharCount, matchedPart);
    }
    
    public static <T> MatchResult<T> failure()
    {
        return new MatchResult(Type.FAILURE, 0, null);
    }
    
    public static <T> MatchResult supply()
    {
        return new MatchResult(Type.SUPPLY, 0, null);
    }
}
