package service;

import java.io.Serializable;
import java.util.Date;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import type.Gender;
import type.TDepartment;
import type.TEmployee;

/**
 *
 * @author owner
 */
@Dependent
public class EmployeeDetailService implements Serializable {
    @PersistenceContext
    private EntityManager em;
    
    public static class FindResult {
        public static enum Type {
            SUCCESS,
            FAILURE_EMPLOYEE_NOT_FOUND
        };
        private static FindResult success(TEmployee employee)
        {
            return new FindResult(Type.SUCCESS, employee);
        }
        private static FindResult failureEmployeeNotFound()
        {
            return new FindResult(Type.FAILURE_EMPLOYEE_NOT_FOUND, null);
        }
        
        private final Type type;
        private final TEmployee employee;
        
        private FindResult(Type type, TEmployee employee)
        {
            this.type = type;
            this.employee = employee;
        }

        public Type getType() {
            return type;
        }

        public TEmployee getEmployee() {
            return employee;
        }
        
    }
    
    public FindResult find(Long employeeId)
    {
        TEmployee employee = em.find(TEmployee.class, employeeId);
        if(employee==null) {
            return FindResult.failureEmployeeNotFound();
        } else {
            return FindResult.success(employee);
        }
    }

    public static class RegistResult {
        public static enum Type {
            SUCCESS,
            FAILURE_EMPLOYEE_ALREADY_EXISTS,
            FAILURE_DEPARTMENT_NOT_FOUND,
        };
        private static RegistResult success(Long employeeId) {
            return new RegistResult(Type.SUCCESS, employeeId);
        }
        private static RegistResult failureEmployeeAlreadExists() { 
            return new RegistResult(Type.FAILURE_EMPLOYEE_ALREADY_EXISTS, null);
        }
        private static RegistResult failureDepartmentNotFound() {
            return new RegistResult(Type.FAILURE_DEPARTMENT_NOT_FOUND, null);
        }
        
        private final Type type;
        private final Long employeeId;
        private RegistResult(Type type, Long employeeId) {
            this.type = type;
            this.employeeId = employeeId;
        }
        public Type getType() { return type; }
        public Long getEmployeeId() { return employeeId; }
    };
    
    public RegistResult regist(
            String employeeCode,
            String departmentCode,
            String name,
            Gender gender,
            Date birthday,
            String phone,
            String mobilePhone,
            String zipCode,
            String address,
            String remarks )
    {
        //既に同じコードの社員が存在しないかチェック
        Long employeeCount = em.createQuery("SELECT COUNT(t) FROM TEmployee t WHERE t.employeeCode = :employeeCode", Long.class)
                                            .setParameter("employeeCode", employeeCode)
                                            .getSingleResult();
        if(employeeCount > 0) {
            //同じ社員情報がデータベースに既に存在する場合
            return RegistResult.failureEmployeeAlreadExists();
        }

        //部署検索
        TypedQuery<TDepartment> query = em.createNamedQuery("TDepartment.findByDepartmentCode", TDepartment.class);
        query.setParameter("departmentCode", departmentCode);
        TDepartment department;
        try {
            department = query.getSingleResult();
        } catch(NoResultException ex) {
            //部署が見つからない
            return RegistResult.failureDepartmentNotFound();
        }
        
        //社員情報登録
        TEmployee entity = new TEmployee();
        entity.setEmployeeCode(employeeCode);
        entity.setDepartment(department);
        entity.setName(name);
        entity.setGender(gender);
        entity.setBirthday(birthday);
        entity.setPhone(phone);
        entity.setMobilePhone(mobilePhone);
        entity.setZipCode(zipCode);
        entity.setAddress(address);
        entity.setRemarks(remarks);
        em.persist(entity);
        em.flush(); //SQLを発行してIDを自動附番させ、entityに反映させる

        return RegistResult.success(entity.getEmployee_id());
    }
    
    
    
    public static enum UpdateResult {
        SUCCESS,
        FAILURE_EMPLOYEE_NOT_FOUND,
        FAILURE_DEPARTMENT_NOT_FOUND,
    };
    
    public UpdateResult update(
            Long employee_id,
            String employeeCode,
            String departmentCode,
            String name,
            Gender gender,
            Date birthday,
            String phone,
            String mobilePhone,
            String zipCode,
            String address,
            String remarks )
    {
            TEmployee entity = em.find(TEmployee.class, employee_id);
            if(entity==null) {
                return UpdateResult.FAILURE_EMPLOYEE_NOT_FOUND;
            } else {
                TDepartment department;
                try {
                    TypedQuery<TDepartment> query = em.createNamedQuery("TDepartment.findByDepartmentCode", TDepartment.class);
                    query.setParameter("departmentCode", departmentCode);
                    department = query.getSingleResult();   //見つからない場合, NoResultExceptionをスロー
                } catch(NoResultException ex) {
                    return UpdateResult.FAILURE_DEPARTMENT_NOT_FOUND;
                }
                    
                entity.setEmployeeCode(employeeCode);
                entity.setDepartment(department);
                entity.setName(name);
                entity.setGender(gender);
                entity.setBirthday(birthday);
                entity.setPhone(phone);
                entity.setMobilePhone(mobilePhone);
                entity.setZipCode(zipCode);
                entity.setAddress(address);
                entity.setRemarks(remarks);
                em.merge(entity);
                return UpdateResult.SUCCESS;
            }        
    }
    
    
    public static enum DeleteResult {
        SUCCESS,
        FAILURE_EMPLOYEE_NOT_FOUND,
    }
    
    public DeleteResult delete(String employeeCode)
    {
        try {
            TEmployee employee = em.createNamedQuery("TEmployee.findByEmployeeCode", TEmployee.class)
                                     .setParameter("employeeCode", employeeCode)
                                     .getSingleResult();
            em.remove(employee);
            
            return DeleteResult.SUCCESS;
            
        } catch(NoResultException ex) {
            return DeleteResult.FAILURE_EMPLOYEE_NOT_FOUND;
        }
    }
}
