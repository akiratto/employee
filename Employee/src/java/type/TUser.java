/**
 * This file was generated by the Jeddict
 */
package type;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Owner
 */
@Entity
public class TUser implements Serializable {

    @Id
    private String userid;

    @Basic
    private String salt;

    @Basic
    private String password;

    public String getUserid() {
        return this.userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getSalt() {
        return this.salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!java.util.Objects.equals(getClass(), obj.getClass())) {
            return false;
        }
        final TUser other = (TUser) obj;
        if (!java.util.Objects.equals(this.getUserid(), other.getUserid())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.getUserid() != null ? this.getUserid().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "TUser{" + " userid=" + userid + '}';
    }

}