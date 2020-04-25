package application;

import application.base.EntityList;
import presentation.jsf.base.JsfEntityListSession;
import database.entity.TableEmployee;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import presentation.jsf.entity.JsfEmployee;

/**
 *
 * @author owner
 */
@Named
@ViewScoped
public class EmployeeList extends EntityList<JsfEmployee, TableEmployee, Integer> {   

    @Override
    protected Class<JsfEmployee> jsfEntityClass() {
        return JsfEmployee.class;
    }

    @Override
    public Class<TableEmployee> tableEntityClass() {
        return TableEmployee.class;
    }

}
