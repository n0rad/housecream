package net.awired.housecream.server.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.activation.DataHandler;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
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
import net.awired.housecream.server.engine.EngineProcessor;
import net.awired.housecream.server.storage.dao.InPointDao;
import net.awired.housecream.server.storage.dao.OutPointDao;
import net.awired.housecream.server.storage.dao.ZoneDao;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import com.google.common.io.ByteStreams;

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
    private EngineProcessor engine;

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
        zoneDao.save(zone);
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
    public void uploadImage(long zoneId, MultipartBody body) throws NotFoundException {
        List<Attachment> allAttachments = body.getAllAttachments();
        Zone zone = zoneDao.find(zoneId);
        DataHandler dataHandler = allAttachments.get(0).getDataHandler();
        try {
            InputStream inputStream = dataHandler.getInputStream();
            zone.setImage(ByteStreams.toByteArray(inputStream));
            zone.setImageMime(dataHandler.getContentType());
            zoneDao.save(zone);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read data", e);
        }
    }

    @Override
    public Response getImage(long zoneId) throws NotFoundException {
        Zone find = zoneDao.find(zoneId);
        return Response.ok(find.getImage()).type(find.getImageMime()).build();
    }

    @Override
    public List<InPoint> inPoints(long zoneId) {
        List<InPoint> findByZone = inPointDao.findByZone(zoneId);
        for (InPoint inPoint : findByZone) {
            try {
                inPoint.setValue(engine.getPointState(inPoint.getId()));
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
                outPoint.setValue(engine.getPointState(outPoint.getId()));
            } catch (NotFoundException e) {
                // nothing to do if we don't have the value in holder
            }
        }

        return findByZone;
    }

}
