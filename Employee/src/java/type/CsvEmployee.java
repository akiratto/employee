package type;

import java.util.Date;
import util.converter.GenderConverter;
import util.csv.annotation.CsvColumn;
import util.csv.annotation.CsvColumnFormula;
import util.csv.annotation.CsvConverter;
import util.csv.converter.builtin.CsvColumnDateConverter;

/**
 *
 * @author owner
 */
public class CsvEmployee {

    @CsvColumn(field="社員コード")
    private String employeeCode;
    
    private String departmentCode;
    
    @CsvColumn(field="氏名")
    private String name;

    //--CSV パーサ
    @CsvColumn(field="性別")
    @CsvConverter(converter = GenderConverter.class)
    private Gender gender;
    
    @CsvColumn(field="生年月日")
    @CsvConverter(converter = CsvColumnDateConverter.class)
    private Date birthday;

    @CsvColumn(field="電話番号")
    private String phone;

    @CsvColumn(field="携帯電話")
    private String mobilePhone;

    @CsvColumn(field="郵便番号")
    private String zipCode;

    @CsvColumnFormula(formula="住所1 += 住所2 += 住所3 += 住所4 += 住所5")
    private String address;

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
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
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
 
    
}
