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
}
