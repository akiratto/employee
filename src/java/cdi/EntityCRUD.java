package cdi;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import jsf.type.JsfUISearchMethodType;
import jsf.ui.annotation.JsfUIListColumnConverter;
import jsf.ui.annotation.JsfUIModel;
import jsf.ui.annotation.JsfUISearchColumn;
import jsf.ui.annotation.JsfUISearchColumnConverter;
import jsf.ui.converter.UIColumnConverter;
import util.Tuple;

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

    public Long countAll(E condition,  Class<E> entityClass) 
//            throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException 
    {
        List<String> jpqlWords = new ArrayList<>();
        jpqlWords.add("SELECT");
        jpqlWords.add("count(t)");
        jpqlWords.add(constructJPQLFrom(entityClass));
        jpqlWords.add(constructJPQLWhere(condition, entityClass));
        String jpql = String.join(" ", jpqlWords);
        
        Function<EntityField, Tuple<String,Object>> function = entityField -> {
            Tuple<String,Object> nameAndValue = new Tuple<>();
            if(entityField.fieldValue != null) {
                
                switch(entityField.jsfUISearchMethodType) {
                    case SEARCH_METHOD_EQUAL:
                        nameAndValue._1 = entityField.fieldName;
                        nameAndValue._2 = entityField.fieldValue;
                        break;
                        
                    case SEARCH_METHOD_INCLUDE:
                        nameAndValue._1 = entityField.fieldName;
                        nameAndValue._2 = likeEscape(entityField.fieldValueAsStringInUISearchColumn);
                        break;
                }
            }
            return nameAndValue;
        };
        
        final TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        mapEntityFields(condition, entityClass, function)
                .stream()
                .filter(nameAndValue -> nameAndValue._1 != null)
                .peek(nameAndValue -> System.out.println(nameAndValue._1 + " " + nameAndValue._2 + " "  + nameAndValue._2.getClass()))
                .forEach(nameAndValue -> query.setParameter(nameAndValue._1, nameAndValue._2));
        
        return query.getSingleResult();    
    }

    public List<E> search(E condition, int offset, int rowCountPerPage, Class<E> entityClass) 
    {
        List<String> jpqlWords = new ArrayList<>();
        jpqlWords.add("SELECT");
        jpqlWords.add("t");
        jpqlWords.add(constructJPQLFrom(entityClass));
        jpqlWords.add(constructJPQLWhere(condition, entityClass));
        String jpql = String.join(" ", jpqlWords);
        
        Function<EntityField, Tuple<String,Object>> function = entityField -> {
            Tuple<String,Object> nameAndValue = new Tuple<>();
            if(entityField.fieldValue != null) {
                
                switch(entityField.jsfUISearchMethodType) {
                    case SEARCH_METHOD_EQUAL:
                        nameAndValue._1 = entityField.fieldName;
                        nameAndValue._2 = entityField.fieldValue;
                        break;
                        
                    case SEARCH_METHOD_INCLUDE:
                        nameAndValue._1 = entityField.fieldName;
                        nameAndValue._2 = likeEscape(entityField.fieldValueAsStringInUISearchColumn);
                        break;
                }
            }
            return nameAndValue;
        };
        
        final TypedQuery<E> query = em.createQuery(jpql, entityClass);
        mapEntityFields(condition, entityClass, function)
                .stream()
                .filter(nameAndValue -> nameAndValue._1 != null)
                .forEach(nameAndValue -> query.setParameter(nameAndValue._1, nameAndValue._2));
        return query.setFirstResult(offset)
                    .setMaxResults(rowCountPerPage)
                    .getResultList();
    }
    
    private String constructJPQLFrom(Class<E> entityClass)
    {
        return "FROM " + entityClass.getName() + " t";
    }
    
    private static class EntityField
    {
        public String fieldName;
        public Object fieldValue;
        public String fieldValueAsStringInUISearchColumn;
        JsfUISearchColumn jsfUISearchColumn;
        JsfUISearchMethodType jsfUISearchMethodType;
        JsfUISearchColumnConverter jsfUISearchColumnConverter;
    }
    private <R> List<R> mapEntityFields(E entity, Class<E> entityClass, Function<EntityField,R> function)
    {
        List<R> result = new ArrayList<>();
        
        for(Field field : entityClass.getDeclaredFields()) {
            //JPAのEntityは、開発者が定義したフィールドとは別に
            //JPA側で自動で名前の先頭に_(アンダーバー)が付くフィールドと
            //serialVersionUIDフィールドが付加する。
            //これらをフィールドは処理しない。
            if(field.getName().startsWith("_")) continue;
            if(field.getName().equals("serialVersionUID")) continue;
            
            JsfUISearchColumn jsfUISearchColumn = field.getAnnotation(JsfUISearchColumn.class);
            if(jsfUISearchColumn==null) continue;
            
            JsfUISearchMethodType jsfUISearchMethodType = jsfUISearchColumn.searchMethodType();
            JsfUISearchColumnConverter jsfUISearchColumnConverter = field.getAnnotation(JsfUISearchColumnConverter.class);
            
            UIColumnConverter uiSearchColumnConverter = null;
            try {
                if(jsfUISearchColumnConverter != null) {
                    uiSearchColumnConverter = (UIColumnConverter)jsfUISearchColumnConverter.converter().newInstance();
                }
            } catch (InstantiationException ex) {
                Logger.getLogger(EntityCRUD.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(EntityCRUD.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            Object fieldValueAsObject = null;
            try {
                boolean tmpAccessible = field.isAccessible();
                field.setAccessible(true);
                fieldValueAsObject = field.get(entity);
                field.setAccessible(tmpAccessible);
            } catch (IllegalAccessException ex) {
                
            } catch (IllegalArgumentException ex) {
                
            }
            EntityField entityField = new EntityField();
            entityField.fieldName = field.getName();
            entityField.fieldValue = fieldValueAsObject;
            entityField.fieldValueAsStringInUISearchColumn = fieldValueAsObject      == null ? "" 
                                                           : uiSearchColumnConverter == null ? fieldValueAsObject.toString()
                                                           : uiSearchColumnConverter.convertToUIColumnValue(fieldValueAsObject);
            entityField.jsfUISearchColumn = jsfUISearchColumn;
            entityField.jsfUISearchMethodType = jsfUISearchMethodType;
            entityField.jsfUISearchColumnConverter = jsfUISearchColumnConverter;
            result.add( function.apply(entityField) );
        }
        return result;
    }
    private String constructJPQLWhere(E condition, Class<E> entityClass) 
    {
        Function<EntityField,String> function = entityField -> {
            String conditionExpression = "";
            if(entityField.fieldValue != null) {
                switch(entityField.jsfUISearchMethodType) {
                    case SEARCH_METHOD_EQUAL:
                        conditionExpression = "t." + entityField.fieldName + "= :" + entityField.fieldName; 
                        break;

                    case SEARCH_METHOD_INCLUDE:
                        conditionExpression = "t." + entityField.fieldName + " like :"  + entityField.fieldName;
                        break;
                }
            }
            return conditionExpression;
        };
        
        String wherePhrase = mapEntityFields(condition, entityClass, function)
                                        .stream()
                                        .filter(conditionExpression -> !conditionExpression.isEmpty())
                                        .collect(Collectors.joining(" and "));
        return wherePhrase.isEmpty() == false 
                ? "WHERE " + wherePhrase : "";
    }
    
    private String likeEscape(String likeCondition)
    {
        return likeCondition
                .replaceAll("_", "\\\\_")
                .replaceAll("%", "\\\\%");
    }    
}
