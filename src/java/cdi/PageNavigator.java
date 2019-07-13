/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cdi;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author owner
 */
@Named
@RequestScoped
public class PageNavigator {
    public static class PageLink {
        public int pageNo;

        public int getPageNo() {
            return pageNo;
        }
        
        public String moveToPage()
        {
            return "";
        }
    }
    
    private int rowCountPerPage = 0;
    private int showPageCount = 10;
    private int currentPageNo = 1;
    private int maxPageCount = 100;
    
    public List<PageLink> getPageLinks()
    {
        return new ArrayList<>();
    }
    
    public void moveToPage(int pageNo)
    {
        
    }
    
    public void prevPage()
    {
        
    }
    
    public void nextPage()
    {
        
    }
    
    public void updateMaxPageCount()
    {
        
    }
}
