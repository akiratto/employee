package cdi.dependent.employeelist;

import entity.TEmployee;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

/**
 *
 * @author owner
 */
@Dependent
public class EmployeeQuery implements Serializable {
    
    @PersistenceContext
    private EntityManager em;
    
    public long countEmployeeAllCount(TEmployee searchCondition)
    {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(t) FROM TEmployee t \n");
        appendWherePhrase(sql);
        
        TypedQuery<Long> query = em.createQuery(sql.toString(), Long.class);
        setQueryParameters(query, searchCondition);

        return query.getSingleResult();
    }
    
    public List<TEmployee> extractEmployees(TEmployee searchCondition, long employeeAllCount, int startPosition, int maxResult)
    {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT t FROM TEmployee t \n");
        appendWherePhrase(sql);

        TypedQuery<TEmployee> query = em.createQuery(sql.toString(), TEmployee.class);
        setQueryParameters(query, searchCondition);

        return query.setFirstResult(startPosition)
                    .setMaxResults(maxResult)
                    .getResultList();
    }
    
    private void appendWherePhrase(StringBuilder sql)
    {
        sql.append("WHERE (:employeeCodeIsEmpty = TRUE OR t.employeeCode = :employeeCode)   \n");
        sql.append("  AND (:nameIsEmpty         = TRUE OR t.name          LIKE :name)       \n");
        sql.append("  AND (:genderIsEmpty       = TRUE OR t.gender        = :gender)        \n");
        sql.append("  AND (:birthdayIsEmpty     = TRUE OR t.birthday      = :birthday)      \n");
        sql.append("  AND (:phoneIsEmpty        = TRUE OR t.phone         LIKE :phone)      \n");
        sql.append("  AND (:mobilePhoneIsEmpty  = TRUE OR t.mobilePhone   LIKE :mobilePhone)\n");
        sql.append("  AND (:zipCodeIsEmpty      = TRUE OR t.zipCode       LIKE :zipCode)    \n");
        sql.append("  AND (:addressIsEmpty      = TRUE OR t.address       LIKE :address)    \n");
        sql.append("  AND (:remarksIsEmpty      = TRUE OR t.remarks       LIKE :remarks)    \n");
    }
    
    private void setQueryParameters(Query query, TEmployee searchCondition)
    {
        boolean employeeCodeIsEmpty = searchCondition.getEmployeeCode()==null   || searchCondition.getEmployeeCode().isEmpty();
        boolean nameIsEmpty         = searchCondition.getName()==null           || searchCondition.getName().isEmpty();
        boolean genderIsEmpty       = searchCondition.getGender()==null;
        boolean birthdayIsEmpty     = searchCondition.getBirthday()==null;
        boolean phoneIsEmpty        = searchCondition.getPhone()==null          || searchCondition.getPhone().isEmpty();
        boolean mobilePhoneIsEmpty  = searchCondition.getMobilePhone()==null    || searchCondition.getMobilePhone().isEmpty();
        boolean zipCodeIsEmpty      = searchCondition.getZipCode()==null        || searchCondition.getZipCode().isEmpty();
        boolean addressIsEmpty      = searchCondition.getAddress()==null        || searchCondition.getAddress().isEmpty();
        boolean remarksIsEmpty      = searchCondition.getRemarks()==null        || searchCondition.getRemarks().isEmpty();
        
        query.setParameter("employeeCodeIsEmpty", employeeCodeIsEmpty);
        query.setParameter("nameIsEmpty"        , nameIsEmpty        );
        query.setParameter("genderIsEmpty"      , genderIsEmpty      );
        query.setParameter("birthdayIsEmpty"    , birthdayIsEmpty    );
        query.setParameter("phoneIsEmpty"       , phoneIsEmpty       );
        query.setParameter("mobilePhoneIsEmpty" , mobilePhoneIsEmpty );
        query.setParameter("zipCodeIsEmpty"     , zipCodeIsEmpty     );
        query.setParameter("addressIsEmpty"     , addressIsEmpty     );
        query.setParameter("remarksIsEmpty"     , remarksIsEmpty     );  
        
        String employeeCode = searchCondition.getEmployeeCode() == null ? "" : searchCondition.getEmployeeCode();
        String name         = searchCondition.getName()         == null ? "" : "%" + likeEscape(searchCondition.getName()) + "%";
        Date   birthday     = searchCondition.getBirthday();
        String phone        = searchCondition.getPhone()        == null ? "" : "%" + likeEscape(searchCondition.getPhone()) + "%";
        String mobilePhone  = searchCondition.getMobilePhone()  == null ? "" : "%" + likeEscape(searchCondition.getMobilePhone()) + "%";
        String zipCode      = searchCondition.getZipCode()      == null ? "" : "%" + likeEscape(searchCondition.getZipCode()) + "%";
        String address      = searchCondition.getAddress()      == null ? "" : "%" + likeEscape(searchCondition.getAddress()) + "%";
        String remarks      = searchCondition.getRemarks()      == null ? "" : "%" + likeEscape(searchCondition.getRemarks()) + "%";
        
        query.setParameter("employeeCode",  employeeCode);
        query.setParameter("name",          name);
        query.setParameter("gender",        searchCondition.getGender());
        query.setParameter("birthday",      birthday, TemporalType.DATE);
        query.setParameter("phone",         phone);
        query.setParameter("mobilePhone",   mobilePhone);
        query.setParameter("zipCode",       zipCode);
        query.setParameter("address",       address);
        query.setParameter("remarks",       remarks);
    }
    
    private String likeEscape(String likeCondition)
    {
        return likeCondition
                .replaceAll("_", "\\\\_")
                .replaceAll("%", "\\\\%");
    }
}
