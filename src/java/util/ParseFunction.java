package util;

import java.util.function.Function;

/**
 *
 * @author Owner
 */
public interface ParseFunction<T,R> extends Function<ParseContext<T>,ParseContext<R>> {
    
}
