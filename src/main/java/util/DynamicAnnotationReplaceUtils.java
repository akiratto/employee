package util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class DynamicAnnotationReplaceUtils {
    private static final String ANNOTATIONS = "annotations";
    private static final String ANNOTATION_DATA = "annotationData";

    /**
     * 特定のクラスのアノテーションを、別のアノテーションに置き換えます。
     *
     * @since
     *
     * @param targetClazz 置換対象のアノテーションを設定しているクラスです。
     * @param originalOne 置換元のアノテーションです。
     * @param newOne 置換先のアノテーションです。置換元のアノテーションをextednsしたクラスのインスタンスになる事が多いでしょう。
     */
  public static void annotationReplacedBy(Class<?> targetClazz, String originalName, Annotation newOne) {
        try {
            @SuppressWarnings("all")
            Method method = Class.class.getDeclaredMethod(ANNOTATION_DATA, null);
            method.setAccessible(true);

            Object annotationData = method.invoke(targetClazz);

            Field annotations = annotationData.getClass().getDeclaredField(ANNOTATIONS);
            annotations.setAccessible(true);

            @SuppressWarnings("unchecked")
            Map<Class<? extends Annotation>, Annotation> map =
                    (Map<Class<? extends Annotation>, Annotation>) annotations.get(annotationData);

            Annotation original = map.entrySet().stream().filter(e -> {
                return e.getKey().getSimpleName().equals(originalName);
            }).findFirst().get().getValue();

            if (original == null) {
                throw new IllegalArgumentException(
                        String.format("Class(%s) has not %s annotaion.",
                                targetClazz.getCanonicalName(), originalName));
            }

            map.put(original.getClass(), newOne);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}