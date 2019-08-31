package util;

import java.util.List;
import java.util.function.Supplier;

/**
 *
 * @author Owner
 */
public class ParserContext<T> {
   
    private String target;
    private T value;
    private ParseResult parseResult;

    public ParserContext(String target)
    {
        this.target = target;
        this.value = null;
        this.parseResult = ParseResult.EMPTY;
    }
    
    public ParserContext(ParserContext<T> sourceContext)
    {
        this.target = sourceContext.getTarget();
        this.value = sourceContext.getValue();
        this.parseResult = sourceContext.getParseResult();
    }
    
    public ParserContext(String target, T value, ParseResult parseResult)
    {
        this.target = target;
        this.value = value;
        this.parseResult = parseResult;
    }

    public String getTarget() {
        return target;
    }

    public T getValue() {
        return value;
    }

    public ParseResult getParseResult() {
        return parseResult;
    }
    
    public ParserContext<T> replaceValue(T newValue)
    {
        return new ParserContext<>(target, newValue, parseResult);
    }
    
    public boolean isSuccess() { return parseResult == ParseResult.SUCCESS; }
    public boolean isFailure()  { return parseResult == ParseResult.FAILURE; }
    public boolean isEmpty() { return parseResult == ParseResult.EMPTY; }
}
