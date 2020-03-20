package presentation.jsf.annotation.dynamic;

import java.lang.annotation.Annotation;
import application.type.OrderType;
import presentation.jsf.annotation.JsfUIListColumnOrder;

/**
 *
 * @author Owner
 */
public class DynJsfUIListColumnOrder implements JsfUIListColumnOrder {
    private OrderType orderType;
    private int orderSequence;
    
    public DynJsfUIListColumnOrder(OrderType orderType, int orderSequence)
    {
        this.orderType = orderType;
        this.orderSequence = orderSequence;
    }
    
    @Override
    public OrderType orderType() {
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
