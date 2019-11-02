package jsf.type;

/**
 *
 * @author Owner
 */
public enum JsfUIColumnType {
    HTML_INPUT_TEXT,
    HTML_INPUT_TEXTAREA,
    HTML_INPUT_HIDDEN,
    HTML_INPUT_SECRET,
    HTML_INPUT_FILE,
    UI_SELECT_BOOLEAN,
    UI_SELECT_MANY,
    UI_SELECT_ONE,
    UI_VIEW_PARAMETER;
    
    public boolean eqHtmlInputText() { return this == HTML_INPUT_TEXT; }
    public boolean eqHtmlInputTextArea() { return this == HTML_INPUT_TEXTAREA; }
    public boolean eqHtmlInputHidden() { return this == HTML_INPUT_HIDDEN; }
    public boolean eqHtmlInputSecret() { return this == HTML_INPUT_SECRET; }
    public boolean eqHtmlInputFile() { return this == HTML_INPUT_FILE; }
    public boolean eqUISelectBoolean() { return this == UI_SELECT_BOOLEAN; }
    public boolean eqUISelectMany() { return this == UI_SELECT_MANY; }
    public boolean eqUISelectOne() { return this == UI_SELECT_ONE; }
    public boolean eqUIViewParameter() { return this == UI_VIEW_PARAMETER; }
}
