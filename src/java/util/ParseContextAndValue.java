package util;

/**
 *
 * @author Owner
 */
public class ParseContextAndValue<T,R> extends Tuple<ParseContext<T>, R> {
    public ParseContextAndValue(ParseContext<T> context, R value)
    {
        super(context, value);
    }
}
