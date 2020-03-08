package cdi.base;

import java.io.Serializable;
import java.lang.reflect.Field;
import javax.faces.convert.Converter;
import jsf.annotation.JsfConverter;
import jsf.converter.IdConverter;
import jsf.ui.annotation.JsfUIId;
import jsf.ui.annotation.JsfUIInternalId;
import jsf.ui.annotation.JsfUIListColumn;
import jsf.ui.annotation.JsfUIListColumnConverter;
import jsf.ui.annotation.JsfUISearchColumn;
import jsf.ui.annotation.JsfUISelectOne;
import jsf.ui.converter.UIColumnConverter;
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
