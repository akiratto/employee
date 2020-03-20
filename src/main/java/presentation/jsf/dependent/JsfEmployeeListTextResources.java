package presentation.jsf.dependent;

import javax.enterprise.context.Dependent;
import presentation.jsf.base.JsfEntityListTextResources;
import presentation.jsf.entity.JsfEmployee;

/**
 *
 * @author Owner
 */
@Dependent
public class JsfEmployeeListTextResources extends JsfEntityListTextResources<JsfEmployee> {

    @Override
    public Class<JsfEmployee> entityClazz() {
        return JsfEmployee.class;
    }
    
}
