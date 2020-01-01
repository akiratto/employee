package cdi.base;

import cdi.dependent.EntityCRUD;
import cdi.dependent.EntityListSetting;
import cdi.dependent.EntityURLQueryHandler;
import cdi.dependent.PageNavigator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 *
 * @author Owner
 */
public abstract class EntityListBase<E extends Serializable, PK extends Serializable> implements Serializable {
    private E searchCondition;
    private List<E> entityDataList;
    private Long entityAllCount;
    @Inject
    private EntityCRUD<E,PK> entityCRUD;
    @Inject
    private PageNavigator pageNavigator;
    @Inject
    private EntityListSetting setting;
    @Inject
    private EntityURLQueryHandler<E> urlQueryHandler;
    
    abstract protected Class<E> entityClass();

    public PageNavigator getPageNavigator() { return pageNavigator; }
    public EntityListSetting getSetting() { return setting; }
    
    public E       getSearchCondition() { return searchCondition; }
    public List<E> getEntityDataList()   { return entityDataList; }
    public int     getEntityCount()     { return entityDataList.size(); }
    public Long    getEntityAllCount()  { return entityAllCount; }
    
    @PostConstruct
    public void init()
    {
        try {
            this.searchCondition = entityClass().newInstance();
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
        this.entityAllCount = entityCRUD.countAll(searchCondition, entityClass());

        getPageNavigator().build(entityAllCount, urlQueryHandler.generateQueryStrings(searchCondition));
        
        this.entityDataList = entityCRUD.search(searchCondition, 
                                               getPageNavigator().getOffset(), 
                                               getPageNavigator().getRowCountPerPage(),
                                               entityClass()); 
    }
    
    public String create()
    { 
        return setting.detailPageName(entityClass()) 
                        + "?faces-redirect=true&mode=New";
    }
    
    public String search()
    {
        String queryString = urlQueryHandler.generateString(searchCondition);        
        return setting.listPageName(entityClass()) 
                + "?faces-redirect=true" 
                + (queryString.isEmpty() ? "" : "&" + queryString);
    }
    
    public String clear()
    {
        return setting.listPageName(entityClass()) + "?faces-redirect=true";
    }
    
    public String createBatch()
    {
        return setting.createBatchPageName(entityClass()) + "?faces-redirect=true";
    }
    
    @Transactional
    public String deleteAll()
    {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.setKeepMessages(true);    //リダイレクト後もFacesMessageが保持されるよう設定する
        int deleteCount = entityCRUD.deleteAll(entityClass());
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(setting.messageDeleteAllEntityCompleted(entityClass(),deleteCount)));
        String queryString = urlQueryHandler.generateString(searchCondition);
        return setting.listPageName(entityClass()) 
                        + "?faces-redirect=true" 
                        + (queryString.isEmpty() ? "" : "&" + queryString);
    }
    
    @Transactional
    public String delete(PK entityId)
    {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.setKeepMessages(true);    //リダイレクト後もFacesMessageが保持されるよう設定する
        
        E entity = entityCRUD.find(entityId, entityClass());
        if(entity == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( setting.messageDeleteEntityNotFound(entityClass()) ));
            return search();
        }
        entityCRUD.delete(entityId, entityClass());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( setting.messageDeleteEntityCompleted(entityClass(), entityId.toString()) ));
        return search();
    }
    
    public String gotoDetail(PK entityId, String mode)
    {
        E entity = entityCRUD.find(entityId, entityClass());
        if(entity == null) {
            return "";
        }
        return setting.detailPageName(entityClass()) 
                            + "?faces-redirect=true&employee_id=" 
                            + urlQueryHandler.urlEncode(entityId.toString()) 
                            + "&mode=" 
                            + urlQueryHandler.urlEncode(mode);
    }
}
