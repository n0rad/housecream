package net.awired.housecream.server.resource;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import net.awired.housecream.server.core.service.EntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/event")
public class InEventResource {

    @Autowired
    private EntryService entryService;

    @PUT
    @Path("/{deviceId}/{devicePointId}")
    public void putEventWithDevicePointId() {

    }

}
