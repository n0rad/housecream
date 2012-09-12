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
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.client.bean.validation.js.domain.ClientValidatorInfo;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.api.domain.zone.Zone;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

@Path("/zone")
public interface ZoneResource {
    @GET
    @Path("/validator")
    public ClientValidatorInfo getZoneValidator();

    @PUT
    long createZone(@Valid Zone zone) throws RuntimeException;

    @GET
    @Path("/{id}")
    Zone getZone(@PathParam("id") long zoneId) throws NotFoundException;

    @DELETE
    @Path("/{id}")
    void deleteZone(@PathParam("id") long zoneId);

    @POST
    @Path("/test")
    @Consumes("multipart/form-data")
    public String upload(MultipartBody body);

    @GET
    @Path("/{id}/inpoints")
    public List<InPoint> inpoints(@PathParam("id") long zoneId);

}
