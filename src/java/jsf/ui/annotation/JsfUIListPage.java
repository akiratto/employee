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
    public String messageDeleteEntityNotFound()     default "'削除する' += modelTitle += 'が見つかりません。'";
    public String messageDeleteEntityCompleted()    default "modelTitle += '(ID:' += entityId += ')を削除しました。'";
    public String messageDeleteAllEntityCompleted() default "deleteCount += '件の' += modelTitle += 'を削除しました。'";
}
