package jsf.ui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jsf.type.JsfUIColumnType;
import jsf.type.JsfUISearchMethodType;
import static jsf.type.JsfUISearchMethodType.SEARCH_METHOD_EQUAL;

/**
 *
 * @author Owner
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsfUISearchColumn {
    public boolean searchable() default true;
    public String componentId();
    public String labelTitle();
    public JsfUIColumnType columnType();
    public JsfUISearchMethodType searchMethodType() default SEARCH_METHOD_EQUAL;
}
