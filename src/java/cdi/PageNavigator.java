package cdi;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author owner
 */
@Dependent
public class PageNavigator implements Serializable {
    public static class PageLink implements Serializable {
        PageNavigator pageNavigator;
        
        public String baseURL;
        private long pageNo;
        private long currentPageNo;
        
        public PageLink(PageNavigator pageNavigator, String baseURL, long pageNo, long currentPageNo)
        {
            this.pageNavigator = pageNavigator;
            this.baseURL = baseURL;
            this.pageNo = pageNo;
            this.currentPageNo = currentPageNo;
        }
        public long getPageNo() {
            return pageNo;
        }
        
        public boolean isCurrentPage()
        {
            return this.currentPageNo == this.pageNo;
        }
        
        public String moveToPage()
        {
            System.out.println(">>> PageLink moveToPage BEGIN >>>");
            String pageURL = baseURL + "?faces-redirect=true&page_no=" + pageNo + generateQueryString(pageNavigator.queryStrings);
            System.out.println(" pageURL=" + pageURL);
            System.out.println("<<< PageLink moveToPage END <<<");
            return pageURL;                   
        }
    }
    
    @PersistenceContext
    private EntityManager em;
    
    private String baseURL;
    private int rowCountPerPage = 20;
    private int showPageCount = 10;
    private Long currentPageNo = 1L;
    private int maxPageCount = 100;
    
    private Long beginRowIndex;
    private Long endRowIndex;
    private Long dataPageCount = 0L;
    private Long beginShowPageNo = 0L;
    private Long endShowPageNo = 0L;
    
    private List<PageLink> pageLinks = new ArrayList<>();
    private Map<String,String> queryStrings = new HashMap<>();

    @PostConstruct
    public void initialize()
    {
        System.out.println(">>> PageNavigator initialize BEGIN >>>");
        this.baseURL = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        System.out.println(" baseURL=" + this.baseURL);
        System.out.println("<<< PageNavigator initialize END <<<");
    }
    
    public void build(Long allRowCount, Map<String,String> queryStrings)
    {
        System.out.println(">>> PageNavigator build BEGIN >>>");
        
        calculate(allRowCount);
        
        List<PageLink> pageLinks = new ArrayList<>();
        for(long i = this.beginShowPageNo; i <= this.endShowPageNo; i++) {
            pageLinks.add(new PageLink(this, this.baseURL,i, this.currentPageNo));
        }
        this.pageLinks = pageLinks;
        this.queryStrings = queryStrings;
        
        System.out.println("<<< PageNavigator build END <<<");
    }

    public Long getCurrentPageNo() {
        return currentPageNo;
    }

    public void setCurrentPageNo(Long currentPageNo) {
        this.currentPageNo = currentPageNo;
    }

    public Long getBeginShowPageNo() {
       return this.beginShowPageNo;
    }
    
    public Long getEndShowPageNo() {
        return this.endShowPageNo;
    }
    
    public Long getBeginRowIndex()
    {
        return this.beginRowIndex;
    }
    
    public Long getEndRowIndex()
    {
        return this.endRowIndex;
    }
    
    public int getOffset()
    {
        return getBeginRowIndex().intValue() - 1;
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
        return pageLinks;
    }
    
    public boolean prevPageEnabled()
    {
        return (currentPageNo - 1) >= 1;
    }
    
    public String prevPage()
    {
        return baseURL + "?faces-redirect=true&page_no=" + (currentPageNo - 1) + generateQueryString(this.queryStrings);
    }
    
    public boolean nextPageEnabled()
    {
        return (currentPageNo + 1)  <= dataPageCount;
    }
    
    public String nextPage()
    {
        return baseURL + "?faces-redirect=true&page_no=" + (currentPageNo + 1) + generateQueryString(this.queryStrings);
    }


    private void calculate(Long allRowCount)
    {
        this.beginRowIndex = calculateBeginRowIndex(this.currentPageNo, this.rowCountPerPage);
        this.endRowIndex = calculateEndRowIndex(this.currentPageNo, this.rowCountPerPage, allRowCount);
        this.dataPageCount = calculateDataPageCount(this.rowCountPerPage, this.maxPageCount, allRowCount);
        this.beginShowPageNo = calculateBeginShowPageNo(this.currentPageNo, this.showPageCount, this.dataPageCount);
        this.endShowPageNo = calculateEndShowPageNo(this.currentPageNo, this.showPageCount, this.dataPageCount);
    }
    
    public static Long calculateBeginRowIndex(Long currentPageNo, int rowCountPerPage)
    {
        return (currentPageNo-1) * rowCountPerPage + 1;
    }
    
    public static Long calculateEndRowIndex(Long currentPageNo, int rowCountPerPage, Long allRowCount)
    {
        return Long.min(allRowCount, calculateBeginRowIndex(currentPageNo, rowCountPerPage) + rowCountPerPage - 1);
    }
    
    private static Long calculateDataPageCount(int rowCountPerPage, int maxPageCount, Long allRowCount)
    {
        return Math.min(allRowCount / rowCountPerPage + (allRowCount % rowCountPerPage > 0 ? 1 : 0), maxPageCount);
    }
    
    private static Long calculateBeginShowPageNo(Long currentPageNo, int showPageCount, Long dataPageCount)
    {
        Long beginShowPageNo;
        Long endShowPageNo;
        Long overPageCount;
        
        endShowPageNo = currentPageNo + showPageCount / 2;
        overPageCount = endShowPageNo - dataPageCount;
        beginShowPageNo  = currentPageNo - showPageCount / 2 + 1;
        beginShowPageNo  = beginShowPageNo - (overPageCount > 0 ? overPageCount : 0);
        beginShowPageNo  = Math.max(1, beginShowPageNo);
        return beginShowPageNo;
    }
    
    private static Long calculateEndShowPageNo(Long currentPageNo, int showPageCount, Long dataPageCount)
    {
        Long beginShowPageNo;
        Long endShowPageNo;
        
        beginShowPageNo  = currentPageNo - showPageCount / 2 + 1;
        endShowPageNo = currentPageNo + showPageCount / 2;
        endShowPageNo = endShowPageNo + (beginShowPageNo <= 0 ? Math.abs(beginShowPageNo) + 1 : 0);
        endShowPageNo = Math.min(dataPageCount, endShowPageNo);
        return endShowPageNo;
    }
    
    private static String generateQueryString(Map<String, String> queryStrings)
    {
        try {
        List<String> temp = new ArrayList<>();
            for(Entry<String,String> entry : queryStrings.entrySet()) {
                temp.add(URLEncoder.encode(entry.getKey(),"UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
            return (temp.size() > 0 ? "&" + String.join("&", temp) : "");
        } catch(UnsupportedEncodingException ex) {
            return "";
        }
    }
}
