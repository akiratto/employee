/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.ui.setting;

import java.io.Serializable;
import jsf.ui.annotation.JsfUIListPage;

/**
 *
 * @author Owner
 */
public class JsfUIListPageSetting<E extends Serializable> {
    private E entity;
    private JsfUIListPage jsfUIListPage;
    
    public JsfUIListPageSetting(E entity)
    {
        this.entity = entity;
        this.jsfUIListPage = entity.getClass().getDeclaredAnnotation(JsfUIListPage.class);
    }
    
    public String listPageTitle() { return jsfUIListPage != null ? jsfUIListPage.listPageTitle() : ""; }
    public String listPageName()   { return jsfUIListPage != null ? jsfUIListPage.listPageName() : ""; }
    public String detailPageTitle() { return jsfUIListPage != null ? jsfUIListPage.detailPageTitle() : ""; }
    public String detailPageName()  { return jsfUIListPage != null ? jsfUIListPage.detailPageName() : ""; }
}
