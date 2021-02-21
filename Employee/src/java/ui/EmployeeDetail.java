package ui;

import type.TEmployee;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import type.Gender;

@Named
@ViewScoped
public class EmployeeDetail implements Serializable {
    @PersistenceContext
    protected EntityManager em;
    
    //<editor-fold defaultstate="collapsed" desc="JSFページでURLのクエリパラメータをバインドするプロパティ">
    public enum Mode {
        Undefined,
        New,
        Read,
        Edit
    }
    
    public static class QueryParameter {
        private Long employeeId;
        private Mode mode = Mode.Undefined;
        
        public Long getEmployeeId() { return employeeId; }
        public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
        public Mode getMode() { return mode; }
        public void setMode(Mode mode) { this.mode = mode; }
    }
    private QueryParameter queryParameter = new QueryParameter();
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="JSFページで画面項目をバインドするプロパティ">
    private Long employee_id;
    
    //--Bean バリデーション
    @NotNull(message="社員コードの入力は必須です。")
    @Pattern(regexp="\\d{10}", message="社員コードは10桁の数字を入力してください。例)0000000001")
    private String employeeCode;
    
    private String departmentCode;
    
    //--Bean バリデーション
    @NotNull(message="社員名の入力は必須です。")
    @Size(min=1, max=60, message="${validatedValue == '' ? '社員名の入力は必須です。' : '社員名は' += min +='～'+=max+='文字以内で入力してください。'}")
    private String name;
    
    //--Bean バリデーション
    @NotNull(message="性別の入力は必須です。")
    private Gender gender;
    
    //--Bean バリデーション
    @NotNull(message="生年月日の入力は必須です。")
    private Date birthday;
    
    //--Bean バリデーション
    @NotNull(message="電話番号の入力は必須です。")
    @Size(min=1, message="電話番号の入力は必須です。")
    @Pattern(regexp = "0\\d{1,4}-\\d{1,4}-\\d{4}", message="電話番号が正しい形式ではありません。")
    private String phone;
    
    //--Bean バリデーション
    @NotNull(message="携帯電話番号の入力は必須です。")
    @Size(min=1, message="携帯電話番号の入力は必須です。")
    @Pattern(regexp = "0\\d{1,4}-\\d{1,4}-\\d{4}", message="携帯電話番号が正しい形式ではありません。")
    private String mobilePhone;
    
    //--Bean バリデーション
    @NotNull(message="郵便番号の入力は必須です。")
    @Size(min=1, message="郵便番号の入力は必須です。")
    @Pattern(regexp = "\\d{3}-\\d{4}", message="郵便番号が正しい形式ではありません。")
    private String zipCode;
    
    //--Bean バリデーション
    @NotNull(message="住所の入力は必須です")
    @Size(min=1, message="住所の入力は必須です")
    private String address;
    
    private String remarks;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="JSFページでURLのクエリパラメータをバインドするプロパティのgetter・setter">
    public QueryParameter getQueryParameter() {
        return queryParameter;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="JSFページで画面項目をバインドするプロパティのgetter・setter">
    public Long getEmployee_id() {
        return employee_id;
    }
    
    public void setEmployee_id(Long employee_id) {
        this.employee_id = employee_id;
    }
    
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
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
//</editor-fold>
    
    public boolean isNew() { return queryParameter.mode == Mode.New; }
    public boolean isRead() { return queryParameter.mode == Mode.Read; }
    public boolean isEdit() { return queryParameter.mode == Mode.Edit; }
    
    @PostConstruct
    public void init()
    {
        System.out.println(">>> Employee init() BEGIN >>>");
        System.out.println("<<< Employee init() END <<<");
    }
    
    @PreDestroy
    public void terminate()
    {
        System.out.println(">>> Employee terminate() BEGIN >>>");
        System.out.println("<<< Employee terminate() END <<<");
    }
    
    public void initQueryParameters()
    {
        System.out.println(">>> Employee initQueryParameters() BEGIN >>>");
        switch(this.queryParameter.mode) {
            case New:
                break;
            case Edit:
            case Read:
                if(this.queryParameter.employeeId == null || this.queryParameter.employeeId.equals("")) break;
                    
                TEmployee entity = em.find(TEmployee.class, this.queryParameter.employeeId);
                if(entity == null) break;
                
                //エンティティの値をプロパティに設定
                this.employee_id = entity.getEmployee_id();
                this.employeeCode = entity.getEmployeeCode();
                this.departmentCode = entity.getDepartmentCode();
                this.name = entity.getName();
                this.gender = entity.getGender();
                this.birthday = entity.getBirthday();
                this.phone = entity.getPhone();
                this.mobilePhone = entity.getMobilePhone();
                this.zipCode = entity.getZipCode();
                this.address = entity.getAddress();
                this.remarks = entity.getRemarks();
                
                System.out.println("employee found! [employeeId=" + entity.getEmployee_id() + ", mode=" + this.queryParameter.mode.name() + "]");
                break;       
            default:
                break;
        }
        System.out.println("<<< Employee initQueryParameters() END <<<");
    }
    
    public String beginEdit()
    {
        System.out.println(">>> Employee beginEdit() BEGIN >>>");
        this.queryParameter.mode = Mode.Edit;
        String viewId = getViewId();
        System.out.println("Return ViewId=" + viewId);
        System.out.println("<<< Employee beginEdit() END <<<");
        return viewId;
    }
    
    public String cancelEdit()
    {
        System.out.println(">>> Employee cancelEdit() BEGIN >>>");
        this.queryParameter.mode = Mode.Read;
        String viewId = getViewId();
        System.out.println("Return ViewId=" + viewId);
        System.out.println("<<< Employee cancelEdit() END <<<");
        return viewId;
    }
    
    public void searchDepartment()
    {
        System.out.println(">>> Employee searchDepartment() BEGIN >>>");
        
        
        
        System.out.println("<<< Employee searchDepartment() END <<<");
    }
    
    @Transactional
    public String save() throws UnsupportedEncodingException
    {
        System.out.println(">>> Employee save() BEGIN >>>");
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.setKeepMessages(true);        //リダイレクト後もFacesMessageが保持されるよう設定する

        String destination;
        if(isNew()) {
            Long employeeCount = em.createQuery("SELECT COUNT(t) FROM TEmployee t WHERE t.employeeCode = :employeeCode", Long.class)
                                            .setParameter("employeeCode", employeeCode)
                                            .getSingleResult();
            if(employeeCount > 0) {
                //同じ社員情報がデータベースに既に存在する場合
                FacesContext.getCurrentInstance().addMessage(
                                null,
                                new FacesMessage("社員コード:" + employeeCode + "は既に使用されています。")
                );
                //登録処理を中止。現在の画面にとどまり、@ViewScopedのCDIビーンの内容を破棄しない。
                destination = null;
                
            } else {
                TEmployee entity = new TEmployee();
                entity.setEmployeeCode(this.employeeCode);
                entity.setDepartmentCode(this.departmentCode);
                entity.setName(this.name);
                entity.setGender(this.gender);
                entity.setBirthday(this.birthday);
                entity.setPhone(this.phone);
                entity.setMobilePhone(this.mobilePhone);
                entity.setZipCode(this.zipCode);
                entity.setAddress(this.address);
                entity.setRemarks(this.remarks);
                em.persist(entity);
                em.flush(); //SQLを発行してIDを自動附番させ、entityに反映させる
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("社員情報を新規登録しました。"));
                
                //処理後は読み取りモードで画面を開きなおす
                StringBuilder builder = new StringBuilder();
                builder.append("employeeDetail.xhtml?faces-redirect=true");
                builder.append("&employee_id=");
                builder.append(entity.getEmployee_id().toString());
                builder.append("&mode=");
                builder.append(URLEncoder.encode(this.queryParameter.mode.Read.name(),"UTF-8"));
                destination = builder.toString();
            }

        } else {
            TEmployee entity = em.find(TEmployee.class, this.employee_id);
            if(entity==null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("社員情報の更新に失敗しました。既に削除されている可能性があります。"));
                //登録処理を中止。現在の画面にとどまり、@ViewScopedのCDIビーンの内容を破棄しない。
                destination = null;
            } else {
                entity.setEmployeeCode(this.employeeCode);
                entity.setDepartmentCode(this.departmentCode);
                entity.setName(this.name);
                entity.setGender(this.gender);
                entity.setBirthday(this.birthday);
                entity.setPhone(this.phone);
                entity.setMobilePhone(this.mobilePhone);
                entity.setZipCode(this.zipCode);
                entity.setAddress(this.address);
                entity.setRemarks(this.remarks);
                em.merge(entity);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("社員情報を保存しました。"));

                //処理後は読み取りモードで画面を開きなおす
                StringBuilder builder = new StringBuilder();
                builder.append("employeeDetail.xhtml?faces-redirect=true");
                builder.append("&employee_id=");
                builder.append(entity.getEmployee_id().toString());
                builder.append("&mode=");
                builder.append(URLEncoder.encode(this.queryParameter.mode.Read.name(),"UTF-8"));
                destination = builder.toString();
            }
        }
        
        System.out.println("<<< Employee save() END <<<");
        return destination;
   }
    
   @Transactional
   public String delete()
   {
       Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
       flash.setKeepMessages(true);        //リダイレクト後もFacesMessageが保持されるよう設定する
       
       TEmployee employee = em.find(TEmployee.class, this.employee_id);
       if(employee == null) {
           FacesContext.getCurrentInstance()
                .addMessage(
                   null, 
                   new FacesMessage("社員コード:" + this.employeeCode + " が見つかりません。既に削除された可能性があります。")
                );
           return "employeeList.xhtml?faces-redirect=true";
       }
       em.remove(employee);
       
       FacesContext.getCurrentInstance()
               .addMessage(null, new FacesMessage("社員コード:" + this.employeeCode + " を削除しました。"));
       return "employeeList.xhtml?faces-redirect=true";
   }
   
//<editor-fold defaultstate="collapsed" desc="プライベート">
   private String getViewId()
   {
       String viewId = "";
       try {
           viewId = String.format("employeeDetail?faces-redirect=true&employee_id=%d&mode=%s"
                   , this.queryParameter.employeeId
                   , URLEncoder.encode(this.queryParameter.mode.name(), "UTF-8"));
           System.out.println("viewId=" + viewId);
           return viewId;
       } catch(UnsupportedEncodingException e) {
           viewId = "employeeDetail?faces-redirect=true";
           e.printStackTrace();
       }
       return viewId;
   }
//</editor-fold>
   
}
