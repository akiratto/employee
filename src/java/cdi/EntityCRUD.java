package cdi;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import jsf.ui.annotation.JsfUIModel;

/**
 *
 * @author owner
 */
@Dependent
public class EntityCRUD<E extends Serializable, PK extends Serializable> implements Serializable {
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

    public int update(Integer entityId, Consumer<E> updater, Class<E> entityClazz) {
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
        JsfUIModel jsfUIModel = entityClazz.getDeclaredAnnotation(JsfUIModel.class);
        String entityName = jsfUIModel.modelName();
        int deleteCount = em.createQuery("DELETE FROM " + entityName).executeUpdate();
        return deleteCount;
    }

    public Long countAll(E condition,  Class<E> entityClazz) 
            throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException 
    {
        List<String> jpqlWords = new ArrayList<>();
        jpqlWords.add("SELECT");
        jpqlWords.add("count(t)");
        jpqlWords.add(constructJPQLFrom(entityClazz));
        jpqlWords.add(constructJPQLWhere(condition, entityClazz));
        String jpql = String.join(" ", jpqlWords);
        
        return em.createQuery(jpql, Long.class).getSingleResult();    
    }

    public List<E> search(E condition, int offset, int rowCountPerPage, Class<E> entityClass) 
            throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException 
    {
        List<String> jpqlWords = new ArrayList<>();
        jpqlWords.add("SELECT");
        jpqlWords.add("t");
        jpqlWords.add(constructJPQLFrom(entityClass));
        jpqlWords.add(constructJPQLWhere(condition, entityClass));
        String jpql = String.join(" ", jpqlWords);
        
        TypedQuery<E> query = em.createQuery(jpql, entityClass);
        for(Field field : entityClass.getDeclaredFields()) {
            //JPAのEntityは、開発者が定義したフィールドとは別に
            //JPA側で自動で名前の先頭に_(アンダーバー)が付くフィールドと
            //serialVersionUIDフィールドが付加する。
            //これらをフィールドは処理しない。
            if(field.getName().startsWith("_")) continue;
            if(field.getName().equals("serialVersionUID")) continue;
            
            Object value = null;
            boolean tmpAccessible = field.isAccessible();
            field.setAccessible(true);
            value = field.get(condition);
            field.setAccessible(tmpAccessible);
            
            if(value != null) {
                String searchMethod = "equal";
                switch(searchMethod) {
                    case "equal":
                        query.setParameter(field.getName(), value);
                        break;
                        
                    case "include":
                        query.setParameter(field.getName(), likeEscape((String)value));
                        break;
                }
            }
        }
        return query.setFirstResult(offset)
                    .setMaxResults(rowCountPerPage)
                    .getResultList();
    }
    
    private String constructJPQLFrom(Class<E> entityClass)
    {
        return "FROM " + entityClass.getName() + " t";
    }
    
    private String constructJPQLWhere(E condition, Class<E> entityClass) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException
    {
        List<String> wherePhrases = new ArrayList<>();
        
        for(Field field : entityClass.getDeclaredFields()) {
            //JPAのEntityは、開発者が定義したフィールドとは別に
            //JPA側で自動で名前の先頭に_(アンダーバー)が付くフィールドと
            //serialVersionUIDフィールドが付加する。
            //これらをフィールドは処理しない。
            if(field.getName().startsWith("_")) continue;
            if(field.getName().equals("serialVersionUID")) continue;
            
            Object value = null;
            
            boolean tmpAccessible = field.isAccessible();
            field.setAccessible(true);
            value = field.get(condition);
            field.setAccessible(tmpAccessible);
            
            String searchMethod = "equal";
            
            if(value != null) {
                String wherePhrase;
                switch(searchMethod) {
                    case "equal":
                        wherePhrase = "t." + field.getName() + "= :" + field.getName(); 
                        wherePhrases.add(wherePhrase);
                        break;
                        
                    case "include":
                        wherePhrase = "t." + field.getName() + " like :"  + field.getName();
                        wherePhrases.add(wherePhrase);
                        break;
                        
                    default:
                        break;
                }

            }
        }
        return wherePhrases.size() > 0 
                ? "WHERE " + String.join(" and ", wherePhrases) : "";
    }

    private String likeEscape(String likeCondition)
    {
        return likeCondition
                .replaceAll("_", "\\\\_")
                .replaceAll("%", "\\\\%");
    }    
}
