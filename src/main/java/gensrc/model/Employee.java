package gensrc.model;

import csv.annotation.CsvColumn;
import csv.annotation.CsvColumnFormula;
import csv.annotation.CsvConverter;
import csv.converter.builtin.CsvColumnDateConverter;
import employee.converter.GenderConverter;
import entity.type.Gender;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import jsf.annotation.JsfConverter;
import jsf.type.JsfUIColumnType;
import jsf.type.JsfUIOrderType;
import jsf.type.JsfUISearchMethodType;
import jsf.ui.annotation.JsfUICreateBatchPage;
import jsf.ui.annotation.JsfUIDetailPage;
import jsf.ui.annotation.JsfUIId;
import jsf.ui.annotation.JsfUIInternalId;
import jsf.ui.annotation.JsfUIListColumn;
import jsf.ui.annotation.JsfUIListColumnConverter;
import jsf.ui.annotation.JsfUIListColumnOrder;
import jsf.ui.annotation.JsfUIListPage;
import jsf.ui.annotation.JsfUIListPageButtons;
import jsf.ui.annotation.JsfUIModel;
import jsf.ui.annotation.JsfUIModelColumn;
import jsf.ui.annotation.JsfUISearchColumn;
import jsf.ui.annotation.JsfUISearchColumnConverter;
import jsf.ui.annotation.JsfUISelectItem;
import jsf.ui.annotation.JsfUISelectOne;
import jsf.ui.converter.impl.UIColumnGenderConverterAsAbbrName;
import jsf.ui.converter.impl.UIColumnGenderConverterAsJpName;

/**
 *
 * @author owner
 */
@Entity(name = "Employee")
@Table(uniqueConstraints=@UniqueConstraint(name = "t_employee_unique_employee_code", columnNames="employeeCode"))
@JsfUIModel           ( modelTitle           = "社員情報",     modelName           = "TEmployee" )
@JsfUIListPage        ( listPageTitle        = "社員一覧",     listPageName        = "employeeList" )
@JsfUIDetailPage      ( detailPageTitle      = "社員詳細",     detailPageName      = "employeeDetail" )
@JsfUICreateBatchPage ( createBatchPageTitle = "社員一括登録", createBatchPageName = "employeeBatch" )
@JsfUIListPageButtons ( createButtonTitle = "新規登録", searchButtonTitle = "検索", clearButtonTitle = "クリア", createBatchButtonTitle = "一括登録", deleteAllButtonTitle = "全件削除")
public class Employee implements Serializable {
    //--JPA
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //--Jsf
    @JsfUIInternalId
    private Integer employee_id;
    
    //--JPA
    @Basic
    //--Bean バリデーション
    @NotNull(message="社員コードの入力は必須です。")
    @Pattern(regexp="\\d{10}", message="社員コードは10桁の数字を入力してください。例)0000000001")
    @CsvColumn(field="社員コード")
    //--Jsf
    @JsfUIId
    @JsfUIModelColumn(columnTitle = "社員コード")
    @JsfUIListColumn(componentId = "search_employee_code",
                     columnType = JsfUIColumnType.HTML_INPUT_TEXT,
                     labelTitle = "社員コード")
    @JsfUIListColumnOrder(orderSequence = 1, orderType = JsfUIOrderType.DESCENDING)
    @JsfUISearchColumn(componentId = "search_employee_code", 
                 columnType = JsfUIColumnType.HTML_INPUT_TEXT, 
                 labelTitle = "社員コード")
    private String employeeCode;
    
    //--JPA
    @Basic
    //--Bean バリデーション
    @NotNull(message="社員名の入力は必須です。")
    @Size(min=1, max=60, message="${validatedValue == '' ? '社員名の入力は必須です。' : '社員名は' += min +='～'+=max+='文字以内で入力してください。'}")
    //--CSV パーサ
    @CsvColumn(field="氏名")
    //--Jsf
    @JsfUIModelColumn(columnTitle = "社員名")
    @JsfUIListColumn(componentId = "search_employee_name",
                     columnType = JsfUIColumnType.HTML_INPUT_TEXT,
                     labelTitle = "社員名")
    @JsfUISearchColumn(componentId = "search_employee_name", 
                 columnType = JsfUIColumnType.HTML_INPUT_TEXT, 
                 labelTitle = "社員名",
                 searchMethodType = JsfUISearchMethodType.SEARCH_METHOD_INCLUDE)
    private String name;

    //--JPA
    @Basic
    @Convert(converter = GenderConverter.class)
    //--Bean バリデーション
    @NotNull(message="性別の入力は必須です。")
    //--CSV パーサ
    @CsvColumn(field="性別")
    @CsvConverter(converter = GenderConverter.class)
    //Jsf Converter
    @JsfConverter(converter = GenderConverter.class)
    //Jsf
    @JsfUIModelColumn(columnTitle = "性別")
    @JsfUIListColumn(componentId = "list_employee_gender",
                     columnType = JsfUIColumnType.HTML_INPUT_TEXT,
                     labelTitle = "性別")
    @JsfUIListColumnConverter(converter = UIColumnGenderConverterAsJpName.class)
    @JsfUISearchColumn(componentId = "search_employee_gender", 
             columnType = JsfUIColumnType.UI_SELECT_ONE, 
             labelTitle = "性別"
    )
    @JsfUISearchColumnConverter(converter = UIColumnGenderConverterAsAbbrName.class)
    @JsfUISelectOne(selectItems = {
                 @JsfUISelectItem(itemLabel = "", itemValue = ""),
                 @JsfUISelectItem(itemLabel = "男", itemValue = "M"),
                 @JsfUISelectItem(itemLabel = "女", itemValue = "F"),
                 @JsfUISelectItem(itemLabel = "その他", itemValue = "O")
    })
    private Gender gender;
    
    //--JPA
    @Temporal(TemporalType.DATE)
    //--Bean バリデーション
    @NotNull(message="生年月日の入力は必須です。")
    //--CSV パーサ
    @CsvColumn(field="生年月日")
    @CsvConverter(converter = CsvColumnDateConverter.class)
    //--Jsf
    @JsfUIModelColumn(columnTitle = "生年月日")
    @JsfUIListColumn(componentId = "list_employee_birthday",
                     columnType = JsfUIColumnType.HTML_INPUT_TEXT,
                     labelTitle = "生年月日")
    @JsfUIListColumnConverter(converter = jsf.ui.converter.impl.UIColumnDateConverter.class)
    @JsfUISearchColumn(componentId = "search_employee_birthday", 
                 columnType = JsfUIColumnType.HTML_INPUT_DATE, 
                 labelTitle = "誕生日")
    private Date birthday;

    //--JPA
    @Basic
    //--Bean バリデーション
    @NotNull(message="電話番号の入力は必須です。")
    @Size(min=1, message="電話番号の入力は必須です。")
    @Pattern(regexp = "0\\d{1,4}-\\d{1,4}-\\d{4}", message="電話番号が正しい形式ではありません。")
    //--CSV パーサ
    @CsvColumn(field="電話番号")
    //--Jsf
    @JsfUIModelColumn(columnTitle = "電話番号")
    @JsfUIListColumn(componentId = "list_employee_phone",
                     columnType = JsfUIColumnType.HTML_INPUT_TEXT,
                     labelTitle = "電話番号")
    @JsfUISearchColumn(componentId = "search_employee_phone", 
             columnType = JsfUIColumnType.HTML_INPUT_TEXT, 
             labelTitle = "電話番号",
             searchMethodType = JsfUISearchMethodType.SEARCH_METHOD_INCLUDE)
    private String phone;

    //--JPA
    @Basic
    //--Bean バリデーション
    @NotNull(message="携帯電話番号の入力は必須です。")
    @Size(min=1, message="携帯電話番号の入力は必須です。")
    @Pattern(regexp = "0\\d{1,4}-\\d{1,4}-\\d{4}", message="携帯電話番号が正しい形式ではありません。")
    //--CSV パーサ
    @CsvColumn(field="携帯電話")
    //--Jsf
    @JsfUIModelColumn(columnTitle = "携帯電話番号")
    @JsfUIListColumn(componentId = "list_employee_mobile_phone",
                     columnType = JsfUIColumnType.HTML_INPUT_TEXT,
                     labelTitle = "携帯電話番号")
    @JsfUISearchColumn(componentId = "search_employee_mobile_phone", 
             columnType = JsfUIColumnType.HTML_INPUT_TEXT, 
             labelTitle = "携帯電話番号",
             searchMethodType = JsfUISearchMethodType.SEARCH_METHOD_INCLUDE)
    private String mobilePhone;

    //--JPA
    @Basic
    //--Bean バリデーション
    @NotNull(message="郵便番号の入力は必須です。")
    @Size(min=1, message="郵便番号の入力は必須です。")
    @Pattern(regexp = "\\d{3}-\\d{4}", message="郵便番号が正しい形式ではありません。")
    //--CSV パーサ
    @CsvColumn(field="郵便番号")
    //--Jsf
    @JsfUIModelColumn(columnTitle = "郵便番号")
    @JsfUIListColumn(componentId = "list_employee_zip_code",
                     columnType = JsfUIColumnType.HTML_INPUT_TEXT,
                     labelTitle = "郵便番号")
    @JsfUISearchColumn(componentId = "search_employee_zip_code", 
             columnType = JsfUIColumnType.HTML_INPUT_TEXT, 
             labelTitle = "郵便番号",
             searchMethodType = JsfUISearchMethodType.SEARCH_METHOD_INCLUDE)
    private String zipCode;

    //--JPA
    @Basic
    //--Bean バリデーション
    @NotNull(message="住所の入力は必須です")
    @Size(min=1, message="住所の入力は必須です")
    //--CSV パーサ
    @CsvColumnFormula(formula="住所1 += 住所2 += 住所3 += 住所4 += 住所5")
    //--Jsf
    @JsfUIModelColumn(columnTitle = "住所")
    @JsfUIListColumn(componentId = "list_employee_address",
                     columnType = JsfUIColumnType.HTML_INPUT_TEXT,
                     labelTitle = "住所")
    @JsfUISearchColumn(componentId = "search_employee_address", 
             columnType = JsfUIColumnType.HTML_INPUT_TEXT, 
             labelTitle = "住所",
             searchMethodType = JsfUISearchMethodType.SEARCH_METHOD_INCLUDE
    )
    private String address;

    //--JPA
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
