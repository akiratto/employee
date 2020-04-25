package presentation.jsf.annotation.dynamic;

import java.lang.annotation.Annotation;
import database.type.SearchOrderType;
import presentation.jsf.annotation.JsfUIListColumnOrder;

/**
 *
 * @author Owner
 */
public class DynJsfUIListColumnOrder implements JsfUIListColumnOrder {
    private SearchOrderType orderType;
    private int orderSequence;
    
    public DynJsfUIListColumnOrder(SearchOrderType orderType, int orderSequence)
    {
        this.orderType = orderType;
        this.orderSequence = orderSequence;
    }
    
    @Override
    public SearchOrderType orderType() {
        return orderType;
    }

    @Override
    public int orderSequence() {
        return orderSequence;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return DynJsfUIListColumnOrder.class;
    }
    
}
