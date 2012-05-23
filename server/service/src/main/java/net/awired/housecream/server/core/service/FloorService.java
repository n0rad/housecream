package net.awired.housecream.server.core.service;

import java.util.List;
import javax.inject.Inject;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.ajsl.persistence.EntityNotFoundException;
import net.awired.housecream.server.common.domain.zone.Floor;
import net.awired.housecream.server.storage.dao.FloorDao;
import org.springframework.stereotype.Service;

@Service
public class FloorService {

    @Inject
    private FloorDao floorDao;

    public Floor saveFloor(Floor floor) {
        return floorDao.save(floor);
    }

    public Floor getFloor(Long id) throws NotFoundException {
        try {
            return floorDao.find(id);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("no Floor found for id " + id, e);
        }
    }

    public List<Floor> getAllFloors() {
        return floorDao.findAll();
    }

    public void deleteAllFloors() {
        floorDao.deleteAll();
    }

    public void deleteFloor(long id) {
        floorDao.delete(id);

    }

}
