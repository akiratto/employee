package presentation.jsf;

import presentation.jsf.base.JsfEntityListSession;
import database.entity.TableEmployee;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import application.type.OrderType;
import presentation.jsf.annotation.JsfUIListColumnOrder;
import presentation.jsf.annotation.dynamic.DynJsfUIListColumnOrder;
import presentation.jsf.type.JsfUIColumnSetting;

/**
 *
 * @author Owner
 */
@Named
@SessionScoped
public class JsfEmployeeListSession extends JsfEntityListSession<TableEmployee> {

    @Override
    public Class<TableEmployee> modelClass() {
        return TableEmployee.class;
    }

}
