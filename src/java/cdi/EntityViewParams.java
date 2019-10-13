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
//    public static class EntityViewParameter
//    {
//        
//        private Field field;
//        
//        public EntityViewParameter(E entity, Field field) { this.field = field; }
//
//        public String getName() { return field.getName(); }
//
//        public String getValue() { return field.get(this); }
//        public void setValue(String value) { this.value = value; }
//    }
//    
//    private Map<String, EntityViewParameter> viewParams;
}
