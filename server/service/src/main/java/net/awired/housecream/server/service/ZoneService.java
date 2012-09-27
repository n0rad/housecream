package net.awired.housecream.server.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.client.bean.validation.js.domain.ClientValidatorInfo;
import net.awired.client.bean.validation.js.service.ValidationService;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.zone.Area;
import net.awired.housecream.server.api.domain.zone.Building;
import net.awired.housecream.server.api.domain.zone.Field;
import net.awired.housecream.server.api.domain.zone.Floor;
import net.awired.housecream.server.api.domain.zone.Land;
import net.awired.housecream.server.api.domain.zone.Room;
import net.awired.housecream.server.api.domain.zone.Zone;
import net.awired.housecream.server.api.resource.ZoneResource;
import net.awired.housecream.server.engine.StateHolder;
import net.awired.housecream.server.storage.dao.InPointDao;
import net.awired.housecream.server.storage.dao.OutPointDao;
import net.awired.housecream.server.storage.dao.ZoneDao;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional
public class ZoneService implements ZoneResource {

    @Inject
    private ZoneDao zoneDao;

    @Inject
    private InPointDao inPointDao;

    @Inject
    private OutPointDao outPointDao;

    @Inject
    private ValidationService validationService;

    @Inject
    private StateHolder stateHolder;

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
    public long createZone(Zone zone) {
        zoneDao.persist(zone);
        return zone.getId();
    }

    @Override
    public Zone getZone(long zoneId) throws NotFoundException {
        return zoneDao.find(zoneId);
    }

    @Override
    public void deleteZone(long zoneId) {
        zoneDao.delete(zoneId);
    }

    @Override
    public String upload(MultipartBody body) {
        List<Attachment> allAttachments = body.getAllAttachments();
        //        attachments.get(0).getDataHandler().get
        //        body.getAllAttachments().get(0).getDataHandler().getInputStream()
        return "SALUT";
    }

    @Override
    public List<InPoint> inPoints(long zoneId) {
        List<InPoint> findByZone = inPointDao.findByZone(zoneId);
        for (InPoint inPoint : findByZone) {
            try {
                inPoint.setValue(stateHolder.getState(inPoint.getId()));
            } catch (NotFoundException e) {
                // nothing to do if we don't have the value in holder
            }
        }

        return findByZone;
    }

    @Override
    public List<OutPoint> outPoints(long zoneId) {
        List<OutPoint> findByZone = outPointDao.findByZone(zoneId);
        for (OutPoint outPoint : findByZone) {
            try {
                outPoint.setValue(stateHolder.getState(outPoint.getId()));
            } catch (NotFoundException e) {
                // nothing to do if we don't have the value in holder
            }
        }

        return findByZone;
    }
}
