package util;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 *
 * @author Owner
 */
public class ParseProcessor<T,R> {
    public enum Status {
        BEGAN,
        ENDED
    }
    private final Status status;
    private final StringBuilder consumedTargetSb;
    private final ParseContext<T> sentAroundContext;
    private final ParseContext<T> backContext;
    private final boolean whetherToContinue;
    private final R returnValue;
    
    private ParseProcessor(Status status, StringBuilder consumedTargetSb, ParseContext<T> sentAroundContext, ParseContext<T> backContext, boolean whetherToContinue, R returnValue)
    {
        this.status = status;
        this.consumedTargetSb = consumedTargetSb;
        this.sentAroundContext = sentAroundContext;
        this.backContext = backContext;
        this.whetherToContinue = whetherToContinue;
        this.returnValue = returnValue;
    }
    
    private static <T,R> ParseProcessor<T,R> cont(Status status, StringBuilder consumedTargetSb, ParseContext<T> sentAroundContext, ParseContext<T> backContext, boolean whetherToContinue, R returnValue)
    {
        return new ParseProcessor<>(status,
                                    consumedTargetSb, 
                                    sentAroundContext, 
                                    backContext, 
                                    whetherToContinue, 
                                    returnValue);
    }
 
    
    public static <T,R> ParseProcessor<T,R> begin(ParseContext<T> sentAroundContext, Class<R> clazz)
    {
        return cont(Status.BEGAN, new StringBuilder(sentAroundContext.getConsumedTarget()), sentAroundContext, sentAroundContext, true, null);
    }
    
    public ParseProcessor<T,R> parse(Matcher<T> matcher)
    {
        if(status == Status.ENDED)
            throw new IllegalStateException("The parsing process has already ended.");
        
        final ParseContext<T> newSendArounContext = whetherToContinue ? Parser.parse(sentAroundContext, matcher)
                                                                      : sentAroundContext;
        final ParseContext<T> newBackContext      = whetherToContinue ? sentAroundContext
                                                                      : backContext;
        
        final StringBuilder newStringBuilder 
                = whetherToContinue ? consumedTargetSb.append(newSendArounContext.getConsumedTarget())
                                    : consumedTargetSb;
        return cont(status, consumedTargetSb, newSendArounContext, newBackContext, whetherToContinue, returnValue);
    }
    
    public ParseProcessor<T,R> continueIf(Predicate<ParseContext<T>> predicate)
    {
        if(status == Status.ENDED)
            throw new IllegalStateException("The parsing process has already ended.");
        
        return whetherToContinue
                ? cont(status, consumedTargetSb, sentAroundContext, backContext, predicate.test(sentAroundContext), returnValue)
                : cont(status, consumedTargetSb, sentAroundContext, backContext, whetherToContinue, returnValue);
    }
    
    public ParseProcessor<T,R> continueIfSuccess()
    {
        return continueIf(ParseContext<T>::isSuccess);
    }
    
    public ParseProcessor<T,R> continueIfFailure()
    {
        return continueIf(ParseContext<T>::isFailure);
    }
    
    public ParseProcessor<T,R> setReturnIf(Predicate<ParseContext<T>> predicate, Function<ParseContext<T>,R> function)
    {
        if(status == Status.ENDED)
            throw new IllegalStateException("The parsing process has already ended.");
        
        return whetherToContinue && predicate.test(sentAroundContext)
                ? cont(status, consumedTargetSb, sentAroundContext, backContext, whetherToContinue, function.apply(sentAroundContext))
                : cont(status, consumedTargetSb, sentAroundContext, backContext, whetherToContinue, returnValue);
    }
    
    public ParseProcessor<T,R> setReturn(Function<ParseContext<T>,R> function)
    {
        return setReturnIf(ctx -> true, function);
    }
    
    public ParseProcessor<T,R> setReturn(R returnValue)
    {
        return setReturn(ctx -> returnValue);
    }
    
    public ParseProcessor<T,R> setReturnIfSuccess(Function<ParseContext<T>,R> function)
    {
        return setReturnIf(ParseContext<T>::isSuccess, function); 
    }
    
    public ParseProcessor<T,R> setReturnIfSuccess(R returnValue)
    {
        return setReturnIf(ParseContext<T>::isSuccess, ctx -> returnValue); 
    }
    
    public ParseProcessor<T,R> setReturnIfFailure(Function<ParseContext<T>,R> function)
    {
        return setReturnIf(ParseContext<T>::isFailure, function); 
    }
    
    public ParseProcessor<T,R> setReturnIfFailure(R returnValue)
    {
        return setReturnIf(ParseContext<T>::isFailure, ctx -> returnValue); 
    }
    
    public ParseProcessor<T,R> backContext()
    {
        if(status == Status.ENDED)
            throw new IllegalStateException("The parsing process has already ended.");
        
        if(sentAroundContext == backContext)
            throw new IllegalStateException("The parsing context can't back.");
        
        ParseContext<T> newBackContext 
                = new ParseContext<>(
                    sentAroundContext.getConsumedTarget() + sentAroundContext.getTarget(),
                    backContext.getProvider(),
                    backContext.getValue(),
                    backContext.getParseResult(),
                    backContext.getConsumedTarget(),
                    backContext.getSuppliedString()
                );
        
        return whetherToContinue
                ? cont(status, consumedTargetSb, newBackContext, newBackContext, whetherToContinue, returnValue)
                : cont(status, consumedTargetSb, sentAroundContext, backContext, whetherToContinue, returnValue);
    }
    
    public ParseProcessor<T,R> loopUntil(Predicate<ParseContext<T>> condition, Function<ParseProcessor<T,R>, ParseProcessor<T,R>> loopProcess)
    {
        if(status == Status.ENDED)
            throw new IllegalStateException("The parsing process has already ended.");
        
        if(whetherToContinue) {
            ParseProcessor<T,R> loopParseProcessor = null;
            ParseContext<T> loopContext = sentAroundContext;
            StringBuilder newConsumedTargetSb = new StringBuilder(consumedTargetSb);
            while( !condition.test(loopContext) ) {
                loopParseProcessor = loopProcess
                                        .apply(ParseProcessor.begin(loopContext, (Class<R>)returnValue.getClass()))
                                        .end();
                loopContext = loopParseProcessor.returnContext();
                
                String consumedTarget = loopParseProcessor.returnConsumedTarget();
                if(consumedTarget.isEmpty()==false) {
                    newConsumedTargetSb.append(consumedTarget);
                }
            };
            final ParseContext<T> newContext = loopContext;
            final ParseContext<T> newBackContext = sentAroundContext;
            final R newReturnValue = loopParseProcessor != null 
                                        ? loopParseProcessor.returnValue() 
                                        : returnValue;
            
            return cont(status, newConsumedTargetSb, newContext, newBackContext, whetherToContinue, newReturnValue); 
        } else {
            return cont(status, consumedTargetSb, sentAroundContext, backContext, whetherToContinue, returnValue);
        }
    }
    
    public ParseProcessor<T,R> loopUntilSuccess(Function<ParseProcessor<T,R>, ParseProcessor<T,R>> loopProcess)
    {
        return loopUntil(ctx -> ctx.isSuccess(), loopProcess);
    }
    
    public ParseProcessor<T,R> loopUntilFailure(Function<ParseProcessor<T,R>, ParseProcessor<T,R>> loopProcess)
    {
        return loopUntil(ctx -> ctx.isFailure(), loopProcess);
    }
    
    public ParseProcessor<T,R> end()
    {
        if(status == Status.ENDED)
            throw new IllegalStateException("The parsing process has already ended.");
        
        return cont(Status.ENDED, consumedTargetSb, sentAroundContext, backContext, whetherToContinue, returnValue);
    }
    
    public R returnValue()
    {
        if(status == Status.BEGAN)
            throw new IllegalStateException("The parsing process has not finished yet.");
        
        return returnValue;
    }
    
    public ParseContext<T> returnContext()
    {
        if(status == Status.BEGAN)
            throw new IllegalStateException("The parsing process has not finished yet.");
        
        return sentAroundContext;
    }
    
    public String returnConsumedTarget()
    {
        return consumedTargetSb.toString();
    }
    
    public ParseContextAndValue<T,R> returnContextAndValue()
    {
        if(status == Status.BEGAN)
            throw new IllegalStateException("The parsing process has not finished yet.");
        
        return new ParseContextAndValue(sentAroundContext, returnValue);
    }
    
}
