/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cdi;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Owner
 */
@Named
@ViewScoped
public class EntityViewParams implements Serializable {
    
    public <E extends Serializable> Map<String, EntityViewParameter> createViewParam(E entity)
    {
        Map<String, EntityViewParameter> viewParams = new HashMap<>();
        for(Field field : entity.getClass().getDeclaredFields()) {
            viewParams.put(field.getName(), new EntityViewParameter(entity, field));
        }
        return viewParams;
    } 
    
    public static class EntityViewParameter<E extends Serializable>
    {
        private final E entity;
        private final Field field;
        
        public EntityViewParameter(E entity, Field field)
        {
            this.entity = entity;
            this.field = field;
        }

        public String getName() { return field.getName(); }

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
