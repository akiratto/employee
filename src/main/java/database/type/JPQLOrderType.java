package database.type;

/**
 *
 * @author Owner
 */
public enum JPQLOrderType {
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
