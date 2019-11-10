package jsf.ui.annotation;

import jsf.type.JsfUIColumnType;

/**
 *
 * @author Owner
 */
public @interface JsfUIListColumn {
    public boolean visible() default true;
    public String componentId();
    public String labelTitle();
    public JsfUIColumnType columnType();
}
