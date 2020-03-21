package presentation.jsf.base;

import database.dependent.EntityCRUDService;
import static database.type.JPQLOrderType.DESCENDING;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import presentation.jsf.annotation.JsfUIListColumnOrder;
import presentation.jsf.annotation.JsfUISearchColumn;
import presentation.jsf.annotation.JsfUISearchColumnConverter;
import presentation.jsf.base.JsfEntityListSession;
import presentation.jsf.converter.UIColumnConverter;
import presentation.jsf.type.JsfUIColumnSetting;
import presentation.jsf.type.JsfUISearchMethodType;
import util.Tuple;

/**
 *
 * @author Owner
 */
public abstract class JsfEntityListSearcher<JE extends Serializable> implements Serializable
{
    @PersistenceContext
    EntityManager em;
    
    abstract protected Class<JE> entityClazz();
    
    public List<JE> search(JE condition, 
                          int offset, 
                          int rowCountPerPage, 
                          JsfEntityListSession<JE> jsfEntityListSession) 
    {
        List<String> jpqlWords = new ArrayList<>();
        jpqlWords.add("SELECT");
        jpqlWords.add("t");
        jpqlWords.add(constructJPQLFrom());
        jpqlWords.add(constructJPQLWhere(condition));
        jpqlWords.add(constructJPQLOrderBy(condition, jsfEntityListSession));
        String jpql = jpqlWords.stream()
                               .filter(s -> !s.isEmpty())
                               .collect(Collectors.joining(" "));
        
        Function<EntityField, Tuple<String,Object>> function =   
                     entityField -> {
                    Tuple<String,Object> nameAndValue = new Tuple<>();
                    if(entityField.fieldValue != null) {

                        switch(entityField.jsfUISearchMethodType) {
                            case SEARCH_METHOD_EQUAL:
                                nameAndValue._1 = entityField.fieldName;
                                nameAndValue._2 = entityField.fieldValue;
                                break;

                            case SEARCH_METHOD_INCLUDE:
                                nameAndValue._1 = entityField.fieldName;
                                nameAndValue._2 = "%" + likeEscape(entityField.fieldValueAsStringInUISearchColumn) + "%";
                                break;
                        }
                    }
                    return nameAndValue;
                };
        
        final TypedQuery<JE> query = em.createQuery(jpql, entityClazz());
        mapEntityFields(condition, entityClazz(), function)
                .stream()
                .filter(nameAndValue -> nameAndValue._1 != null)
                .forEach(nameAndValue -> query.setParameter(nameAndValue._1, nameAndValue._2));
        return query.setFirstResult(offset)
                    .setMaxResults(rowCountPerPage)
                    .getResultList();
    }
    
    private String constructJPQLFrom()
    {
        return "FROM " + entityName() + " t";
    }
    
    private static class EntityField
    {
        public String fieldName;
        public Object fieldValue;
        public String fieldValueAsStringInUISearchColumn;
        JsfUISearchColumn jsfUISearchColumn;
        JsfUISearchMethodType jsfUISearchMethodType;
        JsfUISearchColumnConverter jsfUISearchColumnConverter;
        JsfUIListColumnOrder jsfUIListColumnOrder;
    }
    private <R> List<R> mapEntityFields(JE entity, Class<JE> modelClass, Function<EntityField,R> function)
    {
        List<R> result = new ArrayList<>();
        
        for(Field field : modelClass.getDeclaredFields()) {
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
                Logger.getLogger(EntityCRUDService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(EntityCRUDService.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            JsfUIListColumnOrder jsfUIListColumnOrder = field.getAnnotation(JsfUIListColumnOrder.class);
            
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
            entityField.jsfUIListColumnOrder = jsfUIListColumnOrder;
            result.add( function.apply(entityField) );
        }
        return result;
    }
    private String constructJPQLWhere(JE condition) 
    {
        Function<EntityField,String> function =  
                     -> {
                    String conditionExpression = "";
                    if(entityField.fieldValue != null) {
                        switch(entityField.jsfUISearchMethodType) {
                            case SEARCH_METHOD_EQUAL:
                                conditionExpression = "t." + entityField.fieldName + "= :" + entityField.fieldName; 
                                break;

                            case SEARCH_METHOD_INCLUDE:
                                conditionExpression = "t." + entityField.fieldName + " like :"  + entityField.fieldName + " ESCAPE '\\'";
                                break;
                        }
                    }
                    return conditionExpression;
                };
        
        String wherePhrase = mapEntityFields(condition, entityClazz(), function)
                                        .stream()
                                        .filter(conditionExpression -> !conditionExpression.isEmpty())
                                        .collect(Collectors.joining(" and "));
        return wherePhrase.isEmpty() == false 
                ? "WHERE " + wherePhrase : "";
    }
    
    private String constructJPQLOrderBy(JE condition, JsfEntityListSession<JE> jsfEntityListSession)
    {
        Comparator<JsfUIColumnSetting> comparator =
                Comparator.comparing(setting -> setting.getJsfUIListColumnOrder().orderSequence());

        String orderPhrase = jsfEntityListSession.getUIColumnSettings()
                                .filter(setting -> setting != null)
                                .filter(setting -> setting.getJsfUIListColumnOrder() != null)
                                .sorted(comparator)
                                .map(setting -> "t.%fieldName% %orderType%"
                                                    .replaceAll("%fieldName%", setting.getFieldName())
                                                    .replaceAll("%orderType%", setting.getJsfUIListColumnOrder().orderType().equals(DESCENDING)
                                                                                    ? "DESC" : "ASC")
                                )
                                .collect(Collectors.joining(", "));
                            
        return orderPhrase.isEmpty() == false
                ? "ORDER BY " + orderPhrase : "";
    }
    
    private String entityName()
    {
        Entity entity = entityClazz().getDeclaredAnnotation(Entity.class);
        return entity.name().isEmpty() 
                    ? entity.name() 
                    : entityClazz().getSimpleName();
    }
    
    private String likeEscape(String likeCondition)
    {
        return likeCondition
                .replaceAll("_", "\\\\_")
                .replaceAll("%", "\\\\%");
    }    
}
