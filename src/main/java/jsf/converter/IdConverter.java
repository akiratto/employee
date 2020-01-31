/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Owner
 */
@FacesConverter(value = "idConverter", forClass = Object.class)
public class IdConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if(value==null) return null;
        return value;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(value==null) return null;
        if(value instanceof String == false) return null;
        return (String) value;
    }
    
}
