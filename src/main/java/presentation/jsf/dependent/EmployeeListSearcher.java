package presentation.jsf.dependent;

import presentation.jsf.base.JsfEntityListSearcher;
import javax.enterprise.context.Dependent;
import presentation.jsf.entity.JsfEmployee;

/**
 *
 * @author Owner
 */
@Dependent
public class EmployeeListSearcher extends JsfEntityListSearcher<JsfEmployee> {

    @Override
    protected Class<JsfEmployee> entityClazz() {
        return JsfEmployee.class;
    }
    
}
