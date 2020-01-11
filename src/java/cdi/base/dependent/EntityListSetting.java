package cdi.base.dependent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import javax.el.ELProcessor;
import jsf.ui.annotation.JsfUICreateBatchPage;
import jsf.ui.annotation.JsfUIDetailPage;
import jsf.ui.annotation.JsfUIListPage;
import jsf.ui.annotation.JsfUIListPageButtons;
import jsf.ui.annotation.JsfUIModel;

/**
 *
 * @author Owner
 */
public class EntityListSetting<E extends Serializable> implements Serializable {
    private Class<?> modelClass;
    
    /*
        コンストラクタの引数はダミー。
        実行時にEntityListSettingの型引数Eに指定された型を
        取得するためのもの。
        
        引数に型Eの可変長配列を指定することで
        本コンストラクタ呼び出し時に引数を指定しなくても
        dummy変数に型変数Eに指定された型情報が残る。
        それをgetComponentType()メソッドで取得している。
    */
    public EntityListSetting(E... dummy)
    {
        if(dummy.length > 0) {
            throw new IllegalArgumentException("dummy引数を指定してはいけません。");
        }
        modelClass = dummy.getClass().getComponentType();
    }
    
    public JsfUIModel           getJsfUIModel() { return modelClass.getDeclaredAnnotation(JsfUIModel.class); }
    public JsfUIListPage        getJsfUIListPage() { return modelClass.getDeclaredAnnotation(JsfUIListPage.class); }
    public JsfUIDetailPage      getJsfUIDetailPage() { return modelClass.getDeclaredAnnotation(JsfUIDetailPage.class); }
    public JsfUICreateBatchPage getJsfUICreateBatchPage() { return modelClass.getDeclaredAnnotation(JsfUICreateBatchPage.class); }
    public JsfUIListPageButtons getJsfUIListPageButtons() { return modelClass.getDeclaredAnnotation(JsfUIListPageButtons.class); }
    
    public String modelTitle()          { return Optional.ofNullable(getJsfUIModel()).map(JsfUIModel::modelTitle).orElse(""); }
    public String listPageName()         { return Optional.ofNullable(getJsfUIListPage()).map(JsfUIListPage::listPageName).orElse(""); }
    public String listPageTitle()         { return Optional.ofNullable(getJsfUIListPage()).map(JsfUIListPage::listPageTitle).orElse(""); }
    public String detailPageName()       { return Optional.ofNullable(getJsfUIDetailPage()).map(JsfUIDetailPage::detailPageName).orElse(""); }
    public String detailPageTitle()       { return Optional.ofNullable(getJsfUIDetailPage()).map(JsfUIDetailPage::detailPageTitle).orElse(""); }
    public String createBatchPageName() { return Optional.ofNullable(getJsfUICreateBatchPage()).map(JsfUICreateBatchPage::createBatchPageName).orElse(""); }
    public String createBatchPageTitle() { return Optional.ofNullable(getJsfUICreateBatchPage()).map(JsfUICreateBatchPage::createBatchPageTitle).orElse(""); }
    
    public String createButtonTitle() { return Optional.ofNullable(getJsfUIListPageButtons()).map(JsfUIListPageButtons::createButtonTitle).orElse(""); }
    public String searchButtonTitle() { return Optional.ofNullable(getJsfUIListPageButtons()).map(JsfUIListPageButtons::searchButtonTitle).orElse(""); }
    public String clearButtonTitle() { return Optional.ofNullable(getJsfUIListPageButtons()).map(JsfUIListPageButtons::clearButtonTitle).orElse(""); }
    public String createBatchButtonTitle() { return Optional.ofNullable(getJsfUIListPageButtons()).map(JsfUIListPageButtons::createBatchButtonTitle).orElse(""); }
    public String deleteAllButtonTitle() { return Optional.ofNullable(getJsfUIListPageButtons()).map(JsfUIListPageButtons::deleteAllButtonTitle).orElse(""); }
    
    public String messageDeleteEntityNotFound()
    {
        String messageTemplate = Optional
                                    .ofNullable(getJsfUIListPage())
                                    .map(JsfUIListPage::messageDeleteEntityNotFound)
                                    .orElse("");
        return generateMessage(messageTemplate);
    }
    
    public String messageDeleteEntityCompleted(String entityId)
    {
        Map<String,Object> additionResources = new HashMap<>();
        additionResources.put("entityId", entityId);
        
        String messageTemplate = Optional
                                    .ofNullable(getJsfUIListPage())
                                    .map(JsfUIListPage::messageDeleteEntityCompleted)
                                    .orElse("");
        return generateMessage(messageTemplate,additionResources);
    }
    
    public String messageDeleteAllEntityCompleted(int deleteCount)
    {
        Map<String,Object> addtionResources = new HashMap<>();
        addtionResources.put("deleteCount", deleteCount);
        
        String messageTemplate = Optional
                                    .ofNullable(getJsfUIListPage())
                                    .map(JsfUIListPage::messageDeleteAllEntityCompleted)
                                    .orElse("");
        return generateMessage(messageTemplate, addtionResources);
    }
    
    public String messageDeleteAllEntityConfirm()
    {
        String messageTemplate = Optional
                                    .ofNullable(getJsfUIListPage())
                                    .map(JsfUIListPage::messageDeleteAllEntityConfirm)
                                    .orElse("");
        return generateMessage(messageTemplate);
    }
    
    public String messageEntityNotFoundInDataTable()
    {
        String messageTemplate = Optional
                                    .ofNullable(getJsfUIListPage())
                                    .map(JsfUIListPage::messageEntityNotFoundInDataTable)
                                    .orElse("");
        return generateMessage(messageTemplate);
    }
    
    public String messageDisplayRangeInDataTable(Long entityAllCount, Long beginRowIndex, Long endRowIndex)
    {
        Map<String,Object> addResources = new HashMap<>();
        addResources.put("entityAllCount", entityAllCount);
        addResources.put("beginRowIndex", beginRowIndex);
        addResources.put("endRowIndex", endRowIndex);
        
        String messageTemplate = Optional
                                    .ofNullable(getJsfUIListPage())
                                    .map(JsfUIListPage::messageDisplayRangeInDataTable)
                                    .orElse("");
        return generateMessage(messageTemplate, addResources);
    }
    
    protected String generateMessage(String messageTemplate)
    {
        return generateMessage(messageTemplate, null);
    }
    
    protected String generateMessage(String messageTemplate, Map<String, Object> additionResources)
    {
        ELProcessor elProcessor = new ELProcessor();
        elProcessor.defineBean("modelTitle",                modelTitle());
        elProcessor.defineBean("listPageName",              listPageName());
        elProcessor.defineBean("listPageTitle",             listPageTitle());
        elProcessor.defineBean("detailPageName",            detailPageName());
        elProcessor.defineBean("detailPageTitle",           detailPageTitle());
        elProcessor.defineBean("createBatchPageName",       createBatchPageName());
        elProcessor.defineBean("createBatchPageTitle",      createBatchPageTitle());
        elProcessor.defineBean("createButtonTitle",         createButtonTitle());
        elProcessor.defineBean("searchButtonTitle",         searchButtonTitle());
        elProcessor.defineBean("clearButtonTitle",          clearButtonTitle());
        elProcessor.defineBean("createBatchButtonTitle",    createBatchButtonTitle());
        elProcessor.defineBean("deleteAllButtonTitle",      deleteAllButtonTitle());
        
        if(additionResources != null) {
            for(Entry<String,Object> entry : additionResources.entrySet()) {
                elProcessor.defineBean(entry.getKey(), entry.getValue());
            }
        }
        
        Object evalResult = elProcessor.eval(messageTemplate);
        return evalResult instanceof String 
                ? (String)evalResult 
                : "";
    }
}
