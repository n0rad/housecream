package net.awired.housecream.client.stub;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import net.awired.housecream.client.common.domain.HccError;
import org.springframework.stereotype.Component;

@Component
@Provider
public class HccUpdateExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception ex) {
        ex.printStackTrace();
        HccError error = new HccError();
        error.setErrorClass(ex.getClass().getName());
        error.setMessage(ex.getMessage());
        return Response.status(Status.FORBIDDEN).entity(error).build();
    }
}
