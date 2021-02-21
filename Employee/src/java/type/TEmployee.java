/**
 * This file was generated by the Jeddict
 */
package type;

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
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import util.converter.GenderConverter;

/**
 * @author owner
 */
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "t_employee_unique_employee_code", columnNames = {"EMPLOYEECODE"})})
public class TEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employee_id;

    @Basic
    private String employeeCode;

    @Basic
    private String name;

    @Convert(converter = GenderConverter.class)
    @Basic
    private Gender gender;

    @Basic
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

    @Transient
    private String departmentCode;

    public Long getEmployee_id() {
        return this.employee_id;
    }

    public void setEmployee_id(Long employee_id) {
        this.employee_id = employee_id;
    }

    public String getEmployeeCode() {
        return this.employeeCode;
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
        return this.birthday;
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

    public String getDepartmentCode() {
        return this.departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

}