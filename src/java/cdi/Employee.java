package cdi;

import entity.TEmployee;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

@Named
@ViewScoped
public class Employee implements Serializable {
    public enum Mode {
        Undefined,
        New,
        Read,
        Edit
    }
    private Integer employeeId;
    private TEmployee entity;
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
        switch(this.mode) {
            case New:
                entity = new TEmployee();
                break;
            case Edit:
            case Read:
                if(employeeId == null || employeeId.equals("")) break;
                    
                entity = em.find(TEmployee.class, employeeId);
                if(entity == null) break;
                System.out.println("employee found! [employeeId=" + entity.getEmployee_id() + ", mode=" + mode.name() + "]");

                Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
                String message = (String)flash.getOrDefault("message","");
                if(!message.isEmpty()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
                }
                break;       
            default:
                break;
        }
        System.out.println("<<< Employee initQueryParameters() END <<<");
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }
    
    private String getViewId() 
    {
        String viewId = "";
        try {
            viewId = String.format("employeeDetail?faces-redirect=true&employee_id=%d&mode=%s"
                                                , employeeId
                                                , URLEncoder.encode(mode.name(), "UTF-8"));
            System.out.println("viewId=" + viewId);
            return viewId;
        } catch(UnsupportedEncodingException e) {
            viewId = "employeeDetail?faces-redirect=true";
            e.printStackTrace();
        }
        return viewId;
    }

    public TEmployee getEntity() {
        return entity;
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
    
    public String beginEdit()
    {
        System.out.println(">>> Employee beginEdit() BEGIN >>>");
        this.mode = Mode.Edit;
        String viewId = getViewId();
        System.out.println("Return ViewId=" + viewId);
        System.out.println("<<< Employee beginEdit() END <<<");
        return viewId;
    }
    
    public String cancelEdit()
    {
        System.out.println(">>> Employee cancelEdit() BEGIN >>>");
        this.mode = Mode.Read;
        String viewId = getViewId();
        System.out.println("Return ViewId=" + viewId);
        System.out.println("<<< Employee cancelEdit() END <<<");
        return viewId;
    }
    
    @Transactional
    public String save() throws UnsupportedEncodingException
    {
        System.out.println(">>> Employee save() BEGIN >>>");
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        if(isNew()) {
            em.persist(entity);
            flash.put("message", "社員情報を新規登録しました。");
        } else {
            em.merge(entity);
            flash.put("message","社員情報を保存しました。");
        }
        
        StringBuilder builder = new StringBuilder();
        builder.append("employeeDetail.xhtml?faces-redirect=true");
        builder.append("&employee_id=");
        builder.append(employeeId.toString());
        builder.append("&mode=");
        builder.append(URLEncoder.encode(mode.name(),"UTF-8"));
        
        System.out.println("<<< Employee save() END <<<");
        return builder.toString();
   }
    
   @Transactional
   public String delete()
   {
       Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
       
       TEmployee employee = em.find(TEmployee.class, this.employeeId);
       if(employee == null) {
           flash.put("message", "社員ID:" + this.employeeId + " が見つかりません。既に削除された可能性があります。");
           return "employeeList.xhtml?faces-redirect=true";
       }
       em.remove(employee);
       flash.put("message", "社員ID:" + this.employeeId + " を削除しました。");
       return "employeeList.xhtml?faces-redirect=true";
   }
}
