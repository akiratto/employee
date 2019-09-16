package util;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *
 * @author Owner
 */
public class ParseContext<T> {
    private String target;
    private Supplier<String> provider;
    private T value;
    private ParseResult parseResult;
    private String consumedTarget;
    private String suppliedString;

    public ParseContext(String target)
    {
        this.target = target;
        this.provider = () -> null;
        this.value = null;
        this.parseResult = ParseResult.EMPTY;
        this.consumedTarget = "";
        this.suppliedString = "";
    }
    
    public ParseContext(String target, Supplier<String> provider)
    {
        this.target = target;
        this.provider = provider;
        this.value = null;
        this.parseResult = ParseResult.EMPTY;
        this.consumedTarget = "";
        this.suppliedString = "";
    }
    
    public ParseContext(String target, Supplier<String> provider, T value, ParseResult parseResult, String consumedTarget, String supplyString)
    {
        this.target = target;
        this.provider = provider;
        this.value = value;
        this.parseResult = parseResult;
        this.consumedTarget = consumedTarget;
        this.suppliedString = supplyString;
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

    public Supplier<String> getProvider() {
        return provider;
    }

    public String getConsumedTarget() {
        return consumedTarget;
    }

    public String getSuppliedString() {
        return suppliedString;
    }
    
    public <R> ParseContext<R> map(Function<T,R> function)
    {
        R newValue = function.apply(value);
        return new ParseContext<>(
                this.target,
                this.provider,
                newValue,
                this.parseResult,
                this.consumedTarget,
                this.suppliedString
        );
    }
    
    public boolean isSuccess() { return parseResult == ParseResult.SUCCESS; }
    public boolean isFailure()  { return parseResult == ParseResult.FAILURE; }
    public boolean isEmpty() { return parseResult == ParseResult.EMPTY; }
}
