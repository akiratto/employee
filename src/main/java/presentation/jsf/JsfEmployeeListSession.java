package presentation.jsf;

import presentation.jsf.base.JsfEntityListSession;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import presentation.jsf.entity.JsfEmployee;

/**
 *
 * @author Owner
 */
@Named
@SessionScoped
public class JsfEmployeeListSession extends JsfEntityListSession<JsfEmployee> {

    @Override
    public Class<JsfEmployee> modelClass() {
        return JsfEmployee.class;
    }

}
