/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cdi.base;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Owner
 */
public abstract class EntityInstance<E extends Serializable>
        extends EntityDefinition<E>
{
    abstract public E modelInstance();
    
    public List<EntityFieldInstance<E>> getEntityFieldInstances()
    {
        return getDeclaredFieldsByStream()
                .map(field -> new EntityFieldInstance<>(field, modelInstance()))
                .collect(Collectors.toList());
    }
    
    
    public EntityFieldInstance<E> getJsfUIIdFieldInstance()
    {
        return getDeclaredFieldsByStream()
                .map(field -> new EntityFieldInstance<>(field, modelInstance()))
                .filter(fieldInstance -> fieldInstance.hasJsfUIId())
                .findFirst()
                .orElse(null);
    }
    
    public EntityFieldInstance<E> getJsfUIInternalIdFieldInstance()
    {
        return getDeclaredFieldsByStream()
                .map(field -> new EntityFieldInstance<>(field, modelInstance()))
                .filter(fieldInstance -> fieldInstance.hasJsfUIInternalId())
                .findFirst()
                .orElse(null);
    }
}
