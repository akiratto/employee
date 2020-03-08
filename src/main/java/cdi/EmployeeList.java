package cdi;

import cdi.base.EntityListUI;
import cdi.base.EntityListSession;
import entity.TEmployee;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author owner
 */
@Named
@ViewScoped
public class EmployeeList extends EntityListUI<TEmployee, Integer> {   

    @Inject
    private EmployeeListSession employeeListSession;
    
    @Override
    public Class<TEmployee> modelClass() {
        return TEmployee.class;
    }

    @Override
    public EntityListSession modelListSession() {
        return employeeListSession;
    }
}
