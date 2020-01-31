/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csv.converter;

/**
 *
 * @author Owner
 */
public interface CsvColumnConverter {
    public Object convertToFieldObject(String csvColumnValue);
    public String convertToCsvColumnValue(Object fieldObject);
}
