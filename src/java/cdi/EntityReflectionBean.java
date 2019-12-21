package cdi;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.convert.Converter;
import javax.inject.Named;
import jsf.annotation.JsfConverter;
import jsf.ui.annotation.JsfUISelectOne;
import jsf.converter.IdConverter;
import jsf.ui.annotation.JsfUIListColumn;
import static util.StringFunctions.toSnakeCase;
import jsf.ui.annotation.JsfUISearchColumn;
import jsf.ui.annotation.JsfUIId;
import jsf.ui.annotation.JsfUIInternalId;
import jsf.ui.annotation.JsfUIListColumnConverter;

/**
 *
 * @author Owner
 */
@Named
@ApplicationScoped
public class EntityReflectionBean implements Serializable {

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
    
    public <E extends Serializable> EntityField<E> findEntityField(E entity, String name)
    {
        System.out.println("findEntityField name=" + name);
        Field field = null;
        try {
            
            field = entity.getClass().getDeclaredField(name);
        } catch (NoSuchFieldException ex) { 
            
        } catch (SecurityException ex) {
            
        }
        return field == null ? null : new EntityField<>(entity, field);
    }
    
    public <E extends Serializable> List<EntityField<E>> getEntityFieldsWithJsfUIListColumn(E entity)
    {
        return Arrays.asList( entity.getClass().getDeclaredFields() )
                        .stream()
                        .filter(field -> !field.getName().startsWith("_"))
                        .filter(field -> !field.getName().equals("serialVersionUID"))
                        .filter(field -> field.getAnnotation(JsfUIListColumn.class)!=null)
                        .map(field -> new EntityField<>(entity, field))
                        .collect(Collectors.toList());
    }
    
    public <E extends Serializable> EntityField<E> getJsfUIIdField(E entity)
    {
        return Arrays.asList( entity.getClass().getDeclaredFields() )
                            .stream()
                            .filter(field -> field.getAnnotation(JsfUIId.class)!=null)
                            .map(field -> new EntityField<>(entity, field))
                            .findFirst()
                            .orElse(null);
    }
    
    public <E extends Serializable> EntityField<E> getJsfUIInternalIdField(E entity)
    {
        return Arrays.asList( entity.getClass().getDeclaredFields() )
                            .stream()
                            .filter(field -> field.getAnnotation(JsfUIInternalId.class)!=null)
                            .map(field -> new EntityField<>(entity, field))
                            .findFirst()
                            .orElse(null);
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

        public JsfUIId           getJsfUIId()               { return field.getAnnotation(JsfUIId.class); }
        public JsfUIInternalId   getJsfUIInternalId()      { return field.getAnnotation(JsfUIInternalId.class); }
        public JsfUISearchColumn getJsfUISearchColumn()    { return field.getAnnotation(JsfUISearchColumn.class); }
        public JsfUISelectOne    getJsfUISearchSelectOne() { return field.getAnnotation(JsfUISelectOne.class); }
        public JsfUIListColumn   getJsfUIListColumn()       { return field.getAnnotation(JsfUIListColumn.class); }
        public JsfUIListColumnConverter getJsfUIListColumnConverter() { return field.getAnnotation(JsfUIListColumnConverter.class); }
        
        public boolean hasJsfUIId()               { return field.getAnnotation(JsfUIId.class)!=null; }
        public boolean hasJsfUIInternalId()      { return field.getAnnotation(JsfUIInternalId.class)!=null; }
        public boolean hasJsfUISearchColumn()    { return field.getAnnotation(JsfUISearchColumn.class)!=null; }
        public boolean hasJsfUISearchSelectOne() { return field.getAnnotation(JsfUISelectOne.class)!=null; }
        public boolean hasJsfUIListColumn()       { return field.getAnnotation(JsfUIListColumn.class)!=null; }
        public boolean hasJsfUIListColumnConverter() { return field.getAnnotation(JsfUIListColumnConverter.class)!=null; }
        
        public Converter getJsfConverter() 
                throws InstantiationException, IllegalAccessException 
        {
            JsfConverter jsfConverter = field.getAnnotation(JsfConverter.class);
            return jsfConverter != null 
                    ? jsfConverter.converter().newInstance() 
                    : IdConverter.class.newInstance();
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
