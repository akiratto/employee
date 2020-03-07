/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misc;

import jsf.ui.annotation.JsfUIListColumnOrder;

/**
 *
 * @author Owner
 */
public class JsfUIColumnSetting {
    private String fieldName;
    private JsfUIListColumnOrder jsfUIListColumnOrder;

    public JsfUIColumnSetting(String fieldName, JsfUIListColumnOrder jsfUIListColumnOrder) {
        this.fieldName = fieldName;
        this.jsfUIListColumnOrder = jsfUIListColumnOrder;
    }

    public String getFieldName() {
        return fieldName;
    }

    public JsfUIListColumnOrder getJsfUIListColumnOrder() {
        return jsfUIListColumnOrder;
    }
}
