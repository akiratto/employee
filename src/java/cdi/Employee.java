package cdi;

import entity.TEmployee;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 *
 * @author owner
 */
@Named
@RequestScoped
public class Employee implements Serializable {
    public enum Mode {
        Undefined,
        New,
        Read,
        Edit
    }
    private String employeeId;
    private String name;
    private String gender;
    private String phone;
    private String mobilePhone;
    private String zipCode;
    private String address;
    private String remarks;
    private Mode mode = Mode.Undefined;
    
    @PersistenceContext
    protected EntityManager em;
    
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
        if(employeeId != null && !employeeId.equals("")) {
            TEmployee tEmployee = em.find(TEmployee.class, employeeId);
            if(tEmployee != null) {
                System.out.println("employee found! [employeeId=" + tEmployee.getEmployee_id() + ", mode=" + mode.name() + "]");
                employeeId = tEmployee.getEmployee_id();
                name = tEmployee.getName();
                gender = tEmployee.getGender();
                phone = tEmployee.getPhone();
                mobilePhone = tEmployee.getMobilePhone();
                zipCode = tEmployee.getZipCode();
                address = tEmployee.getAddress();
                remarks = tEmployee.getRemarks();
            }
        }
        System.out.println("<<< Employee initQueryParameters() END <<<");
    }
    
    private String getViewId() throws UnsupportedEncodingException
    {
        String viewId = String.format("employeeDetail?faces-redirect=true&employee_id=%s&mode=%s"
                                            , URLEncoder.encode(employeeId, "UTF-8")
                                            , URLEncoder.encode(mode.name(), "UTF-8"));
        System.out.println("viewId=" + viewId);
        return viewId;
    }
    
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getMode() {
        return mode.name();
    }

    public void setMode(String mode) {
        this.mode = Mode.valueOf(mode);
    }
    
    public boolean isNew() { return mode == Mode.New; }
    public boolean isRead() { return mode == Mode.Read; }
    public boolean isEdit() { return mode == Mode.Edit; }
    
    public String beginEdit() throws UnsupportedEncodingException
    {
        System.out.println(">>> Employee beginEdit() BEGIN >>>");
        this.mode = Mode.Edit;
        String viewId = getViewId();
        System.out.println("Return ViewId=" + viewId);
        System.out.println("<<< Employee beginEdit() END <<<");
        return viewId;
    }
    
    public String cancelEdit() throws UnsupportedEncodingException
    {
        System.out.println(">>> Employee cancelEdit() BEGIN >>>");
        this.mode = Mode.Read;
        String viewId = getViewId();
        System.out.println("Return ViewId=" + viewId);
        System.out.println("<<< Employee cancelEdit() END <<<");
        return viewId;
    }
    
    @Transactional
    public void save()
    {
        System.out.println(">>> Employee save() BEGIN >>>");
        TEmployee tEmployee = em.find(TEmployee.class, employeeId);
        if(tEmployee==null) {
            tEmployee = new TEmployee();
            tEmployee.setAddress(address);
            tEmployee.setEmployee_id(employeeId);
            tEmployee.setGender(gender);
            tEmployee.setMobilePhone(mobilePhone);
            tEmployee.setName(name);
            tEmployee.setPhone(phone);
            tEmployee.setRemarks(remarks);
            tEmployee.setZipCode(zipCode);
            em.persist(tEmployee);
        } else {
            tEmployee.setAddress(address);
            tEmployee.setEmployee_id(employeeId);
            tEmployee.setGender(gender);
            tEmployee.setMobilePhone(mobilePhone);
            tEmployee.setName(name);
            tEmployee.setPhone(phone);
            tEmployee.setRemarks(remarks);
            tEmployee.setZipCode(zipCode);
            em.merge(tEmployee);
        }
        System.out.println("<<< Employee save() END <<<");
   }

    public void showMessage()
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String message =  "名前:%s\n "
                        + "性別:%s\n "
                        + "電話番号:%s\n "
                        + "携帯電話番号:%s \n "
                        + "郵便番号:%s \n"
                        + "住所:%s \n";
        message = String.format(message, name, gender, phone, mobilePhone, zipCode, address);
        FacesMessage normalMessage = new FacesMessage(message);
        facesContext.addMessage(null, normalMessage);
    }
}
