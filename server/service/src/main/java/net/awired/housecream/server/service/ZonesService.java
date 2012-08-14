package net.awired.housecream.server.service;

import java.util.List;
import javax.inject.Inject;
import net.awired.ajsl.persistence.entity.Order;
import net.awired.housecream.server.common.domain.zone.Zone;
import net.awired.housecream.server.common.domain.zone.Zones;
import net.awired.housecream.server.common.resource.ZonesResource;
import net.awired.housecream.server.storage.dao.ZoneDao;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ZonesService implements ZonesResource {

    @Inject
    public ZoneDao zoneDao;

    @Override
    public Zones getZones(Integer length, Integer start, String search, List<String> searchProperties,
            List<Order> orders) {
        List<Zone> findAll = zoneDao.findAll();
        Zones zones = new Zones();
        zones.setZones(findAll);
        zones.setTotal((long) findAll.size());
        return zones;
    }

    @Override
    public void deleteAllZones() {
        zoneDao.deleteAll();
    }

}
