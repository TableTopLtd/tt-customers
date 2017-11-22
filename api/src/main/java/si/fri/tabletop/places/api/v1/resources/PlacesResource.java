package si.fri.tabletop.places.api.v1.resources;

import si.fri.tabletop.places.models.Place;
import si.fri.tabletop.places.services.PlacesBean;
import si.fri.tabletop.places.services.config.RestProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@ApplicationScoped
@Path("/places")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PlacesResource {

    @Context
    private UriInfo uriInfo;

    @Inject
    private PlacesBean placesBean;

    @Inject
    private RestProperties restProperties;

    @GET
    public Response getPlaces() {

        List<Place> places = placesBean.getPlaces(uriInfo);

        return Response.ok(places).build();
    }

    @GET
    @Path("/{placeId}")
    public Response getPlace(@PathParam("placeId") String placeId) {

        Place place = placesBean.getPlace(placeId);

        if (place == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(place).build();
    }

    @POST
    public Response createPlace(Place place) {

        if (place.getTitle() == null || place.getTitle().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            place = placesBean.createPlace(place);
        }

        if (place.getId() != null) {
            return Response.status(Response.Status.CREATED).entity(place).build();
        } else {
            return Response.status(Response.Status.CONFLICT).entity(place).build();
        }
    }

    @PUT
    @Path("{placeId}")
    public Response putPlace(@PathParam("placeId") String placeId, Place place) {

        place = placesBean.putPlace(placeId, place);

        if (place == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            if (place.getId() != null)
                return Response.status(Response.Status.OK).entity(place).build();
            else
                return Response.status(Response.Status.NOT_MODIFIED).build();
        }
    }

    @DELETE
    @Path("{placeId}")
    public Response deletePlace(@PathParam("placeId") String placeId) {

        boolean deleted = placesBean.deletePlace(placeId);

        if (deleted) {
            return Response.status(Response.Status.GONE).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/config")
    public Response config() {
        String response =
                "{\n" +
                        "    \"endpointEnabled\": \"%b\"\n" +
                        "}";
        response = String.format(response, restProperties.isMenuServiceEnabled());
        return Response.ok(response).build();
    }

}
