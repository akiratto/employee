package ui;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import service.AdminService;
import service.AdminService.UpsertUserResult;

/**
 *
 * @author owner
 */
@Named
@RequestScoped
public class Admin {

    private String addUserName;
    private String addPassword;
    private String addGroupNames;
    
    @Inject
    private AdminService service;

    public String getAddUserName() {
        return addUserName;
    }

    public void setAddUserName(String addUserName) {
        this.addUserName = addUserName;
    }

    public String getAddPassword() {
        return addPassword;
    }

    public void setAddPassword(String addPassword) {
        this.addPassword = addPassword;
    }

    public String getAddGroupNames() {
        return addGroupNames;
    }

    public void setAddGroupNames(String addGroupNames) {
        this.addGroupNames = addGroupNames;
    }
    
    @Transactional
    public void addUser()
    {
        try {
            UpsertUserResult result = service.upsertUser(addUserName, addPassword, addGroupNames);
            switch(result) {
                case INSERTED:
                    //メッセージの表示
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, addUserName + "を新規登録しました。", ""));
                    break;

                case UPDATED:
                    //メッセージの表示
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, addUserName + "のパスワードを更新しました。", ""));
                    break;
            }
        } catch(Exception ex) {
            String errorMessage = "ユーザの登録に失敗しました。";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, errorMessage, ""));
            System.err.println(errorMessage);
            ex.printStackTrace();
        }
    }
    

}
