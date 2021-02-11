package cdi;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.print.attribute.standard.Severity;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Owner
 */
@Named
@RequestScoped
public class Login {
    private String userName;
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String login()
    {
        HttpServletRequest request 
                = (HttpServletRequest)FacesContext.getCurrentInstance()
                                                  .getExternalContext()
                                                  .getRequest();
        try {
            request.login(userName, password);
        } catch(Exception ex) {
            FacesContext.getCurrentInstance()
                        .addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                                                         "ユーザ名またはパスワードに誤りがあります。", ""));
            ex.printStackTrace();
        }
        return "employeeList.xhtml?faces-redirect=true";
    }
    
    public String logout()
    {
        //セッションを終了する
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        HttpServletRequest request 
                = (HttpServletRequest)FacesContext.getCurrentInstance()
                                                  .getExternalContext()
                                                  .getRequest();
        try {
            request.logout();
        } catch(ServletException ex) {
            ex.printStackTrace();
        }
        return "login.xhtml?faces-redirect=true";
    }
}
