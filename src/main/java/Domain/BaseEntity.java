package Domain;


public class BaseEntity<ID> {
    private ID id;

    /**
     * Returns the ID fo the entity.
     * @return an {@code id}
     */
    public ID getId() {
        return id;
    }

    /**
     * Sets the ID of the entity.
     * @param id not null
     */
    public void setId(ID id) {
        this.id = id;
    }

    /**
     * Retruns the entity as a String.
     * @return an {@code String}
     */
    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                '}';
    }
}
