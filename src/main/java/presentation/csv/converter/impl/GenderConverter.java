package presentation.csv.converter.impl;

import database.type.Gender;
import presentation.csv.converter.CsvColumnConverter;

/**
 *
 * @author Owner
 */
public class GenderConverter implements CsvColumnConverter {

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
}
