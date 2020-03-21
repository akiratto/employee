package application.base;

import presentation.jsf.base.JsfEntityListSearcher;
import application.converter.JsfEntityAndTableEntityConverter;
import presentation.jsf.base.JsfEntityListTextResources;
import presentation.jsf.base.JsfEntityListSession;
import database.dependent.EntityCRUDService;
import presentation.jsf.entity.JsfEntityURLQueryHandler;
import application.dependent.PageNavigator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 *
 * @author Owner
 */
public abstract class EntityList<JE extends Serializable, TE extends Serializable, PK extends Serializable> 
        implements Serializable
{   
    private JE searchCondition;
    private List<JE> jsfEntities;
    private DataModel<JE> jsfEntityDataModel;
    private Long jsfEntityAllCount;
    @Inject
    private EntityCRUDService<TE,PK> entityCRUDService;
    @Inject
    private JsfEntityListSearcher<JE> searcher;
    @Inject
    private PageNavigator pageNavigator;
    @Inject
    private JsfEntityURLQueryHandler<JE> urlQueryHandler;
    
    abstract protected Class<JE> jsfEntityClass();
    abstract protected JsfEntityListSession<JE> jsfEntityListSession();
    abstract protected JsfEntityListTextResources<JE> jsfEntityListTextResources();
    abstract protected JsfEntityAndTableEntityConverter<JE,TE> jsfEntityAndTableEntityConverter();
    
    abstract public Class<TE> tableEntityClass();

    public PageNavigator getPageNavigator() { return pageNavigator; }
    public JsfEntityListSession<JE> getSession() { return jsfEntityListSession(); }
    public JsfEntityListTextResources<JE> getTextResources() { return jsfEntityListTextResources(); }
    
    public JE       getSearchCondition() { return searchCondition; }
    public List<JE> getJsfEntities()   { return jsfEntities; }
    public DataModel<JE> getJsfEntityDataModel() { return jsfEntityDataModel; }
   
    public int     getJsfEntityCount()     { return jsfEntities.size(); }
    public Long    getJsfEntityAllCount()  { return jsfEntityAllCount; }
    
    @PostConstruct
    public void init()
    {
        try {
            this.searchCondition = jsfEntityClass().newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
            this.searchCondition = null;
        }
        this.jsfEntities = new ArrayList<>();
        this.jsfEntityDataModel = new ListDataModel<>(jsfEntities);
        this.jsfEntityAllCount = 0L;
    }
            
    @PreDestroy
    public void terminate()
    {
        
    }
    
    public void viewAction() 
    {
        //検索条件を基に抽出処理を実行する -------------------------------------
        TE searchConditionTable = jsfEntityAndTableEntityConverter().toTableEntity(searchCondition);
        this.jsfEntityAllCount = entityCRUDService.countAll(searchConditionTable, tableEntityClass());

        getPageNavigator().build(jsfEntityAllCount, urlQueryHandler.generateQueryStrings(searchCondition));
        
        this.jsfEntities = entityCRUDService.search(searchConditionTable, 
                                               getPageNavigator().getOffset(), 
                                               getPageNavigator().getRowCountPerPage(),
                                               tableEntityClass(),
                                               jsfEntityListSession()); 
        this.jsfEntityDataModel = new ListDataModel<>(jsfEntities);
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
        int deleteCount = entityCRUDService.deleteAll(jsfEntityClass());
        
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
        
        E entity = entityCRUDService.find(entityId, jsfEntityClass());
        if(entity == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( setting.messageDeleteEntityNotFound() ));
            return search();
        }
        entityCRUDService.delete(entityId, jsfEntityClass());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( setting.messageDeleteEntityCompleted(entityId.toString()) ));
        return search();
    }
    
    public String gotoDetail(PK entityId, String mode)
    {
        E entity = entityCRUDService.find(entityId, jsfEntityClass());
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
        jsfEntityListSession().sortAscending();
        return search();
    }
    
    public String sortDescending()
    {
        jsfEntityListSession().sortDescending();
        return search();
    }
}
