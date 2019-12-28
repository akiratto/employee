package cdi.base;

import cdi.PageNavigator;
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
import java.util.Optional;
import static java.util.stream.Collectors.joining;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.transaction.Transactional;
import jsf.ui.UIButtonsInList;
import jsf.ui.annotation.JsfUICreateBatchPage;
import jsf.ui.annotation.JsfUIDetailPage;
import jsf.ui.annotation.JsfUIListPage;
import jsf.ui.annotation.JsfUIModel;

/**
 *
 * @author Owner
 */
public abstract class EntityListBase<E extends Serializable, PK> 
        implements Serializable, UIButtonsInList {
    private E searchCondition;
    private List<E> entityDataList;
    private Long entityAllCount;
    @Inject
    private PageNavigator pageNavigator;
    
    abstract protected Class<E> entityClazz();
    abstract protected EntityDbAction<E,PK> entityDbAction();
    public PageNavigator getPageNavigator()
    {
        return pageNavigator;
    }
    
    public JsfUIModel           getJsfUIModel() { return entityClazz().getDeclaredAnnotation(JsfUIModel.class); }
    public JsfUIListPage        getJsfUIListPage() { return entityClazz().getDeclaredAnnotation(JsfUIListPage.class); }
    public JsfUIDetailPage      getJsfUIDetailPage() { return entityClazz().getDeclaredAnnotation(JsfUIDetailPage.class); }
    public JsfUICreateBatchPage getJsfUICreateBatchPage() { return entityClazz().getDeclaredAnnotation(JsfUICreateBatchPage.class); }
        
    public String entityTitle()          { return Optional.of(this.getJsfUIModel()).map(m -> m.modelTitle()).orElse(""); }
    public String listPageName()         { return Optional.of(this.getJsfUIListPage()).map(l -> l.listPageTitle()).orElse(""); }
    public String detailPageName()       { return Optional.of(this.getJsfUIDetailPage()).map(d -> d.detailPageTitle()).orElse(""); }
    public String createBatchPageName() { return Optional.of(this.getJsfUICreateBatchPage()).map(b -> b.createBatchPageName()).orElse(""); }
    
    public String messageDeleteEntityNotFound() { return Optional.of(this.getJsfUIListPage()).map(l -> l.listPageTitle()).orElse(""); }
    
//    public String messageDeleteEntityCompleted(E entity, PK entityId) { return entityTitle() + "(ID:" + entityId.toString() + ")を削除しました。"; }
//    public String messageDeleteAllEntityCompleted(int deleteCount) { return deleteCount + "件の" + entityTitle() + "を削除しました。"; }

    public E       getSearchCondition() { return searchCondition; }
    public List<E> getEntityDataList()   { return entityDataList; }
    public int     getEntityCount()     { return entityDataList.size(); }
    public Long    getEntityAllCount()  { return entityAllCount; }
    
    @PostConstruct
    public void init()
    {
        try {
            this.searchCondition = entityClazz().newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            this.searchCondition = null;
        }
        this.entityDataList = new ArrayList<>();
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

        getPageNavigator().build(entityAllCount, generateQueryStrings(searchCondition));
        
        this.entityDataList = entityDbAction().search(searchCondition, 
                                                getPageNavigator().getOffset(), 
                                                getPageNavigator().getRowCountPerPage()); 
    }
    
    //-- UIButtonsInList インターフェース実装
    @Override public String create() { return detailPageName() + "?faces-redirect=true&mode=New"; }
    @Override
    public String search()
    {
        String queryString = generateString(searchCondition);        
        return listPageName() + "?faces-redirect=true" + (queryString.isEmpty() ? "" : "&" + queryString);
    }
    @Override public String clear() { return listPageName() + "?faces-redirect=true"; }
    @Override public String createBatch() { return createBatchPageName() + "?faces-redirect=true"; }
    @Override
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
    
    public String gotoDetail(PK entityId, String mode)
    {
        E entity = entityDbAction().find(entityId);
        if(entity == null) {
            return "";
        }
        return detailPageName() + "?faces-redirect=true&employee_id=" + urlEncode(entityId.toString()) + "&mode=" + urlEncode(mode);
    }
    

    



    
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
