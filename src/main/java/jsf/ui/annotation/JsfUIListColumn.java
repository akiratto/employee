package jsf.ui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jsf.type.JsfUIColumnType;
import jsf.type.JsfUIOrderType;
import jsf.ui.converter.UIColumnConverter;

/**
 *
 * @author Owner
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsfUIListColumn {
    public boolean visible() default true;
    public String componentId();
    public String labelTitle();
    public JsfUIColumnType columnType();
}
