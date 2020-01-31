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
public class UIColumnGenderConverterAsJpName implements UIColumnConverter {

    @Override
    public String convertToUIColumnValue(Object fieldObject) {
        Gender gender = (Gender)fieldObject;
        try {
            return gender.jpName();
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
