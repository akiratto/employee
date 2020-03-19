package presentation.jsf.converter;

/**
 *
 * @author owner
 */
public interface UIColumnConverter {
    public Object convertToFieldObject(String uiColumnValue);
    public String convertToUIColumnValue(Object fieldObject);
}
