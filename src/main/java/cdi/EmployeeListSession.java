package cdi;

import cdi.base.EntityListSession;
import entity.TEmployee;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import jsf.type.JsfUIOrderType;
import jsf.ui.annotation.JsfUIListColumnOrder;
import jsf.ui.annotation.dynamic.DynJsfUIListColumnOrder;
import misc.JsfUIColumnSetting;

/**
 *
 * @author Owner
 */
@Named
@SessionScoped
public class EmployeeListSession extends EntityListSession<TEmployee> {

    @Override
    public Class<TEmployee> modelClass() {
        return TEmployee.class;
    }

}
