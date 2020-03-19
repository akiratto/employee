package presentation.jsf.converter.custom;

import database.type.Gender;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Owner
 */
@FacesConverter(value = "genderConverter", forClass = String.class)
public class GenderConverter implements Converter {

    // Jsf Converter ------------------------------------------------------
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if(value==null || value.isEmpty()) return null;
        
        Object valueAsObject = Gender.valueForAllNames(value);
        if(valueAsObject == null) {
            throw new ConverterException(new FacesMessage(null, "'" + value + "'は性別に変換できませんでした。"));
        }
        return valueAsObject;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(value==null) return null;
        if(value instanceof String && ((String)value).isEmpty()) return null;
        
        //コンボボックスの場合
        String valueAsString = null;
        if(component instanceof javax.faces.component.html.HtmlSelectOneMenu) {
            valueAsString = Gender.valueOf((String)value).name();
        } else if(component instanceof javax.faces.component.UIViewParameter) {
            valueAsString = ((Gender)value).name();
        }
        if(valueAsString == null) {
            throw new ConverterException("'" + value.toString() + "'はM,F,Oのいずれでもありません。");
        }
        return valueAsString;
    }
    
}
