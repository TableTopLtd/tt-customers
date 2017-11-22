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

    // TODO: Change when we have config server
    //@Inject
    //@DiscoverService("tt-menus")
    //private Optional<String> baseUrl;
    private String baseUrl = "http://localhost:8081";

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

    public Customer getCustomer(String placeId) {

        Customer customer = em.find(Customer.class, placeId);

        if (customer == null) {
            throw new NotFoundException();
        }

        return customer;
    }

    public Customer createCustomer(Customer customer) {

        try {
            beginTx();
            em.persist(customer);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return customer;
    }

    public Customer putCustomer(String placeId, Customer customer) {

        Customer c = em.find(Customer.class, placeId);

        if (c == null) {
            return null;
        }

        try {
            beginTx();
            customer.setId(c.getId());
            customer = em.merge(customer);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return customer;
    }

    public boolean deleteCustomer(String placeId) {

        Customer customer = em.find(Customer.class, placeId);

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
