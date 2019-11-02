package jsf.annotation;

import jsf.type.JsfUIColumnType;

/**
 *
 * @author Owner
 */
public @interface JsfUIColumn {
    public String componentId();
    public String labelTitle();
    public JsfUIColumnType columnType();
}
