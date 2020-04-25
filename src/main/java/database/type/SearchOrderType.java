package database.type;

/**
 *
 * @author Owner
 */
public enum SearchOrderType {
    NONE,
    ASCENDING,
    DESCENDING;
    public String jpqlName()
    {
        return this.equals(ASCENDING) ? "ASC"
                : this.equals(DESCENDING) ? "DESC"
                : "";
    }
}
