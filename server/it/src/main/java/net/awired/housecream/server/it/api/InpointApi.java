package net.awired.housecream.server.it.api;

import static net.awired.housecream.server.it.builder.InPointBuilder.in;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.api.domain.inpoint.InPointType;
import net.awired.housecream.server.api.domain.inpoint.InPoints;
import net.awired.housecream.server.api.domain.zone.Zone;
import net.awired.housecream.server.api.resource.InPointResource;
import net.awired.housecream.server.api.resource.InPointsResource;
import net.awired.housecream.server.api.resource.PluginNotFoundException;
import net.awired.housecream.server.it.HcWsItSession;

public class InpointApi {

    private final HcWsItSession session;

    public InpointApi(HcWsItSession session) {
        this.session = session;
    }

    public InPoint create(String name, Zone zone, InPointType type, String uri) throws PluginNotFoundException {
        InPoint inpoint = session.getServer().getResource(InPointsResource.class, session)
                .createInPoint(in().name(name).uri(uri).type(type).zoneId(zone.getId()).build());
        return inpoint;
    }

    public InPoints list() {
        return session.getServer().getResource(InPointsResource.class, session)
                .getInPoints(null, null, null, null, null);
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
