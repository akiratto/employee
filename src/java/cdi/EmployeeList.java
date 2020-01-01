package cdi;

import cdi.base.EntityListBase;
import entity.TEmployee;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author owner
 */
@Named
@ViewScoped
public class EmployeeList extends EntityListBase<TEmployee, Integer> {   

    @Override protected Class<TEmployee> entityClass() { return TEmployee.class; }

}
