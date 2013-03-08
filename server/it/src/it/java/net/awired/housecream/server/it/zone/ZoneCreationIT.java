package net.awired.housecream.server.it.zone;

import net.awired.housecream.server.it.HcsItServer;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class ZoneCreationIT {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Rule
    public HcsItServer hcs = new HcsItServer();

    //    @Test
    //    public void should_not_create_zone_without_name() {
    //        exception.expect(ValidationException.class);
    //        exception.expectMessage(".name");
    //
    //        hcs.zoneResource().createZone(new LandBuilder().build());
    //    }
    //
    //    @Test
    //    public void should_create_land_without_parent() {
    //        long createZone = hcs.zoneResource().createZone(new LandBuilder().name("mylandname").build());
    //
    //        assertThat(createZone).isNotEqualTo(0);
    //    }
    //
    //    @Test
    //    public void should_not_create_land_with_parent() {
    //        long landId = hcs.zoneResource().createZone(new LandBuilder().name("mylandname").build());
    //        long buildId = hcs.zoneResource().createZone(new BuildingBuilder().name("A").parentId(landId).build());
    //
    //        exception.expect(ValidationException.class);
    //        exception.expectMessage("javax.validation.constraints.Null.message");
    //
    //        hcs.zoneResource().createZone(new LandBuilder().name("mylandname").parentId(buildId).build());
    //    }
    //
    //    @Test
    //    public void should_not_create_building_without_parent() {
    //        exception.expect(ValidationException.class);
    //        exception.expectMessage(".parentId");
    //
    //        hcs.zoneResource().createZone(new BuildingBuilder().name("myBuilding").build());
    //    }
    //
    //    @Test
    //    public void should_create_building_with_land_as_parent() {
    //        long landId = hcs.zoneResource().createZone(new LandBuilder().name("myland").build());
    //
    //        long buildId = hcs.zoneResource().createZone(new BuildingBuilder().name("A").parentId(landId).build());
    //
    //        assertThat(buildId).isNotEqualTo(0);
    //    }
    //
    //    @Test
    //    public void should_not_create_building_without_building_as_parent() {
    //        long landId = hcs.zoneResource().createZone(new LandBuilder().name("myland").build());
    //        long buildId = hcs.zoneResource().createZone(new BuildingBuilder().name("A").parentId(landId).build());
    //        exception.expect(ValidationException.class);
    //        exception.expectMessage(".parentId");
    //        hcs.zoneResource().createZone(new BuildingBuilder().name("B").parentId(buildId).build());
    //    }
    //
    //    @Test
    //    public void should_create_hierachical_zone() {
    //        long landId = hcs.zoneResource().createZone(new LandBuilder().name("myland").build());
    //        long buildId = hcs.zoneResource().createZone(new BuildingBuilder().name("A").parentId(landId).build());
    //        long floorId = hcs.zoneResource().createZone(new FloorBuilder().name("B").parentId(buildId).build());
    //        long roomId = hcs.zoneResource().createZone(new RoomBuilder().name("C").parentId(floorId).build());
    //        hcs.zoneResource().createZone(new AreaBuilder().name("D").parentId(roomId).build());
    //
    //        Zones zones = hcs.zonesResource().getZones(42, null, null, null, null);
    //
    //        assertThat(zones.getZones().size()).isEqualTo(5);
    //    }

}
