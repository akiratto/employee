package jsf.ui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jsf.type.JsfUIOrderType;

/**
 *
 * @author Owner
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsfUIListColumnOrder {
    JsfUIOrderType orderType() default JsfUIOrderType.NONE;
    int orderSequence() default 99;
}
