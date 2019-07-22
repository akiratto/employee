package csv.converter.builtin;

import csv.converter.CsvColumnConverter;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author Owner
 */
public class CsvColumnDateConverter implements CsvColumnConverter {

    public CsvColumnDateConverter() {
    }
    
    @Override
    public Object convertToFieldObject(String csvColumnValue) {
        SimpleDateFormat datePattern1 = new SimpleDateFormat("yyyy/MM/dd");
        try {
            return datePattern1.parse(csvColumnValue);
        } catch(ParseException e) {
            return null;
        }
    }

    @Override
    public String convertToCsvColumnValue(Object fieldObject) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
     
}
