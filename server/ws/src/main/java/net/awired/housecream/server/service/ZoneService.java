/**
 *
 *     Housecream.org project
 *     Copyright (C) Awired.net
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
package net.awired.housecream.server.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.activation.DataHandler;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import net.awired.core.lang.exception.NotFoundException;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.zone.Zone;
import net.awired.housecream.server.api.resource.ZoneResource;
import net.awired.housecream.server.engine.EngineProcessor;
import net.awired.housecream.server.storage.dao.InPointDao;
import net.awired.housecream.server.storage.dao.OutPointDao;
import net.awired.housecream.server.storage.dao.ZoneDao;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import com.google.common.io.ByteStreams;

@Service
@Validated
public class ZoneService implements ZoneResource {

    @Inject
    private ZoneDao zoneDao;

    @Inject
    private InPointDao inPointDao;

    @Inject
    private OutPointDao outPointDao;

    @Inject
    private EngineProcessor engine;

    @Inject
    private ZonesService zonesService;

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
    public List<InPoint> getInPoints(long zoneId) {
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
    public List<OutPoint> getOutPoints(long zoneId) {
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

    @Override
    public Zone updateZone(long zoneId, Zone zone) throws NotFoundException {
        if (zoneId != zone.getId()) {
            throw new IllegalStateException("zoneId do not match payload sent");
        }
        return zonesService.createZone(zone);
    }

}
