package cdi;

import entity.TEmployee;
import entity.TEmployee_;
import entity.type.Gender;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.joining;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

/**
 *
 * @author owner
 */
@Named
@ViewScoped
public class EmployeeList implements Serializable {
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private PageNavigator pageNavigator;
    
    private TEmployee searchCondition = new TEmployee();
    private List<TEmployee> employeeList = new ArrayList<>();
    private Long employeeAllCount = 0L;
    
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
    
    public void viewAction()
    {
        System.out.println(">>> EmployeeList viewAction() BEGIN >>>");
        
        //検索条件を基に抽出処理を実行する -------------------------------------
        CriteriaBuilder build = em.getCriteriaBuilder();
        CriteriaQuery<Long> cqCount = build.createQuery(Long.class);
        CriteriaQuery<TEmployee> cq = build.createQuery(TEmployee.class);
        Root<TEmployee> root = cq.from(TEmployee.class);
        Root<TEmployee> rootCount = cqCount.from(TEmployee.class);
        Predicate where2 = build.conjunction();
        Predicate where1 = build.conjunction();
        if(searchCondition.getEmployeeCode()!=null) {
            where1 = build.and(where1, build.equal(
                                        rootCount.get(TEmployee_.employeeCode), 
                                        searchCondition.getEmployeeCode()
            ));
            where2 = build.and(where2, build.equal(
                                        root.get(TEmployee_.employeeCode), 
                                        searchCondition.getEmployeeCode()
            ));
        }
        if(searchCondition.getName()!=null && !searchCondition.getName().isEmpty()) {
            where1 = build.and(where1, build.like(
                                        rootCount.get(TEmployee_.name), 
                                        "%" + likeEscape(searchCondition.getName()) + "%", '\\'
            ));
            where2 = build.and(where2, build.like(
                                        root.get(TEmployee_.name), 
                                        "%" + likeEscape(searchCondition.getName()) + "%", '\\'
            ));
        }
        if(searchCondition.getGender()!=null) {
            where1 = build.and(where1, build.equal(
                                        rootCount.get(TEmployee_.gender), 
                                        searchCondition.getGender()
            ));
                    
            where2 = build.and(where2, build.equal(
                                        root.get(TEmployee_.gender), 
                                        searchCondition.getGender()
            ));
        }
        if(searchCondition.getBirthday()!=null) {
            where1 = build.and(where1, build.equal(rootCount.get(TEmployee_.birthday), searchCondition.getBirthday()));
            where2 = build.and(where2, build.equal(root.get(TEmployee_.birthday), searchCondition.getBirthday()));
        }
        if(searchCondition.getPhone()!=null && !searchCondition.getPhone().isEmpty()) {
            where1 = build.and(where1, build.like(
                                        rootCount.get(TEmployee_.phone), 
                                        "%" + likeEscape(searchCondition.getPhone()) + "%", '\\'
            ));
            where2 = build.and(where2, build.like(
                                        root.get(TEmployee_.phone), 
                                        "%" + likeEscape(searchCondition.getPhone()) + "%", '\\'
            ));
        }
        if(searchCondition.getMobilePhone()!=null && !searchCondition.getMobilePhone().isEmpty()) {
            where1 = build.and(where1, build.like(
                                        rootCount.get(TEmployee_.mobilePhone), 
                                        "%" + likeEscape(searchCondition.getMobilePhone()) + "%", '\\'
            ));
            where2 = build.and(where2, build.like(
                                        root.get(TEmployee_.mobilePhone), 
                                        "%" + likeEscape(searchCondition.getMobilePhone()) + "%", '\\'
            ));
        }
        if(searchCondition.getZipCode()!=null && !searchCondition.getZipCode().isEmpty()) {
            where1 = build.and(where1, build.like(
                                        rootCount.get(TEmployee_.zipCode), 
                                        "%" + likeEscape(searchCondition.getZipCode()) + "%", '\\'
            ));
            where2 = build.and(where2, build.like(
                                        root.get(TEmployee_.zipCode), 
                                        "%" + likeEscape(searchCondition.getZipCode()) + "%", '\\'
            ));
        }
        if(searchCondition.getAddress()!=null && !searchCondition.getAddress().isEmpty()) {
            where1 = build.and(where1, build.like(
                                        rootCount.get(TEmployee_.address), 
                                        "%" + likeEscape(searchCondition.getAddress()) + "%", '\\'
            ));
            where2 = build.and(where2, build.like(
                                        root.get(TEmployee_.address), 
                                        "%" + likeEscape(searchCondition.getAddress()) + "%", '\\'
            ));
        }
        if(searchCondition.getRemarks()!=null && !searchCondition.getRemarks().isEmpty()) {
            where1 = build.and(where1, build.like(
                                        rootCount.get(TEmployee_.remarks), 
                                        "%" + likeEscape(searchCondition.getRemarks()) + "%", '\\'
            ));
            where2 = build.and(where2, build.like(
                                        root.get(TEmployee_.remarks), 
                                        "%" + likeEscape(searchCondition.getRemarks()) + "%", '\\'
            ));
        }        
        cqCount = cqCount.select(build.count(rootCount)).where(where1);
        this.employeeAllCount = em.createQuery(cqCount).getSingleResult();
        
        this.pageNavigator.build(this.employeeAllCount, this.generateQueryStrings(this.searchCondition));
        
        cq = cq.select(root)
                .where(where2)
                .orderBy(build.asc(root.get(TEmployee_.employeeCode)));
        this.employeeList = em.createQuery(cq)
                                .setFirstResult(this.pageNavigator.getOffset())
                                .setMaxResults(this.pageNavigator.getRowCountPerPage())
                                .getResultList();
        
        System.out.println("<<< EmployeeList viewAction() END <<<");                
    }

    public TEmployee getSearchCondition() {
        return searchCondition;
    }

    public List<TEmployee> getEmployeeList() {
        return employeeList;
    }
    
    public int getEmployeeCount() {
        return employeeList.size();
    }

    public Long getEmployeeAllCount() {
        return employeeAllCount;
    }

    public String createEmployee()
    {
        return "employeeDetail?faces-redirect=true&mode=New";
    }
    
    public String batchEmployee()
    {
        return "employeeBatch?faces-redirect=true";
    }
    
    @Transactional
    public String deleteEmployee(Integer employeeId) throws UnsupportedEncodingException
    {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.setKeepMessages(true);    //リダイレクト後もFacesMessageが保持されるよう設定する
        
        TEmployee employee = em.find(TEmployee.class, employeeId);
        if(employee == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("削除する社員情報が見つかりません。"));
            return search();
        }
        em.remove(employee);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("社員コード:" + employee.getEmployeeCode() + "を削除しました。"));
        return search();
    }
    
    @Transactional
    public String deleteAllEmployee()
    {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.setKeepMessages(true);    //リダイレクト後もFacesMessageが保持されるよう設定する
        int deleteCount = em.createQuery("DELETE FROM TEmployee").executeUpdate();
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(deleteCount + "件の社員情報を削除しました。"));
        String queryString = generateString(searchCondition);
        return "employeeList?faces-redirect=true" + (queryString.isEmpty() ? "" : "&" + queryString);
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
       
    public String search() throws UnsupportedEncodingException
    {
        String queryString = generateString(searchCondition);        
        return "employeeList?faces-redirect=true" + (queryString.isEmpty() ? "" : "&" + queryString);
    }
    
    public String clear()
    {
        return "employeeList?faces-redirect=true";
    }
    
    public List<SelectItem> getGenders()
    {
        return Arrays.asList(Gender.values())
                    .stream()
                    .map(g -> new SelectItem(g.name(), g.jpName()))
                    .collect(Collectors.toList());
    }
    
    private Map<String,String> generateQueryStrings(TEmployee searchCondition)
    {
        Map<String, String> queryStrings = new HashMap<>();
        if(searchCondition.getEmployeeCode()!=null) {
            queryStrings.put("employee_code",searchCondition.getEmployeeCode().toString());
        }
        if(searchCondition.getName()!=null && !searchCondition.getName().isEmpty()) {
            queryStrings.put("name",searchCondition.getName());
        }
        if(searchCondition.getGender()!=null) {
            queryStrings.put("gender",searchCondition.getGender().name());
        }
        if(searchCondition.getBirthday()!=null) {
            SimpleDateFormat birthdayFormat = new SimpleDateFormat("yyyy/MM/dd");
            queryStrings.put("birthday", birthdayFormat.format(searchCondition.getBirthday()));
        }
        if(searchCondition.getPhone()!=null && !searchCondition.getPhone().isEmpty()) {
            queryStrings.put("phone",searchCondition.getPhone());
        }
        if(searchCondition.getMobilePhone()!=null && !searchCondition.getMobilePhone().isEmpty()) {
            queryStrings.put("mobile_phone",searchCondition.getMobilePhone());
        }
        if(searchCondition.getZipCode()!=null && !searchCondition.getZipCode().isEmpty()) {
            queryStrings.put("zip_code",searchCondition.getZipCode());
        }
        if(searchCondition.getAddress()!=null && !searchCondition.getAddress().isEmpty()) {
            queryStrings.put("address",searchCondition.getAddress());
        }
        if(searchCondition.getRemarks()!=null && !searchCondition.getRemarks().isEmpty()) {
            queryStrings.put("remarks",searchCondition.getRemarks());
        }
        return queryStrings;
    }
    
    private String generateString(TEmployee searchCondition)
    {
        return generateQueryStrings(searchCondition)
                .entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + urlEncode(e.getValue()))
                .collect(joining("&"));
    }

    private String likeEscape(String likeCondition)
    {
        return likeCondition
                .replaceAll("_", "\\\\_")
                .replaceAll("%", "\\\\%");
    }
    
    private String urlEncode(String target)
    {
        try {
            return URLEncoder.encode(target, "UTF-8");
        } catch(UnsupportedEncodingException ex) {
            return "";
        }
    }
}
