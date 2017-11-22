package si.fri.tabletop.places.services;

import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import si.fri.tabletop.places.models.dependent.Menu;
import si.fri.tabletop.places.models.Place;
import si.fri.tabletop.places.services.config.RestProperties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PlacesBean {

    private Logger log = LogManager.getLogger(PlacesBean.class.getName());

    @Inject
    private RestProperties restProperties;

    @Inject
    private EntityManager em;

    @Inject
    private PlacesBean placesBean;

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

    public List<Place> getPlaces(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery())
                .defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, Place.class, queryParameters);

    }

    public Place getPlace(String placeId) {

        Place place = em.find(Place.class, placeId);

        if (place == null) {
            throw new NotFoundException();
        }

        // TODO: Change when we have config server
        //if (restProperties.isMenuServiceEnabled()) {
        List<Menu> menus = placesBean.getMenus(placeId);
        place.setMenus(menus);
        //}

        return place;
    }

    public Place createPlace(Place place) {

        try {
            beginTx();
            em.persist(place);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return place;
    }

    public Place putPlace(String placeId, Place place) {

        Place c = em.find(Place.class, placeId);

        if (c == null) {
            return null;
        }

        try {
            beginTx();
            place.setId(c.getId());
            place = em.merge(place);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return place;
    }

    public boolean deletePlace(String placeId) {

        Place place = em.find(Place.class, placeId);

        if (place != null) {
            try {
                beginTx();
                em.remove(place);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else
            return false;

        return true;
    }


    public List<Menu> getMenus(String placeId) {

        log.info("PLACE ID: "+ placeId);
        // TODO: Change when we have config server
        //if (baseUrl.isPresent()) {
        try {
            return httpClient
                    .target(baseUrl + "/v1/menus?where=placeId:EQ:" + placeId)
                    .request().get(new GenericType<List<Menu>>() {
                    });
        }catch (WebApplicationException | ProcessingException e) {
            log.error(e);
            return new ArrayList<>();
            //throw new InternalServerErrorException();
        }
        // }

        //return new ArrayList<>();
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
