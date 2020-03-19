package presentation.jsf.annotation.dynamic;

import java.lang.annotation.Annotation;
import presentation.jsf.type.JsfUIOrderType;
import presentation.jsf.annotation.JsfUIListColumnOrder;

/**
 *
 * @author Owner
 */
public class DynJsfUIListColumnOrder implements JsfUIListColumnOrder {
    private JsfUIOrderType orderType;
    private int orderSequence;
    
    public DynJsfUIListColumnOrder(JsfUIOrderType orderType, int orderSequence)
    {
        this.orderType = orderType;
        this.orderSequence = orderSequence;
    }
    
    @Override
    public JsfUIOrderType orderType() {
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
