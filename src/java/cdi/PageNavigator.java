package cdi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author owner
 */
@Named
@ViewScoped
public class PageNavigator implements Serializable {
    public static class PageLink {
        public String baseURL;
        public int pageNo;
        
        public PageLink(String baseURL, int pageNo)
        {
            this.baseURL = baseURL;
            this.pageNo = pageNo;
        }
        public int getPageNo() {
            return pageNo;
        }
        
        public String moveToPage()
        {
            System.out.println(">>> PageLink moveToPage BEGIN >>>");
            String pageURL = baseURL + "?faces-redirect=true&pageNo=" + pageNo;
            System.out.println(" pageURL=" + pageURL);
            System.out.println("<<< PageLink moveToPage END >>>");
            return pageURL;                   
        }
    }
    
    private String baseURL;
    private int rowCountPerPage = 0;
    private int showPageCount = 10;
    private int currentPageNo = 1;
    private int maxPageCount = 100;

    @PostConstruct
    public void initialize()
    {
        System.out.println(">>> PageNavigator initialize BEGIN >>>");
        this.baseURL = FacesContext.getCurrentInstance().getViewRoot().getViewId().substring(1);
        System.out.println(" baseURL=" + this.baseURL);
        System.out.println("<<< PageNavigator initialize END <<<");
    }
    
    public List<PageLink> getPageLinks()
    {
        List<PageLink> pageLinks = new ArrayList<>();
        for(int i = 1; i <= 10; i++) {
            pageLinks.add(new PageLink(this.baseURL,i));
        }
        return pageLinks;
    }
    
    public String moveToPage(int pageNo)
    {
        return baseURL + "?pageNo=" + pageNo;
    }
    
    public boolean prevPageEnabled()
    {
        return (currentPageNo - 1) >= 1;
    }
    
    public String prevPage()
    {
        return baseURL + "?pageNo=" + (currentPageNo - 1);
    }
    
    public boolean nextPageEnabled()
    {
        return (currentPageNo + 1)  <= maxPageCount;
    }
    
    public String nextPage()
    {
        return baseURL + "?pageNo=" + (currentPageNo + 1);
    }
    
    public void updateMaxPageCount()
    {
        
    }
}
