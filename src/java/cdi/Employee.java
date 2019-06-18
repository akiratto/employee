package cdi;

import entity.TEmployee;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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
    private String employeeId;
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
        if(employeeId != null && !employeeId.equals("")) {
            entity = em.find(TEmployee.class, employeeId);
            if(entity != null) {
                System.out.println("employee found! [employeeId=" + entity.getEmployee_id() + ", mode=" + mode.name() + "]");
            }
        }
        System.out.println("<<< Employee initQueryParameters() END <<<");
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
    
    private String getViewId() throws UnsupportedEncodingException
    {
        String viewId = String.format("employeeDetail?faces-redirect=true&employee_id=%s&mode=%s"
                                            , URLEncoder.encode(employeeId, "UTF-8")
                                            , URLEncoder.encode(mode.name(), "UTF-8"));
        System.out.println("viewId=" + viewId);
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
        if(entity==null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
        System.out.println("<<< Employee save() END <<<");
   }
}
