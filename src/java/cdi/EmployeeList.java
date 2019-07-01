package cdi;

import entity.TEmployee;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author owner
 */
@Named
@ViewScoped
public class EmployeeList implements Serializable {
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
        
        CriteriaBuilder build = em.getCriteriaBuilder();
        CriteriaQuery<TEmployee> cq = build.createQuery(TEmployee.class);
        Root<TEmployee> root = cq.from(TEmployee.class);
        Predicate where = build.conjunction();
        
        if(searchCondition.getEmployee_id()!=null) {
            where = build.and(where, build.equal(
                                        root.get(TEmployee_.employee_id), 
                                        searchCondition.getEmployee_id()
            ));
        }
        if(searchCondition.getName()!=null && !searchCondition.getName().isEmpty()) {
            where = build.and(where, build.like(
                                        root.get(TEmployee_.name), 
                                        "%" + likeEscape(searchCondition.getName()) + "%"
            ));
        }
        if(searchCondition.getGender()!=null && !searchCondition.getGender().isEmpty()) {
            where = build.and(where, build.like(
                                        root.get(TEmployee_.gender), 
                                        "%" + likeEscape(searchCondition.getGender()) + "%", '\\'
            ));
        }
        if(searchCondition.getPhone()!=null && !searchCondition.getPhone().isEmpty()) {
            where = build.and(where, build.like(
                                        root.get(TEmployee_.phone), 
                                        "%" + likeEscape(searchCondition.getPhone()) + "%"
            ));
        }
        if(searchCondition.getMobilePhone()!=null && !searchCondition.getMobilePhone().isEmpty()) {
            where = build.and(where, build.like(
                                        root.get(TEmployee_.mobilePhone), 
                                        "%" + likeEscape(searchCondition.getMobilePhone()) + "%"
            ));
        }
        if(searchCondition.getZipCode()!=null && !searchCondition.getZipCode().isEmpty()) {
            where = build.and(where, build.like(
                                        root.get(TEmployee_.zipCode), 
                                        "%" + likeEscape(searchCondition.getZipCode()) + "%"
            ));
        }
        if(searchCondition.getAddress()!=null && !searchCondition.getAddress().isEmpty()) {
            where = build.and(where, build.like(
                                        root.get(TEmployee_.address), 
                                        "%" + likeEscape(searchCondition.getAddress()) + "%"
            ));
        }
        if(searchCondition.getRemarks()!=null && !searchCondition.getRemarks().isEmpty()) {
            where = build.and(where, build.like(
                                        root.get(TEmployee_.remarks), 
                                        "%" + likeEscape(searchCondition.getRemarks()) + "%"
            ));
        }        
        cq = cq.select(root).where(where);
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
        return "employeeList?faces-redirect=true";
//        return "";
    }

    private String likeEscape(String likeCondition)
    {
        return likeCondition
                .replaceAll("_", "\\\\_")
                .replaceAll("%", "\\\\%");
    }
}
