package net.awired.housecream.server.router;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
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

    public List<Message> split(@Body Actions actions) throws Exception {
        ArrayList<Message> res = new ArrayList<Message>();

        for (ConsequenceAction action : actions.getActions()) {
            OutPoint outpoint = outputDao.find(action.getPointId());

            //            outpoint.getPrefix();
            //            HousecreamPlugin plugin = pluginService.getPluginFromPrefix(null);
            //            Message message = plugin.buildOutputMessage(action, outpoint);
            //            res.add(message);
        }

        return res;
    }

}
