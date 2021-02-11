package employeerealm;

import com.sun.appserv.security.AppservPasswordLoginModule;
import javax.security.auth.login.LoginException;

/**
 *
 * @author owner
 */
public class EmployeeLoginModule extends AppservPasswordLoginModule {

    @Override
    protected void authenticateUser() throws LoginException {
        EmployeeRealm realm = (EmployeeRealm)getCurrentRealm();
        String[] groups = realm.authenticate(_username, _password);
        if(groups == null) {
            throw new LoginException("login failed.");
        }
        commitUserAuthentication(groups);
    }
    
}
