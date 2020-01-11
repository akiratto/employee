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
public @interface JsfUIListPageButtons {
    public String createButtonTitle() default "新規登録";
    public String searchButtonTitle() default "検索";
    public String clearButtonTitle() default "クリア";
    public String createBatchButtonTitle() default "一括登録";
    public String deleteAllButtonTitle() default "全件削除";
    public String deleteAllButtonConfirmMessage() default "modelTitle += 'をすべて削除します。\n本当によろしいです？'";
}
