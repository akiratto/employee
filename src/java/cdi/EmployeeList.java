package cdi;

import entity.TEmployee;
import entity.type.Gender;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author owner
 */
@Named
@ViewScoped
public class EmployeeList extends EntityListBase<TEmployee, Integer> {   
    @Inject
    private PageNavigator pageNavigator;
    
    @Inject
    private EmployeeDbAction employeeDbAction;

    @Override protected Class<TEmployee> entityClazz() { return TEmployee.class; }
    @Override protected EntityDbAction<TEmployee, Integer> entityDbAction() { return employeeDbAction; }
    @Override protected PageNavigator pageNavigator() { return pageNavigator; }
    
    @Override public String entityName() { return "TEmployee"; }
    @Override public String entityTitle() { return "社員情報"; }

    @Override public String listPageName() { return "employeeList"; }
    @Override public String detailPageName() { return "employeeDetail"; }
    @Override public String createBatchPageName() { return "employeeBatch"; }
    
    public List<SelectItem> getGenders()
    {
        return Arrays.asList(Gender.values())
                    .stream()
                    .map(g -> new SelectItem(g.name(), g.jpName()))
                    .collect(Collectors.toList());
    }
}
