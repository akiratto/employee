package database.type;

import database.dependent.EntityCRUDService;
import static database.type.JPQLOrderType.DESCENDING;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import presentation.jsf.annotation.JsfUIListColumnOrder;
import presentation.jsf.base.JsfEntityListSession;
import presentation.jsf.type.JsfUIColumnSetting;
import util.Tuple;

/**
 *
 * @author Owner
 */
public class JPQLOrderBy {
    private Map<String, JPQLOrderType> orderTypes = new HashMap<>();
    
    public static JPQLOrderBy getInstance()
    {
        return new JPQLOrderBy();
    }
    
    public JPQLOrderBy order(String fieldName, JPQLOrderType orderType)
    {
        this.orderTypes.put(fieldName, orderType);
        return this;
    }
    
    public String build(boolean withOrderByPhrase)
    {
        String orderSet = this.orderTypes.entrySet()
                            .stream()
                            .map(entry -> entry.getKey() + " " + entry.getValue().jpqlName())
                            .collect(Collectors.joining(","));
        return orderSet.isEmpty() ? ""
                : (withOrderByPhrase ? "ORDER BY " : "") + orderSet;
    }
}
