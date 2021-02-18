package model;

import exception.cdi.dependent.UserLoginException;
import exception.cdi.dependent.UserLogoutException;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author owner
 */
@Dependent
public class UserService {
    @Inject
    private HttpServletRequest request;
    
    public void login(String userName, String password) throws UserLoginException
    {
        try {
            //既に認証済みの場合、ログアウトする
            if(request.getUserPrincipal()!=null) {
                request.logout();
            }
            request.login(userName, password);
        } catch (ServletException ex) {
            throw new UserLoginException(ex);
        }
    }
    
    public void logout() throws UserLogoutException
    {
        //セッションを終了する
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        try {
            request.logout();
        } catch(ServletException ex) {
            throw new UserLogoutException(ex);
        }
    }
    
    public boolean isAuthenticated()
    {
        return (request.getUserPrincipal()!=null);
    }
    
    public String getUserName()
    {
        return isAuthenticated() ? "" : request.getUserPrincipal().getName();
    }
    
    public boolean hasAdminRole()
    {
        return request.isUserInRole("adminRole");
    }
    
    public boolean hasEmployeeRole()
    {
        return request.isUserInRole("employeeRole");
    }    
}
