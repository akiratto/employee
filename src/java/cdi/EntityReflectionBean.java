/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cdi;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import jsf.annotation.JsfConverter;
import static util.StringFunctions.toSnakeCase;

/**
 *
 * @author Owner
 */
@Named
@ApplicationScoped
public class EntityReflectionBean implements Serializable {
    
    public <E extends Serializable> List<EntityField> getEntityFields(E entity)
    {
        return Arrays.asList( entity.getClass().getDeclaredFields() )
                .stream()
                .filter(field -> !field.getName().startsWith("_"))
                .filter(field -> !field.getName().equals("serialVersionUID"))
                .map(field -> new EntityField(entity, field))
                .collect(Collectors.toList());
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
        public String getJsfConverterId() {
            JsfConverter jsfConverter = field.getAnnotation(JsfConverter.class);
            return jsfConverter != null ? jsfConverter.converterId() : "idConverter";
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
