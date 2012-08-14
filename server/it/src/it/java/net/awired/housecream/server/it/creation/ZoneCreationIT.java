package net.awired.housecream.server.it.creation;

import net.awired.housecream.server.common.domain.zone.Building;
import net.awired.housecream.server.common.domain.zone.Land;
import net.awired.housecream.server.common.domain.zone.Room;
import net.awired.housecream.server.it.HcsItServer;
import org.junit.Rule;
import org.junit.Test;

public class ZoneCreationIT {

    @Rule
    public HcsItServer hcs = new HcsItServer();

    @Test
    public void should_create_hierachical_zone() {
        Land land = new Land();
        land.setName("this is the land name");
        long id = hcs.zoneResource().createZone(land);

        Building building = new Building();
        building.setParentId(id);
        building.setName("name of the building");

        long buildingId = hcs.zoneResource().createZone(building);

        Room room1 = new Room();
        room1.setName("room1");
        room1.setParentId(buildingId);
        long room1Id = hcs.zoneResource().createZone(room1);

        Room room2 = new Room();
        room2.setName("room2");
        room2.setParentId(buildingId);
        long room2Id = hcs.zoneResource().createZone(room2);

        //                hcs.zonesResource().getZones(length, start, search, searchProperties, orders)
    }

}
