package application;

import application.base.EntityList;
import application.base.EntityListSession;
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
    private EmployeeListSession employeeListSession;
    
    @Override
    public Class<TableEmployee> modelClass() {
        return TableEmployee.class;
    }

    @Override
    public EntityListSession modelListSession() {
        return employeeListSession;
    }
}
