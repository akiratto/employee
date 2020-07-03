package database.entity;

import database.type.Gender;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * @author owner
 */
@Entity(name = "TEmployee")
@Table(uniqueConstraints=@UniqueConstraint(name = "t_employee_unique_employee_code", columnNames="employeeCode"))
public class TableEmployee
        implements Serializable 
{
    //--JPA
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer employee_id;
    
    @Basic
    private String employeeCode;
    
    @Basic
    private String name;

    @Basic
    @Convert(converter = database.converter.GenderConverter.class)
    private Gender gender;
    
    @Temporal(TemporalType.DATE)
    private Date birthday;

    @Basic
    private String phone;

    @Basic
    private String mobilePhone;

    @Basic
    private String zipCode;

    @Basic
    private String address;

    @Basic
    private String remarks;

    public Integer getEmployee_id() {
        return this.employee_id;
    }

    public void setEmployee_id(Integer employee_id) {
        this.employee_id = employee_id;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobilePhone() {
        return this.mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}