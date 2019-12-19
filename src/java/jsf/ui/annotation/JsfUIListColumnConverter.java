package jsf.ui.annotation;

import jsf.ui.converter.UIColumnConverter;

/**
 *
 * @author owner
 */
public @interface JsfUIListColumnConverter {
    public Class<? extends UIColumnConverter> converter();
}
