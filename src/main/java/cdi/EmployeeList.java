package cdi;

import cdi.base.EntityListBase;
import cdi.base.EntityListSessionBase;
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
public class EmployeeList extends EntityListBase<TEmployee, Integer> {   

    @Inject
    private EmployeeListSession employeeListSession;
    
    @Override
    public Class<TEmployee> modelClass() {
        return TEmployee.class;
    }

    @Override
    public EntityListSessionBase modelListSession() {
        return employeeListSession;
    }
}
