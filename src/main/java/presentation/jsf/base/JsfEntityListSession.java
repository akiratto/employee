package presentation.jsf.base;

import presentation.jsf.base.EntityDefinition;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import application.type.OrderType;
import presentation.jsf.annotation.JsfUIListColumnOrder;
import presentation.jsf.annotation.dynamic.DynJsfUIListColumnOrder;
import presentation.jsf.type.JsfUIColumnSetting;

/**
 *
 * @author Owner
 */
public abstract class JsfEntityListSession<JE extends Serializable>
        extends EntityDefinition<JE>
        implements Serializable 
{
    Map<String,JsfUIColumnSetting> fieldSetting = new HashMap<>();
    
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
        JsfUIListColumnOrder newOrder = new DynJsfUIListColumnOrder(OrderType.ASCENDING, 1);
        JsfUIColumnSetting newEmployeeCodeUIColumnSetting = new JsfUIColumnSetting(fieldName, newOrder);
        
        fieldSetting.put("employeeCode", newEmployeeCodeUIColumnSetting);
    }
    
    public void sortDescending()
    {
        String fieldName = "employeeCode";
        JsfUIListColumnOrder newOrder = new DynJsfUIListColumnOrder(OrderType.DESCENDING, 1);
        JsfUIColumnSetting newEmployeeCodeUIColumnSetting = new JsfUIColumnSetting(fieldName, newOrder);
        
        fieldSetting.put("employeeCode", newEmployeeCodeUIColumnSetting);
    }
}
