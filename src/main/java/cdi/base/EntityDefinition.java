package cdi.base;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    
    protected Stream<Field> getDeclaredFieldsByStream()
    {
        return Arrays.asList( modelClass().getDeclaredFields() )
                        .stream()
                        .filter(field -> !field.getName().startsWith("_"))
                        .filter(field -> !field.getName().equals("serialVersionUID"));
    }
}
