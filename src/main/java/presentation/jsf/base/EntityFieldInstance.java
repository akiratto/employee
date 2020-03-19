package presentation.jsf.base;

import java.io.Serializable;
import java.lang.reflect.Field;
import presentation.jsf.converter.UIColumnConverter;

/**
 *
 * @author Owner
 */
public class EntityFieldInstance<E extends Serializable>
        extends EntityFieldDefinition<E>
{
    private final E entityInstance;
    
    public EntityFieldInstance(Field entityField, E entityInstance)
    {
        super(entityField);
        this.entityInstance = entityInstance;
    }
    
    public String getValueAsString() throws InstantiationException, IllegalAccessException
    {
        Object value = getValue();
        UIColumnConverter uiColumnConverter = getJsfUIListColumnConverter();
        return uiColumnConverter != null
                    ? uiColumnConverter.convertToUIColumnValue(value)
                    : "";
    }

    public Object getValue()
    {
        try {
            boolean tmpAccessible = entityField.isAccessible();
            entityField.setAccessible(true);
            Object value = entityField.get(entityInstance);
            entityField.setAccessible(tmpAccessible);
            return value;
        } catch(IllegalAccessException | IllegalArgumentException ex) {
            return null;
        }
    }

    public void setValue(Object value) 
    { 
        try {
            boolean tmpAccessible = entityField.isAccessible();
            entityField.setAccessible(true);
            entityField.set(entityInstance,value); 
            entityField.setAccessible(tmpAccessible);
        } catch(IllegalAccessException | IllegalArgumentException ex) {

        }
    }
}
