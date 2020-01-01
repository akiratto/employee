package cdi.dependent;

import java.io.Serializable;
import java.util.Optional;
import javax.el.ELProcessor;
import javax.enterprise.context.Dependent;
import jsf.ui.annotation.JsfUICreateBatchPage;
import jsf.ui.annotation.JsfUIDetailPage;
import jsf.ui.annotation.JsfUIListPage;
import jsf.ui.annotation.JsfUIModel;

/**
 *
 * @author Owner
 */
@Dependent
public class EntityListSetting implements Serializable {
    public JsfUIModel           getJsfUIModel(Class<?> modelClazz) { return modelClazz.getDeclaredAnnotation(JsfUIModel.class); }
    public JsfUIListPage        getJsfUIListPage(Class<?> modelClazz) { return modelClazz.getDeclaredAnnotation(JsfUIListPage.class); }
    public JsfUIDetailPage      getJsfUIDetailPage(Class<?> modelClazz) { return modelClazz.getDeclaredAnnotation(JsfUIDetailPage.class); }
    public JsfUICreateBatchPage getJsfUICreateBatchPage(Class<?> modelClazz) { return modelClazz.getDeclaredAnnotation(JsfUICreateBatchPage.class); }
        
    public String modelTitle(Class<?> modelClazz)          { return Optional.ofNullable(this.getJsfUIModel(modelClazz)).map(JsfUIModel::modelTitle).orElse(""); }
    public String listPageName(Class<?> modelClazz)         { return Optional.ofNullable(this.getJsfUIListPage(modelClazz)).map(JsfUIListPage::listPageName).orElse(""); }
    public String listPageTitle(Class<?> modelClazz)         { return Optional.ofNullable(this.getJsfUIListPage(modelClazz)).map(JsfUIListPage::listPageTitle).orElse(""); }
    public String detailPageName(Class<?> modelClazz)       { return Optional.ofNullable(this.getJsfUIDetailPage(modelClazz)).map(JsfUIDetailPage::detailPageName).orElse(""); }
    public String detailPageTitle(Class<?> modelClazz)       { return Optional.ofNullable(this.getJsfUIDetailPage(modelClazz)).map(JsfUIDetailPage::detailPageTitle).orElse(""); }
    public String createBatchPageName(Class<?> modelClazz) { return Optional.ofNullable(this.getJsfUICreateBatchPage(modelClazz)).map(JsfUICreateBatchPage::createBatchPageName).orElse(""); }
    public String createBatchPageTitle(Class<?> modelClazz) { return Optional.ofNullable(this.getJsfUICreateBatchPage(modelClazz)).map(JsfUICreateBatchPage::createBatchPageTitle).orElse(""); }
    
    public String messageDeleteEntityNotFound(Class<?> modelClazz)
    {
        ELProcessor elProcessor = new ELProcessor();
        elProcessor.defineBean("modelTitle", modelTitle(modelClazz));
        
        String messageTemplate = Optional
                                    .ofNullable(getJsfUIListPage(modelClazz))
                                    .map(JsfUIListPage::messageDeleteEntityNotFound)
                                    .orElse("");
        Object evalResult = elProcessor.eval(messageTemplate);
        return evalResult instanceof String 
                ? (String)evalResult 
                : "";
    }
    
    public String messageDeleteEntityCompleted(Class<?> modelClazz, String entityId)
    {
        ELProcessor elProcessor = new ELProcessor();
        elProcessor.defineBean("modelTitle", modelTitle(modelClazz));
        elProcessor.defineBean("entityId", entityId);
        
        String messageTemplate = Optional
                                    .ofNullable(getJsfUIListPage(modelClazz))
                                    .map(JsfUIListPage::messageDeleteEntityCompleted)
                                    .orElse("");
        Object evalResult = elProcessor.eval(messageTemplate);
        return evalResult instanceof String 
                ? (String)evalResult 
                : "";
    }
    
    public String messageDeleteAllEntityCompleted(Class<?> modelClazz, int deleteCount)
    {
        ELProcessor elProcessor = new ELProcessor();
        elProcessor.defineBean("modelTitle", modelTitle(modelClazz));
        elProcessor.defineBean("deleteCount", deleteCount);
        
        String messageTemplate = Optional
                                    .ofNullable(getJsfUIListPage(modelClazz))
                                    .map(JsfUIListPage::messageDeleteAllEntityCompleted)
                                    .orElse("");
        Object evalResult = elProcessor.eval(messageTemplate);
        return evalResult instanceof String 
                ? (String)evalResult 
                : "";
    }
    
}
