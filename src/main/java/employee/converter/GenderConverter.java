package employee.converter;

import csv.converter.CsvColumnConverter;
import entity.type.Gender;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.persistence.AttributeConverter;

/**
 *
 * @author Owner
 */
@javax.persistence.Converter
@FacesConverter(value = "genderConverter", forClass = String.class)
public class GenderConverter implements CsvColumnConverter, Converter, AttributeConverter<Gender, String> {

    // CsvColumnConverter ------------------------------------------------------
    @Override
    public Object convertToFieldObject(String csvColumnValue) {
        try {
            return Gender.valueForAllNames(csvColumnValue);
        } catch(IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String convertToCsvColumnValue(Object fieldObject) {
        Gender gender = (Gender)fieldObject;
        try {
            return gender.jpName();
        } catch(IllegalArgumentException e) {
            return null;
        }
    }

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

    //AttributeConverter -------------------------------------------------------
    @Override
    public String convertToDatabaseColumn(Gender attribute) {
        return attribute.name();
    }

    @Override
    public Gender convertToEntityAttribute(String dbData) {
        return (dbData == null || dbData.isEmpty() ? null : Gender.valueOf(dbData));
    }
    
    
}
