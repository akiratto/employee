package employeerealm.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author owner
 */
public class SecurityStore {
    private Connection connection;
    private final static Logger LOGGER = Logger.getLogger(SecurityStore.class.getName());
    private final static String SALT_FOR_USER = "SELECT salt FROM tuser WHERE userid = ?;";
    private final static String VERIFY_USER = "SELECT userid FROM tuser WHERE userid = ? AND password = ?;";
    private final static String FIND_GROUP = "SELECT groupid FROM tgroup WHERE userid=?;";
    
    public SecurityStore(String dataSourceName)
    {
        Context context = null;
        try {
            context = new InitialContext();
            DataSource dataSource = (javax.sql.DataSource)context.lookup(dataSourceName);
            connection = dataSource.getConnection();
        } catch (NamingException | SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error getting connection!", ex);
        } finally {
            if (context != null) {
                try {
                    context.close();
                } catch (NamingException ex) {
                    LOGGER.log(Level.SEVERE, "Error closing context!", ex);
                }
            }
        }
    }
       
    public String getSaltForUser(String userid)
    {
        String salt = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SALT_FOR_USER);
            preparedStatement.setString(1, userid);
            ResultSet recordSet = preparedStatement.executeQuery();
            
            if(recordSet.next()) {
                salt = recordSet.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SecurityStore.class.getName()).log(Level.SEVERE, "User not found!", ex);
        }
        return salt;
    }
    
    public boolean validateUser(String userid, String password) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(VERIFY_USER);
            preparedStatement.setString(1, userid);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return true;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "User validation failed!", ex);
        }
        return false;
    }
    
    public String[] findGroups(String userid)
    {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_GROUP);
            preparedStatement.setString(1, userid);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<String> groupIdList = new ArrayList<>();
            while(resultSet.next()) {
                groupIdList.add(resultSet.getString("groupid"));
            }
            return groupIdList.toArray(new String[groupIdList.size()]);
        } catch (SQLException ex) {
            Logger.getLogger(SecurityStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
