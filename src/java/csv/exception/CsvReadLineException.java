package csv.exception;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Owner
 */
public class CsvReadLineException extends Exception {
    private final Path csvFilePath;
    private final int errorOccurrenceLine;
    private final Class errorClazz;
    private final Map<String, List<String>> fieldErrorMessages;
    
    /**
     * Creates a new instance of <code>CsvReadLineException</code> without
     * detail message.
     */
    public CsvReadLineException() {
        this.csvFilePath = null;
        this.errorOccurrenceLine = -1;
        this.errorClazz = null;
        this.fieldErrorMessages = null;
    }

    /**
     * Constructs an instance of <code>CsvReadLineException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CsvReadLineException(String msg) {
        super(msg);
        this.csvFilePath = null;
        this.errorOccurrenceLine = -1;
        this.errorClazz = null;
        this.fieldErrorMessages = null;
    }
    
    public CsvReadLineException(Path csvFilePath, int errorOccureLineCount, Map<String,List<String>> fieldErrorMessages)
    {
        this.csvFilePath = csvFilePath;
        this.errorOccurrenceLine = errorOccureLineCount;
        this.errorClazz = null;
        this.fieldErrorMessages = fieldErrorMessages;
    }
    
    public CsvReadLineException(Path csvFilePath, int errorOccureLineCount, Class errorClazz, Map<String,List<String>> fieldErrorMessages)
    {
        this.csvFilePath = csvFilePath;
        this.errorOccurrenceLine = errorOccureLineCount;
        this.errorClazz = errorClazz;
        this.fieldErrorMessages = fieldErrorMessages;       
    }

    public Class getErrorClazz() {
        return errorClazz;
    }

    public Path getCsvFilePath() {
        return csvFilePath;
    }

    public int getErrorOccurrenceLine() {
        return errorOccurrenceLine;
    }

    public Map<String, List<String>> getFieldErrorMessages() {
        return fieldErrorMessages;
    }
}
