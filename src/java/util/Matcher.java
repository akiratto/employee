package util;

/**
 *
 * @author Owner
 */
public interface Matcher<T> {
    MatchResult<T> match(String target);
}
