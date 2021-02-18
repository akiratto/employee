package ui;

import model.PageNavigator;
import model.EmployeeListService;
import ui.employeelist.EmployeeSearch;
import entity.TEmployee;
import entity.type.Gender;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    
    @Inject
    private EmployeeListService employeeListService;
    
    @Inject
    private EmployeeSearch employeeSearch;
    
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

        Long               employeeAllCount   = employeeListService.countEmployeeAllCount(searchCondition);
        Map<String,String> searchParameterMap = employeeSearch.generateSearchParameterMap(searchCondition);
        pageNavigator.build(employeeAllCount, searchParameterMap);
        
        List<TEmployee> employeeList = employeeListService.extractEmployees(searchCondition, 
                                                                          employeeAllCount, 
                                                                          pageNavigator.getOffset(), 
                                                                          pageNavigator.getRowCountPerPage());
        
        this.employeeAllCount = employeeAllCount;
        this.employeeList     = employeeList;

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

    public PageNavigator getPageNavigator() {
        return pageNavigator;
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
        String queryString = employeeSearch.generateSearchURLQueryString(searchCondition);
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
        String queryString = employeeSearch.generateSearchURLQueryString(searchCondition);
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
}
