package cdi.base;

import cdi.dependent.util.EntityCRUD;
import cdi.base.dependent.EntityListSetting;
import cdi.dependent.util.EntityURLQueryHandler;
import cdi.dependent.util.PageNavigator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.transaction.Transactional;
import jsf.type.JsfUIOrderType;
import jsf.ui.annotation.JsfUIListColumnOrder;
import jsf.ui.annotation.dynamic.DynJsfUIListColumnOrder;
import util.AnnotationHelper;

/**
 *
 * @author Owner
 */
public abstract class EntityListBase<E extends Serializable, PK extends Serializable> implements Serializable {   
    private E searchCondition;
    private List<E> entities;
    private DataModel<E> entityDataModel;
    private Long entityAllCount;
    @Inject
    private EntityCRUD<E,PK> entityCRUD;
    @Inject
    private PageNavigator pageNavigator;
    @Inject
    private EntityListSetting<E> setting;
    @Inject
    private EntityURLQueryHandler<E> urlQueryHandler;
    
    abstract public Class<E> modelClass();

    public PageNavigator getPageNavigator() { return pageNavigator; }
    public EntityListSetting getSetting() { return setting; }
    
    public E       getSearchCondition() { return searchCondition; }
    public List<E> getEntities()   { return entities; }
    public DataModel<E> getEntityDataModel() { return entityDataModel; }
   
    public int     getEntityCount()     { return entities.size(); }
    public Long    getEntityAllCount()  { return entityAllCount; }
    
    @PostConstruct
    public void init()
    {
        try {
            this.searchCondition = modelClass().newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
            this.searchCondition = null;
        }
        this.entities = new ArrayList<>();
        this.entityDataModel = new ListDataModel<>(entities);
        this.entityAllCount = 0L;
    }
            
    @PreDestroy
    public void terminate()
    {
        
    }
    
    public void viewAction() 
    {
        //検索条件を基に抽出処理を実行する -------------------------------------
        this.entityAllCount = entityCRUD.countAll(searchCondition, modelClass());

        getPageNavigator().build(entityAllCount, urlQueryHandler.generateQueryStrings(searchCondition));
        
        this.entities = entityCRUD.search(searchCondition, 
                                               getPageNavigator().getOffset(), 
                                               getPageNavigator().getRowCountPerPage(),
                                               modelClass()); 
        this.entityDataModel = new ListDataModel<>(entities);
    }
    
    public String create()
    { 
        return setting.detailPageName() 
                        + "?faces-redirect=true&mode=New";
    }
    
    public String search()
    {
        String queryString = urlQueryHandler.generateString(searchCondition);        
        return setting.listPageName() 
                + "?faces-redirect=true" 
                + (queryString.isEmpty() ? "" : "&" + queryString);
    }
    
    public String clear()
    {
        return setting.listPageName() + "?faces-redirect=true";
    }
    
    public String createBatch()
    {
        return setting.createBatchPageName() + "?faces-redirect=true";
    }
    
    @Transactional
    public String deleteAll()
    {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.setKeepMessages(true);    //リダイレクト後もFacesMessageが保持されるよう設定する
        int deleteCount = entityCRUD.deleteAll(modelClass());
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(setting.messageDeleteAllEntityCompleted(deleteCount)));
        String queryString = urlQueryHandler.generateString(searchCondition);
        return setting.listPageName() 
                        + "?faces-redirect=true" 
                        + (queryString.isEmpty() ? "" : "&" + queryString);
    }
    
    @Transactional
    public String delete(PK entityId)
    {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.setKeepMessages(true);    //リダイレクト後もFacesMessageが保持されるよう設定する
        
        E entity = entityCRUD.find(entityId, modelClass());
        if(entity == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( setting.messageDeleteEntityNotFound() ));
            return search();
        }
        entityCRUD.delete(entityId, modelClass());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( setting.messageDeleteEntityCompleted(entityId.toString()) ));
        return search();
    }
    
    public String gotoDetail(PK entityId, String mode)
    {
        E entity = entityCRUD.find(entityId, modelClass());
        if(entity == null) {
            return "";
        }
        return setting.detailPageName() 
                            + "?faces-redirect=true&employee_id=" 
                            + urlQueryHandler.urlEncode(entityId.toString()) 
                            + "&mode=" 
                            + urlQueryHandler.urlEncode(mode);
    }
    
    
    public String sortAscending()
    {
        AnnotationHelper.alterAnnotationOn(
                            modelClass(),
                            "employeeCode",
                            JsfUIListColumnOrder.class, 
                            new DynJsfUIListColumnOrder(JsfUIOrderType.ASCENDING, 1));
        return search();
    }
    
    public String sortDescending()
    {
        AnnotationHelper.alterAnnotationOn(
                            modelClass(),
                            "employeeCode",
                            JsfUIListColumnOrder.class, 
                            new DynJsfUIListColumnOrder(JsfUIOrderType.DESCENDING, 1));
        return search();
    }
}
