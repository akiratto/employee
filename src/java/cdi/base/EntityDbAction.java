package cdi.base;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;
import util.Tuple;

/**
 *
 * @author Owner
 */
public interface EntityDbAction<E extends Serializable,PK> {
    public void create(Consumer<E> creater);
    public E find(PK entityId);
    public int update(PK entityId, Consumer<E> updater);
    public int delete(PK entityId);
    public int deleteAll();
    public Long countAll(E condition);
    public List<E> search(E condition, int offset, int rowCountPerPage);
}
