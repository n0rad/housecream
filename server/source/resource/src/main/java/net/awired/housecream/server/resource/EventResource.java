package net.awired.housecream.server.resource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import net.awired.housecream.server.core.service.EntryService;
import net.awired.housecream.server.storage.entity.EndPoint;
import org.springframework.stereotype.Component;

@Component
@Path("/entry")
public class EventResource {

    @Inject
    private EntryService entryService;

    @GET
    @Produces("text/plain")
    public String event() {
        EndPoint endPoint = new EndPoint();
        endPoint.setName("salut");
        entryService.test(endPoint);
        return "salut";
    }
}
