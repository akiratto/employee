package model;

import entity.TGroup;
import entity.TGroupPK;
import entity.TUser;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import realm.util.Password;

/**
 *
 * @author owner
 */
@Dependent
public class AdminService {
    @PersistenceContext
    EntityManager em;

    public static enum UpsertUserResult {
        INSERTED,
        UPDATED,
    }
    public UpsertUserResult upsertUser(String addUserName, String addPassword, String addGroupNames)
    {
        HashedPasswordAndSalt hashedPasswordAndSalt = generateHashedPasswordAndSalt(addPassword);

        TUser user = em.find(TUser.class, addUserName);
        if(user==null) {
            //ユーザの新規登録
            user = new TUser();
            user.setUserid(addUserName);
            user.setSalt(hashedPasswordAndSalt.getSalt());
            user.setPassword(hashedPasswordAndSalt.getHashedPassword());
            em.persist(user);

            //ユーザの所属するグループを更新
            updateGroups(addUserName, addGroupNames);

            return UpsertUserResult.INSERTED;
        } else {
            //ユーザのパスワード更新
            user.setSalt(hashedPasswordAndSalt.getSalt());
            user.setPassword(hashedPasswordAndSalt.getHashedPassword());
            em.merge(user);

            //ユーザの所属するグループを更新
            updateGroups(addUserName, addGroupNames);

            return UpsertUserResult.UPDATED;
        }
    }    
    
    private void updateGroups(String userid, String groupNames)
    {
        //ユーザの所属するグループの更新
        int deleteCount = em.createNamedQuery("TGroup.deleteByUserid", TGroup.class)
                                .setParameter("userid", userid)
                                .executeUpdate();
        List<String> addGroupNameList = Arrays.asList(groupNames.split(","));
        for(String addGroupName : addGroupNameList) {
            TGroup group = new TGroup();
            TGroupPK groupPk = new TGroupPK();
            groupPk.setUserid(userid);
            groupPk.setGroupid(addGroupName);
            group.setGroupPK(groupPk);
            em.persist(group);
        }
    }
    
    private static class HashedPasswordAndSalt {
        private String hashedPassword;
        private String salt;

        public HashedPasswordAndSalt(String hashedPassword, String salt) {
            this.hashedPassword = hashedPassword;
            this.salt = salt;
        }

        public String getHashedPassword() {
            return hashedPassword;
        }

        public String getSalt() {
            return salt;
        }
    }
    private HashedPasswordAndSalt generateHashedPasswordAndSalt(String givenPassword)
    {
        Password password = new Password();

        byte[] saltBytes = password.getSalt(20);
        String salt = password.base64FromBytes(saltBytes);
        byte[] hashedPasswordBytes = password.hashWithSalt(givenPassword, saltBytes);
        String hashedPassword = password.base64FromBytes(hashedPasswordBytes);
        
        return new HashedPasswordAndSalt(hashedPassword, salt);
    }
}
