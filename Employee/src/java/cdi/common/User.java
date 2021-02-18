/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cdi.common;

import cdi.dependent.UserService;
import exception.cdi.dependent.UserLoginException;
import exception.cdi.dependent.UserLogoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author owner
 */
@Named
@RequestScoped
public class User {
    private final static Logger LOGGER = Logger.getLogger(User.class.getName());
    
    @Inject
    private UserService userService;
    
    public String getUserName()
    {
        return userService.getUserName();
    }
    
    public boolean isAuthenticated()
    {
        return userService.isAuthenticated();
    }
    
    public boolean hasAdminRole()
    {
        return userService.hasAdminRole();
    }
    
    public boolean hasEmployeeRole()
    {
        return userService.hasEmployeeRole();
    }
    
    public String login(String userName, String password)
    {
        LOGGER.log(Level.INFO, ">>>>>>>>>>>>>>>> ログイン開始 >>>>>>>>>>>>>>>>");
        try {
            userService.login(userName, password);
            return "employeeList.xhtml?faces-redirect=true";
        } catch(UserLoginException ex) {
            String message = "ユーザ名またはパスワードに誤りがあります。";
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, message, ""));
            LOGGER.log(Level.SEVERE, message, ex);
        }
        return "";
    }
    
    public String logout()
    {
        try {
            userService.logout();
        } catch(UserLogoutException ex) {
            String message = "ログアウトに失敗しました。";
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, message, ""));
            LOGGER.log(Level.SEVERE, message, ex);
        }
        return "login.xhtml?faces-redirect=true";
    }    
}
