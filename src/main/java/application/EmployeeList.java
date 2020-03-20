package application;

import presentation.jsf.JsfEmployeeListSession;
import application.base.EntityList;
import presentation.jsf.base.JsfEntityListSession;
import database.entity.TableEmployee;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author owner
 */
@Named
@ViewScoped
public class EmployeeList extends EntityList<TableEmployee, Integer> {   

    @Inject
    private JsfEmployeeListSession employeeListSession;
    
    @Override
    public Class<TableEmployee> modelClass() {
        return TableEmployee.class;
    }

    @Override
    public JsfEntityListSession modelListSession() {
        return employeeListSession;
    }
}
