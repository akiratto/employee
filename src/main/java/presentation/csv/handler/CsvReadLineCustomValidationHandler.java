package presentation.csv.handler;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Owner
 */
public interface CsvReadLineCustomValidationHandler {
    public Map<String, List<String>> validate(Class<?> clazz, Map<String,String> headerAndValues);
}
