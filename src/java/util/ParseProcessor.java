package util;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @author Owner
 */
public class ParseProcessor<T> {
    private ParserContext<T> context;
    
    public static ParseProcessor<String> start(String target)
    {
        return new ParseProcessor<String>(new ParserContext<String>(target));
    }
    
    protected ParseProcessor(ParserContext<T> context)
    {
        this.context = context;
    }
    
    public <R> ParseProcessor<R> parse(Function<ParserContext<T>, ParserContext<R>> function)
    {
        ParseResult prevParseResult = context.getParseResult();
        if(prevParseResult == ParseResult.FAILURE) {
            ParserContext<R> failureContext = new ParserContext(
                                                    context.getTarget(), 
                                                    null, 
                                                    prevParseResult);
            return new ParseProcessor<>(failureContext);
        }
        
        ParserContext<R> nextContext = function.apply(context);
        return new ParseProcessor<R>(nextContext);
    }
    
    public <R> ParseProcessor<R> map(Function<T,R> function)
    {
        T inputValue = this.context.getValue();
        R nextValue = function.apply(inputValue);
        ParserContext<R> nextContext = new ParserContext<>(
                                        context.getTarget(), 
                                        nextValue, 
                                        context.getParseResult());
        return new ParseProcessor(nextContext);
    }
    
    public ParseProcessor<T> peek(Consumer<ParserContext<T>> consumer)
    {
        consumer.accept(this.context);
        return this;
    }
}
