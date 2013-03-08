package net.awired.housecream.server.it.api;

import static net.awired.housecream.server.it.builder.InPointBuilder.in;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.api.domain.inpoint.InPointType;
import net.awired.housecream.server.api.domain.zone.Zone;
import net.awired.housecream.server.api.resource.InPointResource;
import net.awired.housecream.server.api.resource.InPointsResource;
import net.awired.housecream.server.api.resource.PluginNotFoundException;
import net.awired.housecream.server.it.HcsItSession;

public class InpointApi {

    private final HcsItSession session;

    public InpointApi(HcsItSession session) {
        this.session = session;
    }

    public InPoint create(String name, Zone zone, InPointType type, String uri) throws PluginNotFoundException {
        InPoint inpoint = session.getServer().getResource(InPointResource.class, session)
                .createInPoint(in().name(name).uri(uri).type(type).zoneId(zone.getId()).build());
        return inpoint;
    }

    public void deleteAll() {
        session.getServer().getResource(InPointsResource.class, session).deleteAllInPoints();
    }

    public InPoint getPoint(long pointId) throws NotFoundException {
        return session.getServer().getResource(InPointResource.class, session).getInPoint(pointId);
    }

    public InPointResource internalInpointResource() {
        return session.getServer().getResource(InPointResource.class, session);
    }
}
