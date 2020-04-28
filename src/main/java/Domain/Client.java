package Domain;
import Domain.BaseEntity;

public class Client extends BaseEntity<Long> {
    private String name;
    private String address;
    private Long cnp;



    /**
     * @param name
     *              must not be null
     * @param adr
     *              must not be null
     */
    public Client( String name, String adr,Long cnp) {
        this.name = name;
        this.address = adr;
        this.cnp = cnp;
    }

    public Client() {

    }

    /**
     * @return an {@code String}
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return an {@code String}
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * Sets the name of the client entity.
     * @param name
     *              must not be null
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the address of the client entity.
     * @param addr
     *              must not be null
     */
    public void setAddress(String addr) {
        this.address = addr;
    }

    public Long getCnp() {
        return cnp;
    }

    public void setCnp(Long cnp) {
        this.cnp = cnp;
    }

    /**
     * @param o not null
     * @return an {@code boolean}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;


        if (!name.equals(client.name)) return false;
        return address.equals(client.address);

    }

    @Override
    public String toString() {
        return "Name: " + this.name +
                "\nAddress: " + this.address +
                "\nCnp: " + String.valueOf(this.cnp) + "\n";
    }

    @Override
    public int hashCode() {
        int result = address.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
