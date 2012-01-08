package net.awired.housecream.client.stub;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import net.awired.housecream.client.common.domain.HccError;
import net.awired.housecream.client.common.resource.NotFoundException;
import org.springframework.stereotype.Component;

@Component
@Provider
public class HccNotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException ex) {
        HccError error = new HccError();
        error.setErrorClass(ex.getClass().getName());
        error.setMessage(ex.getMessage());
        return Response.status(Status.NOT_FOUND).entity(error).build();
    }

}
