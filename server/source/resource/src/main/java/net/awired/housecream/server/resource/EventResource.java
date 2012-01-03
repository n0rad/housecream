package net.awired.housecream.server.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import net.awired.housecream.server.core.service.EntryService;
import net.awired.housecream.server.storage.entity.EndPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/event")
public class EventResource {

    @Autowired
    private EntryService entryService;

    @GET
    public EndPoint event() {
        EndPoint endPoint = new EndPoint();
        endPoint.setName("salut");
        entryService.test(endPoint);
        return endPoint;
    }
}
