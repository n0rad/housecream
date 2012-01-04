package net.awired.housecream.server.core.service;

import javax.inject.Inject;
import net.awired.housecream.server.storage.dao.FloorDao;
import net.awired.housecream.server.storage.entity.Floor;
import org.springframework.stereotype.Service;

@Service
public class FloorService {

    @Inject
    private FloorDao floorDao;

    public Floor addFloor(Floor floor) {
        return this.floorDao.save(floor);
    }

}
