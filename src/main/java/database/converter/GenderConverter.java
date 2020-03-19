package database.converter;

import database.type.Gender;
import javax.persistence.AttributeConverter;

/**
 *
 * @author Owner
 */
@javax.persistence.Converter
public class GenderConverter implements AttributeConverter<Gender, String> {

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
