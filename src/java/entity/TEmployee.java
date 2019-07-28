/**
 * This file was generated by the Jeddict
 */
package entity;

import csv.annotation.CsvColumn;
import csv.annotation.CsvConverter;
import csv.converter.builtin.CsvColumnDateConverter;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author owner
 */
@Entity(name = "TEmployee")
public class TEmployee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer employee_id;
    
    @Basic
    //--Bean バリデーション
    @NotNull(message="社員名の入力は必須です。")
    @Size(min=1, max=60, message="${validatedValue == '' ? '社員名の入力は必須です。' : '社員名は' += min +='～'+=max+='文字以内で入力してください。'}")
    //--CSV パーサ
    @CsvColumn(field="氏名")
    private String name;

    @Basic
    //--Bean バリデーション
    @NotNull(message="性別の入力は必須です。")
    @Pattern(regexp = "[MFO]", message="性別は男性(M),女性(F),その他(O)のいずれかを入力してください:${validatedValue}")
    //--CSV パーサ
    @CsvColumn(field="性別")
    private String gender;
    
    @Temporal(TemporalType.DATE)
    //--Bean バリデーション
    @NotNull(message="生年月日の入力は必須です。")
    //--CSV パーサ
    @CsvColumn(field="生年月日")
    @CsvConverter(converter = CsvColumnDateConverter.class)
    private Date birthday;

    @Basic
    //--Bean バリデーション
    @NotNull(message="電話番号の入力は必須です。")
    @Size(min=1, message="電話番号の入力は必須です。")
    @Pattern(regexp = "0\\d{1,4}-\\d{1,4}-\\d{4}", message="電話番号が正しい形式ではありません。")
    //--CSV パーサ
    @CsvColumn(field="電話番号")
    private String phone;

    @Basic
    //--Bean バリデーション
    @NotNull(message="携帯電話番号の入力は必須です。")
    @Size(min=1, message="携帯電話番号の入力は必須です。")
    @Pattern(regexp = "0\\d{1,4}-\\d{1,4}-\\d{4}", message="携帯電話番号が正しい形式ではありません。")
    //--CSV パーサ
    @CsvColumn(field="携帯電話")
    private String mobilePhone;

    @Basic
    //--Bean バリデーション
    @NotNull(message="郵便番号の入力は必須です。")
    @Size(min=1, message="郵便番号の入力は必須です。")
    @Pattern(regexp = "\\d{3}-\\d{4}", message="郵便番号が正しい形式ではありません。")
    //--CSV パーサ
    @CsvColumn(field="郵便番号")
    private String zipCode;

    @Basic
    //--Bean バリデーション
    @NotNull(message="住所の入力は必須です")
    @Size(min=1, message="住所の入力は必須です")
    //--CSV パーサ
    @CsvColumn(field="住所")
    private String address;

    @Basic
    private String remarks;

    public Integer getEmployee_id() {
        return this.employee_id;
    }

    public void setEmployee_id(Integer employee_id) {
        this.employee_id = employee_id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
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