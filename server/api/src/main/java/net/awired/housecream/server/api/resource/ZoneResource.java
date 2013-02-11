package net.awired.housecream.server.api.resource;

import java.util.List;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.zone.Zone;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

@Path("/zones/{id}")
public interface ZoneResource {

    @PUT
    Zone updateZone(@PathParam("id") long zoneId, @Valid Zone zone) throws NotFoundException;

    @GET
    Zone getZone(@PathParam("id") long zoneId) throws NotFoundException;

    @DELETE
    void deleteZone(@PathParam("id") long zoneId);

    @POST
    @Path("/image")
    @Consumes("multipart/form-data")
    void uploadImage(@PathParam("id") long zoneId, MultipartBody body) throws NotFoundException;

    @GET
    @Path("/image")
    Response getImage(@PathParam("id") long zoneId) throws NotFoundException;

    @GET
    @Path("/inpoints")
    List<InPoint> getInPoints(@PathParam("id") long zoneId);

    @GET
    @Path("/outpoints")
    List<OutPoint> getOutPoints(@PathParam("id") long zoneId);

}
