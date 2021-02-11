package employeerealm;

import com.sun.appserv.security.AppservRealm;
import com.sun.enterprise.security.auth.realm.BadRealmException;
import com.sun.enterprise.security.auth.realm.InvalidOperationException;
import com.sun.enterprise.security.auth.realm.NoSuchRealmException;
import com.sun.enterprise.security.auth.realm.NoSuchUserException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import employeerealm.util.Password;
import employeerealm.util.SecurityStore;

/**
 *
 * @author owner
 */
public class EmployeeRealm extends AppservRealm {
    private String jaasCtxName;
    private String dataSource;
    
    @Override
    protected void init(Properties properties) throws BadRealmException, NoSuchRealmException {
        _logger.fine("init()");
        jaasCtxName = properties.getProperty("jaas-context", "employeeRealm");
        dataSource  = properties.getProperty("dataSource", "jdbc/employee");
    }

    @Override
    public synchronized String getJAASContext() {
        return jaasCtxName;
    }
    
    @Override
    public String getAuthType() {
        return "EmployeeRealm";
    }
    
    
    public String[] authenticate(String userid, String givenPassword) {
        
        SecurityStore store = new SecurityStore(dataSource);
        String salt = store.getSaltForUser(userid);
        
        String[] result = null;
        
        if(salt != null) {
            Password password = new Password();
            byte[] saltBytes = password.bytesFromBase64(salt);
            byte[] passwordBytes = password.hashWithSalt(givenPassword, saltBytes);
            String passwordBase64 = password.base64FromBytes(passwordBytes);
            if(store.validateUser(userid, passwordBase64)) {
                result = store.findGroups(userid);
            }
        }
        return result;
    }

    @Override
    public Enumeration<String> getGroupNames(String userid) throws InvalidOperationException, NoSuchUserException {
        SecurityStore store = new SecurityStore(dataSource);
        String[] groups = store.findGroups(userid);
        return groups == null ? null : Collections.enumeration(Arrays.asList(groups));
    }
    
}
