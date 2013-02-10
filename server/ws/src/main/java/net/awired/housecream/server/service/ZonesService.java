package net.awired.housecream.server.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import net.awired.ajsl.persistence.entity.Order;
import net.awired.client.bean.validation.js.domain.ClientValidatorInfo;
import net.awired.client.bean.validation.js.service.ValidationService;
import net.awired.housecream.server.api.domain.zone.Area;
import net.awired.housecream.server.api.domain.zone.Building;
import net.awired.housecream.server.api.domain.zone.Field;
import net.awired.housecream.server.api.domain.zone.Floor;
import net.awired.housecream.server.api.domain.zone.Land;
import net.awired.housecream.server.api.domain.zone.Room;
import net.awired.housecream.server.api.domain.zone.Zone;
import net.awired.housecream.server.api.domain.zone.Zones;
import net.awired.housecream.server.api.resource.ZonesResource;
import net.awired.housecream.server.storage.dao.ZoneDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional
public class ZonesService implements ZonesResource {

    @Inject
    public ZoneDao zoneDao;

    @Inject
    private ValidationService validationService;

    @Override
    public Map<String, ClientValidatorInfo> getZoneValidator() {
        Map<String, ClientValidatorInfo> validators = new HashMap<String, ClientValidatorInfo>();
        validators.put(Land.class.getSimpleName(), validationService.getValidatorInfo(Land.class));
        validators.put(Building.class.getSimpleName(), validationService.getValidatorInfo(Building.class));
        validators.put(Floor.class.getSimpleName(), validationService.getValidatorInfo(Floor.class));
        validators.put(Room.class.getSimpleName(), validationService.getValidatorInfo(Room.class));
        validators.put(Area.class.getSimpleName(), validationService.getValidatorInfo(Area.class));
        validators.put(Field.class.getSimpleName(), validationService.getValidatorInfo(Field.class));
        return validators;
    }

    @Override
    public Zone createZone(Zone zone) {
        zoneDao.save(zone);
        return zone;
    }

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
