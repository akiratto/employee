package database.type;

import database.dependent.EntityCRUDService;
import static database.type.JPQLOrderType.DESCENDING;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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
    private final List<Tuple<String, JPQLOrderType>> orderTypes = new ArrayList<>();
    
    public static JPQLOrderBy getInstance()
    {
        return new JPQLOrderBy();
    }
    
    public JPQLOrderBy order(String fieldName, JPQLOrderType orderType)
    {
        this.orderTypes.add(new Tuple<>(fieldName, orderType));
        return this;
    }
    
    public String build(boolean withOrderByPhrase)
    {
        String orderSet = this.orderTypes
                            .stream()
                            .map(tp -> tp._1 + " " + tp._2.jpqlName())
                            .collect(Collectors.joining(","));
        return orderSet.isEmpty() ? ""
                : (withOrderByPhrase ? "ORDER BY " : "") + orderSet;
    }
}
