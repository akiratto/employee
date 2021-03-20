package service;

import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import type.CsvEmployee;
import type.TDepartment;
import type.TEmployee;

/**
 *
 * @author Owner
 */
@Dependent
public class EmployeeBatchService implements Serializable {
    @PersistenceContext
    private EntityManager em;
    
    
    public static enum BatchResult {
        EMPLOYEE_NOT_FOUND,
        DEPARTMENT_NOT_FOUND,
    }
    
    public boolean existsEmployee(String employeeCode)
    {
        try {
            TEmployee employee = em.createNamedQuery("TEmployee.findByEmployeeCode", TEmployee.class).setParameter("employeeCode", employeeCode).getSingleResult();
            return true;
        } catch(NoResultException e) {
            return false;
        }
    }
    
    @Transactional
    public void batchRegist(List<CsvEmployee> csvEmployees)
    {
        for(CsvEmployee csvEmployee : csvEmployees) {
            
            TEmployee employee;
            try {
                TypedQuery<TEmployee> query1 = em.createNamedQuery("TEmployee.findByEmployeeCode", TEmployee.class);
                employee = query1.setParameter("employeeCode", csvEmployee.getEmployeeCode())
                                 .getSingleResult();
                
                TDepartment department = null;
                TypedQuery<TDepartment> query2 = em.createNamedQuery("TDepartment.findByDepartmentCode", TDepartment.class);
                try {
                    department = query2.setParameter("departmentCode", csvEmployee.getDepartmentCode())
                                                   .getSingleResult();
                } catch(NoResultException ex) {
                    
                }
                employee.setEmployeeCode(csvEmployee.getEmployeeCode());
                employee.setDepartment(department);
                employee.setName(csvEmployee.getName());
                employee.setGender(csvEmployee.getGender());
                employee.setBirthday(csvEmployee.getBirthday());
                employee.setPhone(csvEmployee.getPhone());
                employee.setMobilePhone(csvEmployee.getMobilePhone());
                employee.setZipCode(csvEmployee.getZipCode());
                employee.setAddress(csvEmployee.getAddress());
                em.merge(employee);
                
            } catch(NoResultException ex) {
                employee = new TEmployee();
                TDepartment department = null;
                TypedQuery<TDepartment> query2 = em.createNamedQuery("TDepartment.findByDepartmentCode", TDepartment.class);
                try {
                    department = query2.setParameter("departmentCode", csvEmployee.getDepartmentCode())
                                                   .getSingleResult();
                } catch(NoResultException ex2) {
                    
                }
                employee.setEmployeeCode(csvEmployee.getEmployeeCode());
                employee.setDepartment(department);
                employee.setName(csvEmployee.getName());
                employee.setGender(csvEmployee.getGender());
                employee.setBirthday(csvEmployee.getBirthday());
                employee.setPhone(csvEmployee.getPhone());
                employee.setMobilePhone(csvEmployee.getMobilePhone());
                employee.setZipCode(csvEmployee.getZipCode());
                employee.setAddress(csvEmployee.getAddress());
                employee.setRemarks("");
                em.persist(employee);
            }
        }
    }
}
