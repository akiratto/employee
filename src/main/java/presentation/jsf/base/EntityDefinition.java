package presentation.jsf.base;

import application.reflection.EntityReflectionBean;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import presentation.jsf.annotation.JsfUIId;

/**
 *
 * @author Owner
 */
public abstract class EntityDefinition<E extends Serializable> {
    abstract public Class<E> modelClass();
    
    public List<EntityFieldDefinition<E>> getEntityFieldDefinitions()
    {
        return getDeclaredFieldsByStream()
                .map(field -> new EntityFieldDefinition<E>(field))
                .collect(Collectors.toList());
    }
    
    public <E extends Serializable> EntityFieldDefinition<E> getJsfUIIdFieldDefinition(E entity)
    {
        return getDeclaredFieldsByStream()
                .map(field -> new EntityFieldDefinition<E>(field))
                .filter(fieldDefinition -> fieldDefinition.hasJsfUIId())
                .findFirst()
                .orElse(null);
    }
    
    public <E extends Serializable> EntityFieldDefinition<E> getJsfUIInternalIdFieldDefinition(E entity)
    {
        return getDeclaredFieldsByStream()
                .map(field -> new EntityFieldDefinition<E>(field))
                .filter(fieldDefinition -> fieldDefinition.hasJsfUIInternalId())
                .findFirst()
                .orElse(null);
    }
        
    protected Stream<Field> getDeclaredFieldsByStream()
    {
        return Arrays.asList( modelClass().getDeclaredFields() )
                        .stream()
                        .filter(field -> !field.getName().startsWith("_"))
                        .filter(field -> !field.getName().equals("serialVersionUID"));
    }
}
