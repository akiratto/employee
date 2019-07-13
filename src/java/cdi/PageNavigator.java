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
            String pageURL = baseURL + "?faces-redirect=true&page_no=" + pageNo;
            System.out.println(" pageURL=" + pageURL);
            System.out.println("<<< PageLink moveToPage END <<<");
            return pageURL;                   
        }
    }
    
    private String baseURL;
    private int rowCountPerPage = 20;
    private int showPageCount = 10;
    private Long currentPageNo = 1L;
    private int maxPageCount = 100;

    @PostConstruct
    public void initialize()
    {
        System.out.println(">>> PageNavigator initialize BEGIN >>>");
        this.baseURL = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        System.out.println(" baseURL=" + this.baseURL);
        System.out.println("<<< PageNavigator initialize END <<<");
    }
    
    public void viewAction()
    {
        System.out.println(">>> PageNavigator viewAction BEGIN >>>");
        List<PageLink> pageLinks = new ArrayList<>();
        for(int i = 1; i <= 10; i++) {
            pageLinks.add(new PageLink(this.baseURL,i));
        }
        return pageLinks;
        
        System.out.println("<<< PageNavigator viewAction END <<<");
    }

    public Long getCurrentPageNo() {
        return currentPageNo;
    }

    public void setCurrentPageNo(Long currentPageNo) {
        this.currentPageNo = currentPageNo;
    }
    
    public Long getBeginShowPageNo() {
        this.showPageCount
    }
    
    public Long getBeginRowIndex()
    {
        return (currentPageNo-1) * rowCountPerPage + 1;
    }
    
    public Long getEndRowIndex(Long allRowCount)
    {
        return Long.min(allRowCount, getBeginRowIndex() + rowCountPerPage - 1);
    }

    public int getRowCountPerPage() {
        return rowCountPerPage;
    }

    public int getShowPageCount() {
        return showPageCount;
    }

    public int getMaxPageCount() {
        return maxPageCount;
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
        return baseURL + "?faces-redirect=true&page_no=" + pageNo;
    }
    
    public boolean prevPageEnabled()
    {
        return (currentPageNo - 1) >= 1;
    }
    
    public String prevPage()
    {
        return baseURL + "?faces-redirect=true&page_no=" + (currentPageNo - 1);
    }
    
    public boolean nextPageEnabled()
    {
        return (currentPageNo + 1)  <= maxPageCount;
    }
    
    public String nextPage()
    {
        return baseURL + "?faces-redirect=true&page_no=" + (currentPageNo + 1);
    }
    
    public void updateMaxPageCount()
    {
        
    }
}
