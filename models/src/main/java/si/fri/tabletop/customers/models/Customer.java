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

    @Id
    private String username;

    // Password will be hashed
    private String password;

    private String email;

    private String firstName;

    private String surname;

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return firstName;
    }

    public void setName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

}
