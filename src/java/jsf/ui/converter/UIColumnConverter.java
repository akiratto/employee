package jsf.ui.converter;

/**
 *
 * @author owner
 */
public interface UIColumnConverter {
    public String convertToFieldObject(Object fieldObject);
    public Object convertToUIColumnValue(String uiColumnValue);
}
