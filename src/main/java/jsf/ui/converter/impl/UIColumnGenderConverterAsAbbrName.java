package jsf.ui.converter.impl;

import entity.database.type.Gender;
import jsf.ui.converter.UIColumnConverter;

/**
 *
 * @author owner
 */
public class UIColumnGenderConverterAsAbbrName implements UIColumnConverter {

    @Override
    public String convertToUIColumnValue(Object fieldObject) {
        Gender gender = (Gender)fieldObject;
        try {
            return gender.name();
        } catch(IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public Object convertToFieldObject(String uiColumnValue) {
        try {
            return Gender.valueForAllNames(uiColumnValue);
        } catch(IllegalArgumentException e) {
            return null;
        }
    }
    
}
