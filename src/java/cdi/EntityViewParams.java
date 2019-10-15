/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cdi;

import java.io.Serializable;
import java.lang.reflect.Field;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Owner
 */
@Named
@ViewScoped
public class EntityViewParams<E extends Serializable> {
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
                throws IllegalArgumentException, IllegalAccessException 
        {
            return field.get(entity);
        }
        
        public void setValue(Object value) 
                throws IllegalArgumentException, IllegalAccessException 
        { 
            field.set(entity,value); 
        }
    }
//    
//    private Map<String, EntityViewParameter> viewParams;
}
