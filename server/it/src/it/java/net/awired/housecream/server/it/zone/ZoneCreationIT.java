package net.awired.housecream.server.it.zone;

import static org.fest.assertions.api.Assertions.assertThat;
import javax.validation.ValidationException;
import net.awired.housecream.server.api.domain.zone.Zones;
import net.awired.housecream.server.it.HcWsItServer;
import net.awired.housecream.server.it.builder.zone.AreaBuilder;
import net.awired.housecream.server.it.builder.zone.BuildingBuilder;
import net.awired.housecream.server.it.builder.zone.FloorBuilder;
import net.awired.housecream.server.it.builder.zone.LandBuilder;
import net.awired.housecream.server.it.builder.zone.RoomBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ZoneCreationIT {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Rule
    public HcWsItServer hc = new HcWsItServer();

    @Test
    public void should_not_create_zone_without_name() {
        exception.expect(ValidationException.class);
        exception.expectMessage(".name");

        hc.zoneResource().createZone(new LandBuilder().build());
    }

    @Test
    public void should_create_land_without_parent() {
        long createZone = hc.zoneResource().createZone(new LandBuilder().name("mylandname").build());

        assertThat(createZone).isNotEqualTo(0);
    }

    @Test
    public void should_not_create_land_with_parent() {
        long landId = hc.zoneResource().createZone(new LandBuilder().name("mylandname").build());
        long buildId = hc.zoneResource().createZone(new BuildingBuilder().name("A").parentId(landId).build());

        exception.expect(ValidationException.class);
        exception.expectMessage("javax.validation.constraints.Null.message");

        hc.zoneResource().createZone(new LandBuilder().name("mylandname").parentId(buildId).build());
    }

    @Test
    public void should_not_create_building_without_parent() {
        exception.expect(ValidationException.class);
        exception.expectMessage(".parentId");

        hc.zoneResource().createZone(new BuildingBuilder().name("myBuilding").build());
    }

    @Test
    public void should_create_building_with_land_as_parent() {
        long landId = hc.zoneResource().createZone(new LandBuilder().name("myland").build());

        long buildId = hc.zoneResource().createZone(new BuildingBuilder().name("A").parentId(landId).build());

        assertThat(buildId).isNotEqualTo(0);
    }

    @Test
    public void should_not_create_building_without_building_as_parent() {
        long landId = hc.zoneResource().createZone(new LandBuilder().name("myland").build());
        long buildId = hc.zoneResource().createZone(new BuildingBuilder().name("A").parentId(landId).build());
        exception.expect(ValidationException.class);
        exception.expectMessage(".parentId");
        hc.zoneResource().createZone(new BuildingBuilder().name("B").parentId(buildId).build());
    }

    @Test
    public void should_create_hierachical_zone() {
        long landId = hc.zoneResource().createZone(new LandBuilder().name("myland").build());
        long buildId = hc.zoneResource().createZone(new BuildingBuilder().name("A").parentId(landId).build());
        long floorId = hc.zoneResource().createZone(new FloorBuilder().name("B").parentId(buildId).build());
        long roomId = hc.zoneResource().createZone(new RoomBuilder().name("C").parentId(floorId).build());
        hc.zoneResource().createZone(new AreaBuilder().name("D").parentId(roomId).build());

        Zones zones = hc.zonesResource().getZones(42, null, null, null, null);

        assertThat(zones.getZones().size()).isEqualTo(5);
    }

}
