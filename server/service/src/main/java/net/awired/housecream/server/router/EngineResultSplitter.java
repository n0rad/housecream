package net.awired.housecream.server.router;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import net.awired.ajsl.core.lang.Pair;
import net.awired.housecream.plugins.api.HousecreamPlugin;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.engine.Actions;
import net.awired.housecream.server.engine.ConsequenceAction;
import net.awired.housecream.server.service.PluginService;
import net.awired.housecream.server.storage.dao.OutPointDao;
import org.apache.camel.Body;
import org.apache.camel.Message;
import org.springframework.stereotype.Component;

@Component
public class EngineResultSplitter {

    @Inject
    private OutPointDao outputDao;

    @Inject
    private PluginService pluginService;

    @Inject
    private OutDynamicRouter outRouter;

    public List<Message> split(@Body Actions actions) throws Exception {
        ArrayList<Message> res = new ArrayList<Message>();
        for (ConsequenceAction action : actions.getActions()) {
            OutPoint outpoint = outputDao.find(action.getPointId());
            HousecreamPlugin plugin = pluginService.getPluginFromPrefix(outpoint.extractUrlPrefix());
            Pair<Object, Map<String, Object>> bodyAndHeaders = plugin.prepareOutBodyAndHeaders(action, outpoint);
            Message outMessage = outRouter.buildMessage(bodyAndHeaders, outpoint, action);
            res.add(outMessage);
        }
        return res;
    }

}
