/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.ui.converter.impl;

import entity.type.Gender;
import jsf.ui.converter.UIColumnConverter;

/**
 *
 * @author owner
 */
public class GenderConverter implements UIColumnConverter {

    @Override
    public String convertToFieldObject(Object fieldObject) {
        Gender gender = (Gender)fieldObject;
        try {
            return gender.jpName();
        } catch(IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public Object convertToUIColumnValue(String uiColumnValue) {
        try {
            return Gender.valueForAllNames(uiColumnValue);
        } catch(IllegalArgumentException e) {
            return null;
        }
    }
    
}
