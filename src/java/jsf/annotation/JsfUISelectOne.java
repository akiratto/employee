package jsf.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author Owner
 */
public @interface JsfUISelectOne {
    public JsfUISelectItem[] selectItems();
}
