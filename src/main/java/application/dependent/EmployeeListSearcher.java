package application.dependent;

import application.base.EntityListSearcher;
import javax.enterprise.context.Dependent;
import presentation.jsf.entity.JsfEmployee;

/**
 *
 * @author Owner
 */
@Dependent
public class EmployeeListSearcher extends EntityListSearcher<JsfEmployee> {

    @Override
    protected Class<JsfEmployee> entityClazz() {
        return JsfEmployee.class;
    }
    
}
