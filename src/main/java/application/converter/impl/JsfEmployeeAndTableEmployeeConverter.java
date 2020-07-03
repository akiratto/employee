package application.converter.impl;

import application.converter.JsfEntityAndTableEntityConverter;
import database.entity.TableEmployee;
import java.io.Serializable;
import javax.enterprise.context.Dependent;
import presentation.jsf.entity.JsfEmployee;

/**
 *
 * @author Owner
 */
@Dependent
public class JsfEmployeeAndTableEmployeeConverter
        implements JsfEntityAndTableEntityConverter<JsfEmployee, TableEmployee>, Serializable
{

    @Override
    public TableEmployee toTableEntity(JsfEmployee jsfEmployee) {
        TableEmployee tableEmployee = new TableEmployee();
        tableEmployee.setAddress(jsfEmployee.getAddress());
        tableEmployee.setBirthday(jsfEmployee.getBirthday());
        tableEmployee.setEmployeeCode(jsfEmployee.getEmployeeCode());
        tableEmployee.setEmployee_id(jsfEmployee.getEmployee_id());
        tableEmployee.setGender(jsfEmployee.getGender());
        tableEmployee.setMobilePhone(jsfEmployee.getMobilePhone());
        tableEmployee.setName(jsfEmployee.getName());
        tableEmployee.setPhone(jsfEmployee.getPhone());
        tableEmployee.setRemarks(jsfEmployee.getRemarks());
        tableEmployee.setZipCode(jsfEmployee.getZipCode());
        return tableEmployee;
    }

    @Override
    public JsfEmployee toJsfEntity(TableEmployee tableEntity) {
        JsfEmployee jsfEmployee = new JsfEmployee();
        jsfEmployee.setAddress(tableEntity.getAddress());
        jsfEmployee.setBirthday(tableEntity.getBirthday());
        jsfEmployee.setEmployeeCode(tableEntity.getEmployeeCode());
        jsfEmployee.setEmployee_id(tableEntity.getEmployee_id());
        jsfEmployee.setGender(tableEntity.getGender());
        jsfEmployee.setMobilePhone(tableEntity.getMobilePhone());
        jsfEmployee.setName(tableEntity.getName());
        jsfEmployee.setPhone(tableEntity.getPhone());
        jsfEmployee.setRemarks(tableEntity.getRemarks());
        jsfEmployee.setZipCode(tableEntity.getZipCode());
        return jsfEmployee;
    }
    
}
