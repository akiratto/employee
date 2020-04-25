package presentation.jsf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import database.type.SearchOrderType;

/**
 *
 * @author Owner
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsfUIListColumnOrder {
    SearchOrderType orderType() default SearchOrderType.NONE;
    int orderSequence() default 99;
}
