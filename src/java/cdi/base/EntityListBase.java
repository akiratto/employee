package cdi.base;

import cdi.EntityCRUD;
import cdi.EntityListSetting;
import cdi.EntityURLQueryHandler;
import cdi.PageNavigator;
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
    
    abstract protected Class<E> entityClazz();
//    abstract protected EntityDbAction<E,PK> entityDbAction();
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
    
    public void viewAction() throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException
    {
        //検索条件を基に抽出処理を実行する -------------------------------------
        this.entityAllCount = entityCRUD.countAll(searchCondition, entityClazz());

        getPageNavigator().build(entityAllCount, urlQueryHandler.generateQueryStrings(searchCondition));
        
        this.entityDataList = entityCRUD.search(searchCondition, 
                                               getPageNavigator().getOffset(), 
                                               getPageNavigator().getRowCountPerPage(),
                                               entityClazz()); 
    }
    
    public String create()
    { 
        return setting.detailPageName(entityClazz()) 
                        + "?faces-redirect=true&mode=New";
    }
    
    public String search()
    {
        String queryString = urlQueryHandler.generateString(searchCondition);        
        return setting.listPageName(entityClazz()) 
                + "?faces-redirect=true" 
                + (queryString.isEmpty() ? "" : "&" + queryString);
    }
    
    public String clear()
    {
        return setting.listPageName(entityClazz()) + "?faces-redirect=true";
    }
    
    public String createBatch()
    {
        return setting.createBatchPageName(entityClazz()) + "?faces-redirect=true";
    }
    
    @Transactional
    public String deleteAll()
    {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.setKeepMessages(true);    //リダイレクト後もFacesMessageが保持されるよう設定する
        int deleteCount = entityCRUD.deleteAll(entityClazz());
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(setting.messageDeleteAllEntityCompleted(entityClazz(),deleteCount)));
        String queryString = urlQueryHandler.generateString(searchCondition);
        return setting.listPageName(entityClazz()) 
                        + "?faces-redirect=true" 
                        + (queryString.isEmpty() ? "" : "&" + queryString);
    }
    
    @Transactional
    public String delete(PK entityId)
    {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.setKeepMessages(true);    //リダイレクト後もFacesMessageが保持されるよう設定する
        
        E entity = entityCRUD.find(entityId, entityClazz());
        if(entity == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( setting.messageDeleteEntityNotFound(entityClazz()) ));
            return search();
        }
        entityCRUD.delete(entityId, entityClazz());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( setting.messageDeleteEntityCompleted(entityClazz(), entityId.toString()) ));
        return search();
    }
    
    public String gotoDetail(PK entityId, String mode)
    {
        E entity = entityCRUD.find(entityId, entityClazz());
        if(entity == null) {
            return "";
        }
        return setting.detailPageName(entityClazz()) 
                            + "?faces-redirect=true&employee_id=" 
                            + urlQueryHandler.urlEncode(entityId.toString()) 
                            + "&mode=" 
                            + urlQueryHandler.urlEncode(mode);
    }
}
