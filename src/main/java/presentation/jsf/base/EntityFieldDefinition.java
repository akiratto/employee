package presentation.jsf.base;

import java.io.Serializable;
import java.lang.reflect.Field;
import javax.faces.convert.Converter;
import presentation.jsf.annotation.JsfConverter;
import presentation.jsf.converter.custom.IdConverter;
import presentation.jsf.annotation.JsfUIId;
import presentation.jsf.annotation.JsfUIInternalId;
import presentation.jsf.annotation.JsfUIListColumn;
import presentation.jsf.annotation.JsfUIListColumnConverter;
import presentation.jsf.annotation.JsfUISearchColumn;
import presentation.jsf.annotation.JsfUISelectOne;
import presentation.jsf.converter.UIColumnConverter;
import static util.StringFunctions.toSnakeCase;

/**
 *
 * @author Owner
 */
public class EntityFieldDefinition<E extends Serializable> {
        protected final Field entityField;
        
        public EntityFieldDefinition(Field entityField)
        {
            this.entityField = entityField;
        }
        
        public String getName() { return entityField.getName(); }
        public String getSnakeCaseName() { return toSnakeCase(entityField.getName()); }

        public JsfUIId           getJsfUIId()               { return entityField.getAnnotation(JsfUIId.class); }
        public JsfUIInternalId   getJsfUIInternalId()      { return entityField.getAnnotation(JsfUIInternalId.class); }
        public JsfUISearchColumn getJsfUISearchColumn()    { return entityField.getAnnotation(JsfUISearchColumn.class); }
        public JsfUISelectOne    getJsfUISearchSelectOne() { return entityField.getAnnotation(JsfUISelectOne.class); }
        public JsfUIListColumn   getJsfUIListColumn()       { return entityField.getAnnotation(JsfUIListColumn.class); }

        
        public boolean hasJsfUIId()               { return entityField.getAnnotation(JsfUIId.class)!=null; }
        public boolean hasJsfUIInternalId()      { return entityField.getAnnotation(JsfUIInternalId.class)!=null; }
        public boolean hasJsfUISearchColumn()    { return entityField.getAnnotation(JsfUISearchColumn.class)!=null; }
        public boolean hasJsfUISearchSelectOne() { return entityField.getAnnotation(JsfUISelectOne.class)!=null; }
        public boolean hasJsfUIListColumn()       { return entityField.getAnnotation(JsfUIListColumn.class)!=null; }
        public boolean hasJsfUIListColumnConverter() { 
            JsfUIListColumnConverter jsfUIListColumnConverter = entityField.getAnnotation(JsfUIListColumnConverter.class);
            return jsfUIListColumnConverter != null;
        }
        
        public Converter getJsfConverter() 
                throws InstantiationException, IllegalAccessException 
        {
            JsfConverter jsfConverter = entityField.getAnnotation(JsfConverter.class);
            return jsfConverter != null 
                    ? jsfConverter.converter().newInstance() 
                    : IdConverter.class.newInstance();
        }
        
        public UIColumnConverter getJsfUIListColumnConverter() throws InstantiationException, IllegalAccessException { 
            
            JsfUIListColumnConverter jsfUIListColumnConverter = entityField.getAnnotation(JsfUIListColumnConverter.class);
            return jsfUIListColumnConverter != null
                        ? (UIColumnConverter)jsfUIListColumnConverter.converter().newInstance()
                        : null;
        }
}
