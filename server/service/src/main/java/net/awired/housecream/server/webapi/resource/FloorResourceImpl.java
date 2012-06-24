package net.awired.housecream.server.webapi.resource;

import java.util.List;
import javax.inject.Inject;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.housecream.server.common.domain.zone.Floor;
import net.awired.housecream.server.common.resource.FloorResource;
import net.awired.housecream.server.service.FloorService;
import org.springframework.stereotype.Component;

@Component
public class FloorResourceImpl implements FloorResource {

    @Inject
    private FloorService floorService;

    @Override
    public List<Floor> getAllFloors() {
        return floorService.getAllFloors();
    }

    @Override
    public Floor getFloor(long id) throws NotFoundException {
        return floorService.getFloor(id);
    }

    @Override
    public Floor createFloor(Floor floor) {
        return floorService.saveFloor(floor);
    }

    @Override
    public void deleteFloors() {
        floorService.deleteAllFloors();
    }

    @Override
    public Floor updateFloor(Floor floor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteFloor(long id) {
        floorService.deleteFloor(id);
    }

}
