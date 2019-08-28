package util;

import java.util.List;
import java.util.function.Supplier;

/**
 *
 * @author Owner
 */
public class ParserContext {
    private String target;
    private List<MatchResult> matchResults;
    
    public ParserContext(String target, List<MatchResult> matchResults)
    {
        this.target = target;
        this.matchResults = matchResults;
    }

    public String getTarget() {
        return target;
    }

    public List<MatchResult> getMatchResults() {
        return matchResults;
    }
    
    public boolean allMatch()
    {
        List<MatchResult> matchResults = getMatchResults();
        return matchResults
                .stream()
                .allMatch(r -> Boolean.valueOf(r.getConsumeCharCount() > 0)); 
    }
    
    public boolean lastMatch()
    {
        MatchResult.Type lastMatchResultType
                = getMatchResults().size() > 0
                    ? getMatchResults().get(getMatchResults().size() - 1).getType() 
                    : MatchResult.Type.FAILURE;
        
        return lastMatchResultType == MatchResult.Type.SUCCESS;
    }
}
