package util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 *
 * @author Owner
 */
public class Parser {
//    private String target;
    private Supplier<String> provider;
    private Function<ParserContext, ParserContext> intermediateOperations;
    
    public static Parser build(String target)
    {
        return build(target, () -> null);
    }
    
    public static Parser build(String target, Supplier<String> provider)
    {
        ParserContext parserContext = new ParserContext(target, new ArrayList<>());
        return new Parser(provider, ctx -> parserContext);
    }
    
    protected Parser(Supplier<String> provider, 
                      Function<ParserContext, ParserContext> intermediateOperations)
    {
        this.provider = provider;
        this.intermediateOperations = intermediateOperations;
    }
    
    protected Parser nextParser(
            Supplier<String> provider, 
            Function<ParserContext, ParserContext> intermediateOperation)
    {
         Function<ParserContext, ParserContext> nextIntermediateOperations 
            = initParserContext -> {
               ParserContext currentParserContext = intermediateOperations.apply(initParserContext);
               List<MatchResult> matchResults = currentParserContext.getMatchResults();
               
               ParserContext nextParserContext = intermediateOperation.apply(currentParserContext);
               return nextParserContext;
           };
        return new Parser(provider, nextIntermediateOperations);
    }
    
    public Parser match(Matcher matcher)
    {
        Function<ParserContext, ParserContext> intermediateOperation = ctx -> {
            String target = ctx.getTarget();
            List<MatchResult> matchResults = ctx.getMatchResults();
            
            MatchResult matchResult = 
                    matchResults.stream().allMatch(r -> r.getConsumeCharCount()>0) 
                        ? matcher.match(target)
                        : MatchResult.failure();
                    
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
            matchResults.add(matchResult);
            return new ParserContext(target, matchResults);
        };
        return nextParser(provider, intermediateOperation);
    }
    
    public boolean allMatch()
    {
        ParserContext parserContext = intermediateOperations.apply(null);
        return parserContext.allMatch();
    }
    
    public String[] matches()
    {
        ParserContext parserContext = intermediateOperations.apply(null);
        List<MatchResult> matchResults = parserContext.getMatchResults();
        return matchResults
                .stream()
                .map(r -> r.getMatchString())
                .collect(Collectors.toList())
                .toArray(new String[matchResults.size()]);
    }
    
    public <R> R parse(Function<ParserContext,R> function)
    {
        ParserContext parserContext = intermediateOperations.apply(null);
        return function.apply(parserContext);
    }
    
    public ParserContext parse(ParserContext context, Matcher matcher)
    {
        match(matcher);
        return intermediateOperations.apply(context); 
    }
}
