package net.awired.housecream.server.api.resource;

import javax.ws.rs.Path;
import net.awired.restmcu.api.resource.server.RestMcuNotifyResource;

@Path("/router" + HcRestMcuNotifyResource.INNER_ROUTE_CONTEXT)
public interface HcRestMcuNotifyResource extends RestMcuNotifyResource {
    public static final String INNER_ROUTE_CONTEXT = "/restMcuNotify";
}
