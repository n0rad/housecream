/**
 *
 *     Copyright (C) Housecream.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package org.housecream.server.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.housecream.server.api.domain.Order;
import org.housecream.server.api.domain.zone.Area;
import org.housecream.server.api.domain.zone.Building;
import org.housecream.server.api.domain.zone.Field;
import org.housecream.server.api.domain.zone.Floor;
import org.housecream.server.api.domain.zone.Land;
import org.housecream.server.api.domain.zone.Room;
import org.housecream.server.api.domain.zone.Zone;
import org.housecream.server.api.resource.ZonesResource;
import org.housecream.server.storage.dao.ZoneDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import fr.norad.client.bean.validation.js.domain.ClientValidatorInfo;
import fr.norad.client.bean.validation.js.service.ValidationService;

@Service
@Validated
public class ZonesService implements ZonesResource {

    @Autowired
    public ZoneDao zoneDao;

    @Autowired
    private ValidationService validationService;

    @Override
    public Map<String, ClientValidatorInfo> getZoneValidator() {
        Map<String, ClientValidatorInfo> validators = new HashMap<>();
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
    public List<Zone> getZones(Integer length, UUID start, String search, List<String> searchProperties,
            List<Order> orders) {
        List<Zone> zones = zoneDao.findAll();
        return zones;
    }

    @Override
    public void deleteAllZones() {
        zoneDao.deleteAll();
    }

}
