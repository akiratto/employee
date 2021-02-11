package cdi;

import entity.TGroup;
import entity.TGroupPK;
import entity.TUser;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import realm.util.Password;

/**
 *
 * @author owner
 */
@Named
@RequestScoped
public class Admin {
    
    @PersistenceContext
    EntityManager em;
    
    private String addUserName;
    private String addPassword;
    private String addGroupNames;

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
            Password password = new Password();

            byte[] saltBytes = password.getSalt(20);
            String salt = password.base64FromBytes(saltBytes);
            byte[] hashedPasswordBytes = password.hashWithSalt(addPassword, saltBytes);
            String hashedPassword = password.base64FromBytes(hashedPasswordBytes);

            TUser user = em.find(TUser.class, addUserName);
            if(user==null) {
                //ユーザの新規登録
                user = new TUser();
                user.setUserid(addUserName);
                user.setSalt(salt);
                user.setPassword(hashedPassword);
                em.persist(user);
                
                //ユーザの所属するグループを更新
                updateGroups(addUserName, addGroupNames);
                
                //メッセージの表示
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, user.getUserid() + "を新規登録しました。", ""));
            } else {
                //ユーザのパスワード更新
                user.setSalt(salt);
                user.setPassword(hashedPassword);
                em.merge(user);
                
                //ユーザの所属するグループを更新
                updateGroups(addUserName, addGroupNames);
                
                //メッセージの表示
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, user.getUserid() + "のパスワードを更新しました。", ""));
            }

        } catch(Exception e) {
            String errorMessage = "ユーザの登録に失敗しました。";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, errorMessage, ""));
            System.err.println(errorMessage);
            e.printStackTrace();
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
}
