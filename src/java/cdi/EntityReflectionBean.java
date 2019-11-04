package cdi;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.convert.Converter;
import javax.inject.Named;
import jsf.annotation.JsfConverter;
import jsf.ui.annotation.JsfUIColumn;
import jsf.ui.annotation.JsfUISelectOne;
import jsf.converter.IdConverter;
import jsf.ui.UIButtonsInList;
import jsf.ui.annotation.JsfUIButtonsInList;
import static util.StringFunctions.toSnakeCase;

/**
 *
 * @author Owner
 */
@Named
@ApplicationScoped
public class EntityReflectionBean implements Serializable {
    
    public UIButtonsInList getUIButtonsInList(UIButtonsInList uiButtonsInList) { return uiButtonsInList; }
    
    public <E extends Serializable> Entity<E> getEntity(E entity)
    {
        return new Entity<>(entity);
    }
    
    public <E extends Serializable> List<EntityField<E>> getEntityFields(E entity)
    {
        return Arrays.asList( entity.getClass().getDeclaredFields() )
                .stream()
                .filter(field -> !field.getName().startsWith("_"))
                .filter(field -> !field.getName().equals("serialVersionUID"))
                .map(field -> new EntityField<>(entity, field))
                .collect(Collectors.toList());
    }
    
    public static class Entity<E extends Serializable>
    {
        private final E entity;
        
        public Entity(E entity)
        {
            this.entity = entity;
        }
        
        public String getName() { return entity.getClass().getSimpleName(); }
        public String getSnakeCaseName() { return toSnakeCase(entity.getClass().getSimpleName()); }
    }
    
    public static class EntityField<E extends Serializable>
    {
        private final E entity;
        private final Field field;
        
        public EntityField(E entity, Field field)
        {
            this.entity = entity;
            this.field = field;
        }

        public String getName() { return field.getName(); }
        public String getSnakeCaseName() { return toSnakeCase(field.getName()); }
        public Converter getJsfConverter() 
                throws InstantiationException, IllegalAccessException 
        {
            JsfConverter jsfConverter = field.getAnnotation(JsfConverter.class);
            return jsfConverter != null 
                    ? jsfConverter.converter().newInstance() 
                    : IdConverter.class.newInstance();
        }
        
        public JsfUIColumn getJsfUIColumn()
        {
            return field.getAnnotation(JsfUIColumn.class);
        }
        
        public JsfUISelectOne getJsfUISelectOne()
        {
            return field.getAnnotation(JsfUISelectOne.class);
        }

        public Object getValue()
        {
            try {
                boolean tmpAccessible = field.isAccessible();
                field.setAccessible(true);
                Object value = field.get(entity);
                field.setAccessible(tmpAccessible);
                return value;
            } catch(IllegalAccessException | IllegalArgumentException ex) {
                return null;
            }
        }
        
        public void setValue(Object value) 
        { 
            try {
                boolean tmpAccessible = field.isAccessible();
                field.setAccessible(true);
                field.set(entity,value); 
                field.setAccessible(tmpAccessible);
            } catch(IllegalAccessException | IllegalArgumentException ex) {
                
            }
        }
    }

}
