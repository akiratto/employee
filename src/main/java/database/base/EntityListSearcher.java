package database.base;

import application.dependent.PageNavigator;
import database.entity.TableEmployee;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Owner
 */
public interface EntityListSearcher<E extends Serializable> {
    public List<E> search(E condition, PageNavigator pageNavigator);
    public Long count(E condition);
}
