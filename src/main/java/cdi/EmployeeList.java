package cdi;

import cdi.base.EntityListUI;
import cdi.base.EntityListSession;
import entity.database.EmployeeTable;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author owner
 */
@Named
@ViewScoped
public class EmployeeList extends EntityListUI<EmployeeTable, Integer> {   

    @Inject
    private EmployeeListSession employeeListSession;
    
    @Override
    public Class<EmployeeTable> modelClass() {
        return EmployeeTable.class;
    }

    @Override
    public EntityListSession modelListSession() {
        return employeeListSession;
    }
}
