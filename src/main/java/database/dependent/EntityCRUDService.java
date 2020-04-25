package database.dependent;

import java.io.Serializable;
import java.util.function.Consumer;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Entity;

/**
 *
 * @author owner
 */
@Dependent
public class EntityCRUDService<E extends Serializable, PK extends Serializable> implements Serializable {
    @PersistenceContext
    private EntityManager em;
    
    public void create(Consumer<E> creater, Class<E> entityClazz) throws InstantiationException, IllegalAccessException {
        E newEntity = entityClazz.newInstance();
        creater.accept(newEntity);
        em.persist(newEntity);
        em.flush();
        em.clear();
    }

    public E find(PK entityId, Class<E> entityClazz) {
        E foundEntity = em.find(entityClazz, entityId);
        em.clear();
        return foundEntity;
    }

    public int update(PK entityId, Consumer<E> updater, Class<E> entityClazz) {
        E foundEntity = em.find(entityClazz, entityId);
        if(foundEntity==null) {
            em.clear();
            return 0;
        }
        updater.accept(foundEntity);    //管理状態のエンティティをupdater内でsetterによる値設定を行えば, UPDATE文が発行される
        em.flush();
        em.clear();
        return 1;
    }

    public int delete(PK entityId, Class<E> entityClazz) {
        E entity = em.find(entityClazz, entityId);
        if(entity==null) {
            em.clear();
            return 0;
        }
        em.remove(entity);
        em.flush();
        em.clear();
        return 1;
    }

    public int deleteAll(Class<E> entityClazz) {
        int deleteCount = em.createQuery("DELETE FROM " + entityName(entityClazz)).executeUpdate();
        return deleteCount;
    }

    private String entityName(Class<E> entityClazz)
    {
        Entity entity = entityClazz.getDeclaredAnnotation(Entity.class);
        return entity.name().isEmpty() 
                    ? entity.name() 
                    : entityClazz.getSimpleName();
    }
}
