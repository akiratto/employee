package jsf.ui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Owner
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsfUIListPage {
    public String listPageTitle();
    public String listPageName();
    public String detailPageTitle();
    public String detailPageName();
    public String createBatchPageTitle();
    public String createBatchPageName();
//    public String newPageURL();
//    public String searchPageURL();
//    public String clearPageURL();
//    public String createBatchPageURL();
//    public String viewDetailPageURL();
}
