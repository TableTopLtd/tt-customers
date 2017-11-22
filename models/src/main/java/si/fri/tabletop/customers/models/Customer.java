package si.fri.tabletop.customers.models;

import org.eclipse.persistence.annotations.UuidGenerator;

import javax.persistence.*;

@Entity(name = "customers")
@NamedQueries(value =
        {
                @NamedQuery(name = "Customer.getAll", query = "SELECT p FROM customers p"),
        })
@UuidGenerator(name = "idGenerator")
public class Customer {
    // TODO: Adjust for customer
    @Id
    @GeneratedValue(generator = "idGenerator")
    private String id;

    private String title;

    private String description;

    private String address;

    @Column(name = "postal_code")
    private int postalCode;

    @Column(name = "country_code")
    private String countryCode;


    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

}
