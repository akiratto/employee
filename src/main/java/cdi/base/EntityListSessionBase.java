package cdi.base;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import jsf.type.JsfUIOrderType;
import jsf.ui.annotation.JsfUIListColumnOrder;
import jsf.ui.annotation.dynamic.DynJsfUIListColumnOrder;
import misc.JsfUIColumnSetting;

/**
 *
 * @author Owner
 */
public abstract class EntityListSessionBase<E extends Serializable> implements Serializable {
    Map<String,JsfUIColumnSetting> fieldSetting = new HashMap<>();
    
    abstract public Class<E> modelClass();
    
    @PostConstruct
    public void init()
    {
        for(Field field : modelClass().getDeclaredFields()) {
            String fieldName = field.getName();
            JsfUIListColumnOrder jsfUIListColumnOrder = field.getAnnotation(JsfUIListColumnOrder.class);
            
            JsfUIColumnSetting jsfUIColumnSetting = new JsfUIColumnSetting(fieldName, jsfUIListColumnOrder);
            fieldSetting.put(fieldName, jsfUIColumnSetting);
        }
    }
    
    public JsfUIColumnSetting getUIColumnSetting(String fieldName)
    {
        return fieldSetting.get(fieldName);
    }
    
    public Stream<JsfUIColumnSetting> getUIColumnSettings()
    {
        return fieldSetting.entrySet().stream()
                    .map(entry -> entry.getValue());
    }
    
    public void sortAscending()
    {
        String fieldName = "employeeCode";
        JsfUIListColumnOrder newOrder = new DynJsfUIListColumnOrder(JsfUIOrderType.ASCENDING, 1);
        JsfUIColumnSetting newEmployeeCodeUIColumnSetting = new JsfUIColumnSetting(fieldName, newOrder);
        
        fieldSetting.put("employeeCode", newEmployeeCodeUIColumnSetting);
    }
    
    public void sortDescending()
    {
        String fieldName = "employeeCode";
        JsfUIListColumnOrder newOrder = new DynJsfUIListColumnOrder(JsfUIOrderType.DESCENDING, 1);
        JsfUIColumnSetting newEmployeeCodeUIColumnSetting = new JsfUIColumnSetting(fieldName, newOrder);
        
        fieldSetting.put("employeeCode", newEmployeeCodeUIColumnSetting);
    }
}
