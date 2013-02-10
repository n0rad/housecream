package net.awired.housecream.server.it.zone;

import static org.fest.assertions.api.Assertions.assertThat;
import javax.validation.ValidationException;
import net.awired.housecream.server.api.domain.zone.Building;
import net.awired.housecream.server.api.domain.zone.Floor;
import net.awired.housecream.server.api.domain.zone.Land;
import net.awired.housecream.server.api.domain.zone.Room;
import net.awired.housecream.server.api.domain.zone.Zone;
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

        hc.zonesResource().createZone(new LandBuilder().build());
    }

    @Test
    public void should_create_land_without_parent() {
        Land land = (Land) hc.zonesResource().createZone(new LandBuilder().name("mylandname").build());

        assertThat(land.getId()).isNotEqualTo(0);
    }

    @Test
    public void should_not_create_land_with_parent() {
        Land land = (Land) hc.zonesResource().createZone(new LandBuilder().name("mylandname").build());
        Building building = (Building) hc.zonesResource().createZone(
                new BuildingBuilder().name("A").parentId(land.getId()).build());

        exception.expect(ValidationException.class);
        exception.expectMessage("javax.validation.constraints.Null.message");

        hc.zonesResource().createZone(new LandBuilder().name("mylandname").parentId(building.getId()).build());
    }

    @Test
    public void should_not_create_building_without_parent() {
        exception.expect(ValidationException.class);
        exception.expectMessage(".parentId");

        hc.zonesResource().createZone(new BuildingBuilder().name("myBuilding").build());
    }

    @Test
    public void should_create_building_with_land_as_parent() {
        Land land = (Land) hc.zonesResource().createZone(new LandBuilder().name("myland").build());

        Zone building = hc.zonesResource().createZone(new BuildingBuilder().name("A").parentId(land.getId()).build());

        assertThat(building.getId()).isNotEqualTo(0);
    }

    @Test
    public void should_not_create_building_without_building_as_parent() {
        Land land = (Land) hc.zonesResource().createZone(new LandBuilder().name("myland").build());
        Building building = (Building) hc.zonesResource().createZone(
                new BuildingBuilder().name("A").parentId(land.getId()).build());
        exception.expect(ValidationException.class);
        exception.expectMessage(".parentId");
        hc.zonesResource().createZone(new BuildingBuilder().name("B").parentId(building.getId()).build());
    }

    @Test
    public void should_create_hierachical_zone() {
        Land land = (Land) hc.zonesResource().createZone(new LandBuilder().name("myland").build());
        Building building = (Building) hc.zonesResource().createZone(
                new BuildingBuilder().name("A").parentId(land.getId()).build());
        Floor floor = (Floor) hc.zonesResource().createZone(
                new FloorBuilder().name("B").parentId(building.getId()).build());
        Room room = (Room) hc.zonesResource().createZone(new RoomBuilder().name("C").parentId(floor.getId()).build());
        hc.zonesResource().createZone(new AreaBuilder().name("D").parentId(room.getId()).build());

        Zones zones = hc.zonesResource().getZones(42, null, null, null, null);

        assertThat(zones.getZones().size()).isEqualTo(5);
    }

}
