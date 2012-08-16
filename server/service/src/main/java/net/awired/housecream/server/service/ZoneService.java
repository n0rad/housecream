package net.awired.housecream.server.service;

import java.util.List;
import javax.inject.Inject;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.client.bean.validation.js.domain.ClientValidatorInfo;
import net.awired.housecream.server.common.domain.inpoint.InPoint;
import net.awired.housecream.server.common.domain.zone.Zone;
import net.awired.housecream.server.common.resource.ZoneResource;
import net.awired.housecream.server.storage.dao.InPointDao;
import net.awired.housecream.server.storage.dao.ZoneDao;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ZoneService implements ZoneResource {

    @Inject
    private ZoneDao zoneDao;

    @Inject
    private InPointDao inPointDao;

    @Override
    public ClientValidatorInfo getZoneValidator() {
        // TODO Auto-generated method stub
        return null; // TODO handle inheritance
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
    public List<InPoint> inpoints(long zoneId) {
        return inPointDao.findByZone(zoneId);
    }
}
