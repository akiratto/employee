package presentation.jsf.entity;

import database.type.Gender;
import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import presentation.jsf.annotation.JsfConverter;
import presentation.jsf.type.JsfUIColumnType;
import database.type.SearchOrderType;
import presentation.jsf.type.JsfUISearchMethodType;
import presentation.jsf.annotation.JsfUICreateBatchPage;
import presentation.jsf.annotation.JsfUIDetailPage;
import presentation.jsf.annotation.JsfUIId;
import presentation.jsf.annotation.JsfUIInternalId;
import presentation.jsf.annotation.JsfUIListColumn;
import presentation.jsf.annotation.JsfUIListColumnConverter;
import presentation.jsf.annotation.JsfUIListColumnOrder;
import presentation.jsf.annotation.JsfUIListPage;
import presentation.jsf.annotation.JsfUIListPageButtons;
import presentation.jsf.annotation.JsfUIModel;
import presentation.jsf.annotation.JsfUIModelColumn;
import presentation.jsf.annotation.JsfUISearchColumn;
import presentation.jsf.annotation.JsfUISearchColumnConverter;
import presentation.jsf.annotation.JsfUISelectItem;
import presentation.jsf.annotation.JsfUISelectOne;
import presentation.jsf.base.EntityInstance;
import presentation.jsf.converter.impl.UIColumnGenderConverterAsAbbrName;
import presentation.jsf.converter.impl.UIColumnGenderConverterAsJpName;

/**
 *
 * @author owner
 */
@JsfUIModel           ( modelTitle           = "社員情報",     modelName           = "TEmployee" )
@JsfUIListPage        ( listPageTitle        = "社員一覧",     listPageName        = "employeeList" )
@JsfUIDetailPage      ( detailPageTitle      = "社員詳細",     detailPageName      = "employeeDetail" )
@JsfUICreateBatchPage ( createBatchPageTitle = "社員一括登録", createBatchPageName = "employeeBatch" )
@JsfUIListPageButtons ( createButtonTitle = "新規登録", searchButtonTitle = "検索", clearButtonTitle = "クリア", createBatchButtonTitle = "一括登録", deleteAllButtonTitle = "全件削除")
public class JsfEmployee extends EntityInstance<JsfEmployee> implements Serializable {

    @Override
    public Class<JsfEmployee> modelClass() {
        return JsfEmployee.class;
    }
    
    @Override
    public JsfEmployee modelInstance() {
        return this;
    }
    
    //--Jsf
    @JsfUIInternalId
    private Integer employee_id;
    
    //--Bean バリデーション
    @NotNull(message="社員コードの入力は必須です。")
    @Pattern(regexp="\\d{10}", message="社員コードは10桁の数字を入力してください。例)0000000001")
    //--Jsf
    @JsfUIId
    @JsfUIModelColumn(columnTitle = "社員コード")
    @JsfUIListColumn(componentId = "search_employee_code",
                     columnType = JsfUIColumnType.HTML_INPUT_TEXT,
                     labelTitle = "社員コード")
    @JsfUIListColumnOrder(orderSequence = 1, orderType = SearchOrderType.DESCENDING)
    @JsfUISearchColumn(componentId = "search_employee_code", 
                 columnType = JsfUIColumnType.HTML_INPUT_TEXT, 
                 labelTitle = "社員コード")
    private String employeeCode;
    
    //--Bean バリデーション
    @NotNull(message="社員名の入力は必須です。")
    @Size(min=1, max=60, message="${validatedValue == '' ? '社員名の入力は必須です。' : '社員名は' += min +='～'+=max+='文字以内で入力してください。'}")
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

    //--Bean バリデーション
    @NotNull(message="性別の入力は必須です。")
    //Jsf Converter
    @JsfConverter(converter = presentation.jsf.converter.custom.GenderConverter.class)
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
    
    //--Bean バリデーション
    @NotNull(message="生年月日の入力は必須です。")
    //--Jsf
    @JsfUIModelColumn(columnTitle = "生年月日")
    @JsfUIListColumn(componentId = "list_employee_birthday",
                     columnType = JsfUIColumnType.HTML_INPUT_TEXT,
                     labelTitle = "生年月日")
    @JsfUIListColumnConverter(converter = presentation.jsf.converter.impl.UIColumnDateConverter.class)
    @JsfUISearchColumn(componentId = "search_employee_birthday", 
                 columnType = JsfUIColumnType.HTML_INPUT_DATE, 
                 labelTitle = "誕生日")
    private Date birthday;

    //--Bean バリデーション
    @NotNull(message="電話番号の入力は必須です。")
    @Size(min=1, message="電話番号の入力は必須です。")
    @Pattern(regexp = "0\\d{1,4}-\\d{1,4}-\\d{4}", message="電話番号が正しい形式ではありません。")
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

    //--Bean バリデーション
    @NotNull(message="携帯電話番号の入力は必須です。")
    @Size(min=1, message="携帯電話番号の入力は必須です。")
    @Pattern(regexp = "0\\d{1,4}-\\d{1,4}-\\d{4}", message="携帯電話番号が正しい形式ではありません。")
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

    //--Bean バリデーション
    @NotNull(message="郵便番号の入力は必須です。")
    @Size(min=1, message="郵便番号の入力は必須です。")
    @Pattern(regexp = "\\d{3}-\\d{4}", message="郵便番号が正しい形式ではありません。")
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

    //--Bean バリデーション
    @NotNull(message="住所の入力は必須です")
    @Size(min=1, message="住所の入力は必須です")
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

    private String remarks;

    public Integer getEmployee_id() {
        return employee_id;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
