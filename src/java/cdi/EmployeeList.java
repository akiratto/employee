package cdi;

import entity.TEmployee;
import entity.TEmployee_;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author owner
 */
@Named
@RequestScoped
public class EmployeeList {
    @PersistenceContext
    private EntityManager em;
    
    private TEmployee searchCondition = new TEmployee();
    private List<TEmployee> employeeList;
    
    @PostConstruct
    public void init()
    {
        System.out.println(">>> EmployeeList init() BEGIN >>>");
        System.out.println("<<< EmployeeList init() END <<<");
    }
    
    @PreDestroy
    public void terminate()
    {
        System.out.println(">>> EmployeeList terminate() BEGIN >>>");
        System.out.println("<<< EmployeeList terminate() END <<<");
    }
    
    public void initQueryParameters()
    {
        
    }

    public TEmployee getSearchCondition() {
        return searchCondition;
    }

    public List<TEmployee> extract()
    {
        List<TEmployee> employeeList;
        System.out.println(">>> EmployeeList extract() BEGIN >>>");
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TEmployee> cq = cb.createQuery(TEmployee.class);
        Root<TEmployee> root = cq.from(TEmployee.class);
        cq = cq.select(root);
        cq = searchCondition != null && !searchCondition.equals("") ? 
                cq.where(cb.equal(root.get(TEmployee_.employee_id), searchCondition.getEmployee_id())) : cq ;
                

        
//エラー
//        String sql = "SELECT t FROM TEmployee t "
//                   + "WHERE (t.employee_id = :searchEmployeeId OR :searchEmployeeId IS NULL) "
//                   + "  AND (t.name like :searchName OR :searchName IS NULL OR :searchName = '') "
//                   + "  AND (t.gender like :searchGender OR :searchGender IS NULL OR :searchGender = '') "
//                   + "  AND (t.phone like :searchPhone OR :searchPhone IS NULL OR :searchPhone = '') "
//                   + "  AND (t.mobilePhone like :searchMobilePhone OR :searchMobilePhone IS NULL OR :searchMobilePhone = '') "
//                   + "  AND (t.zipCode like :searchZipCode OR :searchZipCode IS NULL OR :searchZipCode = '') "
//                   + "  AND (t.address like :searchAddress OR :searchAddress IS NULL OR :searchAddress = '') "
//                   + "  AND (t.remarks like :searchRemarks OR :searchRemarks IS NULL OR :searchRemarks = '') ";


//        String sql = "SELECT t FROM TEmployee t "
//                   + "WHERE (t.employee_id = :searchEmployeeId) "
//                   + "  AND (t.name like :searchName) "
//                   + "  AND (t.gender like :searchGender) "
//                   + "  AND (t.phone like :searchPhone) "
//                   + "  AND (t.mobilePhone like :searchMobilePhone) "
//                   + "  AND (t.zipCode like :searchZipCode) "
//                   + "  AND (t.address like :searchAddress) "
//                   + "  AND (t.remarks like :searchRemarks) ";
//        employeeList = em.createQuery(sql, TEmployee.class)
//                            .setParameter("searchEmployeeId", searchCondition.getEmployee_id())
//                            .setParameter("searchName", searchCondition.getName())
//                            .setParameter("searchGender", searchCondition.getGender())
//                            .setParameter("searchPhone", searchCondition.getPhone())
//                            .setParameter("searchMobilePhone", searchCondition.getMobilePhone())
//                            .setParameter("searchZipCode", searchCondition.getZipCode())
//                            .setParameter("searchAddress", searchCondition.getAddress())
//                            .setParameter("searchRemarks", searchCondition.getRemarks())
//                            .getResultList();       
        
        System.out.println("<<< EmployeeList extract() END <<<");
        return em.createQuery(cq).getResultList();
                
    }
    
    public String createEmployee()
    {
        return "employeeDetail?faces-redirect=true&mode=New";
    }
    
    public String gotoDetail(Integer employeeId, String mode) throws UnsupportedEncodingException
    {
        System.out.println(String.format(">>> EmployeeList gotoDetail(%s,%s) BEGIN >>>", employeeId, mode));
        
        TEmployee tEmployee = em.find(TEmployee.class, employeeId);
        if(tEmployee == null) {
            return "";
        }
        
        System.out.println(String.format("<<< EmployeeList gotoDetail(%s,%s) END <<<", employeeId, mode));
        return "employeeDetail?faces-redirect=true&employee_id=" + employeeId + "&mode=" + URLEncoder.encode(mode, "UTF-8");
    }
    
    public String search()
    {
        return "";
    }
}
