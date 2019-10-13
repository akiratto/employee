package cdi;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.joining;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.transaction.Transactional;

/**
 *
 * @author Owner
 */
public abstract class EntityListBase<E extends Serializable, PK> implements Serializable {
    private E searchCondition;
    private List<E> entityList;
    private Long entityAllCount;
    
    abstract protected Class<E> entityClazz();
    abstract protected EntityDbAction<E,PK> entityDbAction();
    abstract protected PageNavigator pageNavigator();
    
    abstract public String entityName();
    abstract public String entityTitle();
    abstract public String listPageName();
    abstract public String detailPageName();
    abstract public String createBatchPageName();
    
    public String messageDeleteEntityNotFound(PK entityId) { return "削除する" + entityTitle() + "が見つかりません。"; }
    public String messageDeleteEntityCompleted(E entity, PK entityId) { return entityTitle() + "(ID:" + entityId.toString() + ")を削除しました。"; }
    public String messageDeleteAllEntityCompleted(int deleteCount) { return deleteCount + "件の" + entityTitle() + "を削除しました。"; }

    public E       getSearchCondition() { return searchCondition; }
    public List<E> getEntityList()      { return entityList; }
    public int     getEntityCount()     { return entityList.size(); }
    public Long    getEntityAllCount()  { return entityAllCount; }
    
    @PostConstruct
    public void init()
    {
        try {
            this.searchCondition = entityClazz().newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            this.searchCondition = null;
        }
        this.entityList = new ArrayList<>();
        this.entityAllCount = 0L;
    }
            
    @PreDestroy
    public void terminate()
    {
        
    }
    
    public void viewAction()
    {
        //検索条件を基に抽出処理を実行する -------------------------------------
        this.entityAllCount = entityDbAction().countAll(searchCondition);

        pageNavigator().build(entityAllCount, generateQueryStrings(searchCondition));
        
        this.entityList = entityDbAction().search(searchCondition, 
                                                pageNavigator().getOffset(), 
                                                pageNavigator().getRowCountPerPage()); 
    }
    
    public String create() { return detailPageName() + "?faces-redirect=true&mode=New"; }
    public String createBatch() { return createBatchPageName() + "?faces-redirect=true"; }
    
    @Transactional
    public String delete(PK entityId)
    {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.setKeepMessages(true);    //リダイレクト後もFacesMessageが保持されるよう設定する
        
        E entity = entityDbAction().find(entityId);
        if(entity == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( messageDeleteEntityNotFound(entityId) ));
            return search();
        }
        entityDbAction().delete(entityId);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( messageDeleteEntityCompleted(entity, entityId) ));
        return search();
    }
    
    @Transactional
    public String deleteAll()
    {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.setKeepMessages(true);    //リダイレクト後もFacesMessageが保持されるよう設定する
        int deleteCount = entityDbAction().deleteAll();
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(messageDeleteAllEntityCompleted(deleteCount)));
        String queryString = generateString(searchCondition);
        return listPageName() + "?faces-redirect=true" + (queryString.isEmpty() ? "" : "&" + queryString);
    }
    
    public String gotoDetail(PK entityId, String mode)
    {
        E entity = entityDbAction().find(entityId);
        if(entity == null) {
            return "";
        }
        return detailPageName() + "?faces-redirect=true&employee_id=" + urlEncode(entityId.toString()) + "&mode=" + urlEncode(mode);
    }
    
    public String search()
    {
        String queryString = generateString(searchCondition);        
        return listPageName() + "?faces-redirect=true" + (queryString.isEmpty() ? "" : "&" + queryString);
    }
    
    public String clear() { return listPageName() + "?faces-redirect=true"; }


    
    private Map<String,String> generateQueryStrings(E searchCondition)
    {
        Map<String, String> queryStrings = new HashMap<>();
        for(Field field : searchCondition.getClass().getDeclaredFields()) {
            try {
                Class<?> fieldType = field.getType();
                String fieldName = toSnakeCase(field.getName());
                
                Object fieldValue = null;
                boolean tmpAccessible = field.isAccessible();
                field.setAccessible(true);
                fieldValue = field.get(searchCondition);
                field.setAccessible(tmpAccessible);

                String fieldValueAsString 
                        = fieldValue instanceof Integer && fieldValue != null                                          ? ((Integer)fieldValue).toString()
                        : fieldValue instanceof String  && fieldValue != null && ((String)fieldValue).isEmpty()==false ? (String)fieldValue
                        : fieldType.isEnum() && fieldValue != null                                                     ? ((Enum)fieldValue).name()
                        : fieldValue instanceof Date && fieldValue != null                                             ? (new SimpleDateFormat("yyyy/MM/dd")).format((Date)fieldValue) 
                        : null;
                if(fieldValueAsString != null) {
                    queryStrings.put(fieldName, fieldValueAsString);
                }
            } catch(IllegalAccessException | IllegalArgumentException e) {
                
            }
        }
        return queryStrings;
    }
    
    private String generateString(E searchCondition)
    {
        return generateQueryStrings(searchCondition)
                .entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + urlEncode(e.getValue()))
                .collect(joining("&"));
    }
    
    private String urlEncode(String target)
    {
        try {
            return URLEncoder.encode(target, "UTF-8");
        } catch(UnsupportedEncodingException ex) {
            return "";
        }
    }   
    
    private String toSnakeCase(String target)
    {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < target.length(); i++) {
            char ch = target.charAt(i);
            if('A' <= ch && ch <= 'Z') {
                sb.append('_');
                sb.append(Character.toLowerCase(ch));
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }
}
