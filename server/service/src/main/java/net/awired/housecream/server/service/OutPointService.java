package net.awired.housecream.server.service;

import javax.inject.Inject;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.client.bean.validation.js.domain.ClientValidatorInfo;
import net.awired.client.bean.validation.js.service.ValidationService;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.resource.OutPointResource;
import net.awired.housecream.server.api.resource.PluginNotFoundException;
import net.awired.housecream.server.engine.EngineProcessor;
import net.awired.housecream.server.storage.dao.OutPointDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional
public class OutPointService implements OutPointResource {

    @Inject
    private OutPointDao pointDao;

    @Inject
    private EngineProcessor engine;

    @Inject
    private ValidationService validationService;

    @Inject
    private PluginService pluginService;

    @Override
    public OutPoint createOutPoint(OutPoint outPoint) throws PluginNotFoundException {
        pluginService.validateAndNormalizeURI(outPoint.getUri());
        pointDao.save(outPoint);
        return outPoint;
    }

    @Override
    public Float getPointValue(long pointId) throws NotFoundException {
        return engine.getPointState(pointId);
    }

    @Override
    public ClientValidatorInfo getOutPointValidator() {
        return validationService.getValidatorInfo(InPoint.class);
    }

    @Override
    public OutPoint getOutPoint(long outPointId) throws NotFoundException {
        OutPoint find = pointDao.find(outPointId);
        try {
            find.setValue(engine.getPointState(outPointId));
        } catch (NotFoundException e) {
            // no current value for point
        }
        return find;
    }

    @Override
    public void deleteOutPoint(long outPointId) {
        try {
            OutPoint point = pointDao.find(outPointId);
            pointDao.delete(point.getId());
        } catch (NotFoundException e) {
            // nothing to do if trying to remove a point that is already not there
        }
    }

    @Override
    public void setValue(long outPointId, Float value) {
        // TODO Auto-generated method stub
    }
}
