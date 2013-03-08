package net.awired.housecream.server.it.api;

import static net.awired.housecream.server.it.builder.OutPointBuilder.out;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.outPoint.OutPointType;
import net.awired.housecream.server.api.domain.zone.Zone;
import net.awired.housecream.server.api.resource.OutPointResource;
import net.awired.housecream.server.api.resource.OutPointsResource;
import net.awired.housecream.server.api.resource.PluginNotFoundException;
import net.awired.housecream.server.it.HcsItSession;

public class OutpointApi {

    private final HcsItSession session;

    public OutpointApi(HcsItSession session) {
        this.session = session;
    }

    public OutPoint create(String name, Zone zone, OutPointType type, String uri) throws PluginNotFoundException {
        OutPoint outpoint = session.getServer().getResource(OutPointResource.class, session)
                .createOutPoint(out().name(name).uri(uri).type(type).zoneId(zone.getId()).build());
        return outpoint;
    }

    public void deleteAll() {
        session.getServer().getResource(OutPointsResource.class, session).deleteAllOutPoints();
    }

    public void setValue(OutPoint outpoint, float value) throws Exception {
        session.getServer().getResource(OutPointResource.class, session).setValue(outpoint.getId(), value);
    }
}
