package presentation.jsf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import presentation.jsf.converter.UIColumnConverter;

/**
 *
 * @author owner
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsfUIListColumnConverter {
    public Class converter();
}
