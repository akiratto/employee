/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.ui.converter.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import jsf.ui.converter.UIColumnConverter;

/**
 *
 * @author owner
 */
public class UIColumnDateConverter implements UIColumnConverter {

    @Override
    public Object convertToFieldObject(String uiColumnValue) {
        SimpleDateFormat datePattern1 = new SimpleDateFormat("yyyy/MM/dd");
        try {
            return datePattern1.parse(uiColumnValue);
        } catch(ParseException e) {
            return null;
        }
    }

    @Override
    public String convertToUIColumnValue(Object fieldObject) {
        Date dateValue = (Date)fieldObject;
        SimpleDateFormat datePattern1 = new SimpleDateFormat("yyyy/MM/dd");
        return datePattern1.format(dateValue);
    }
}
