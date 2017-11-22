package si.fri.tabletop.customers.services;

import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import si.fri.tabletop.customers.models.Customer;
import si.fri.tabletop.customers.services.config.RestProperties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.UriInfo;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@ApplicationScoped
public class CustomersBean {

    private Logger log = LogManager.getLogger(CustomersBean.class.getName());

    @Inject
    private RestProperties restProperties;

    @Inject
    private EntityManager em;

    @Inject
    private CustomersBean customersBean;

    private Client httpClient;

    // TODO: Change when we have config server, at the moment we dont need
    //@Inject
    //@DiscoverService("tt-menus")
    //private Optional<String> baseUrl;
    private String baseUrl = "http://localhost:8081";

    // Currently not used
    public static final String SALT = "xxx";

    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
    }

    public List<Customer> getCustomers(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery())
                .defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, Customer.class, queryParameters);

    }

    public Customer getCustomer(String customerUsername) {

        Customer customer = em.find(Customer.class, customerUsername);

        if (customer == null) {
            throw new NotFoundException();
        }

        return customer;
    }

    public Customer createCustomer(Customer customer) {

        // hash password - maybe for future.
        /*
        String saltedPassword = SALT + customer.getPassword();
        customer.setPassword(generateHash(saltedPassword));
        */

        try {
            beginTx();
            em.persist(customer);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return customer;
    }

    public Customer putCustomer(String customerUsername, Customer customer) {

        Customer c = em.find(Customer.class, customerUsername);

        if (c == null) {
            return null;
        }

        try {
            beginTx();
            customer.setUsername(c.getUsername());
            customer = em.merge(customer);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return customer;
    }

    public boolean deleteCustomer(String username) {

        Customer customer = em.find(Customer.class, username);

        if (customer != null) {
            try {
                beginTx();
                em.remove(customer);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else
            return false;

        return true;
    }

    public Boolean loginCustomer(String username, String password) {
        // Currently not used.
        // From https://dzone.com/articles/storing-passwords-java-web
        Boolean isAuthenticated = false;

        // remember to use the same SALT value use used while storing password
        // for the first time.
        String saltedPassword = SALT + password;
        String hashedPassword = generateHash(saltedPassword);

        Customer customer = em.find(Customer.class, username);
        String storedPasswordHash = customer.getPassword();

        if(hashedPassword.equals(storedPasswordHash)){
            isAuthenticated = true;
        }else{
            isAuthenticated = false;
        }
        return isAuthenticated;
    }

    private static String generateHash(String input) {
        // Currently not used.
        // From https://dzone.com/articles/storing-passwords-java-web
        StringBuilder hash = new StringBuilder();

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = sha.digest(input.getBytes());
            char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f' };
            for (int idx = 0; idx < hashedBytes.length; ++idx) {
                byte b = hashedBytes[idx];
                hash.append(digits[(b & 0xf0) >> 4]);
                hash.append(digits[b & 0x0f]);
            }
        } catch (NoSuchAlgorithmException e) {
            // handle error here.
        }

        return hash.toString();
    }

    private void beginTx() {
        if (!em.getTransaction().isActive())
            em.getTransaction().begin();
    }

    private void commitTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().commit();
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
    }
}
