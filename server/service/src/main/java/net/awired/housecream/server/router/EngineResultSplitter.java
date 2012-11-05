package net.awired.housecream.server.router;

import java.util.ArrayList;
import java.util.List;
import net.awired.housecream.server.engine.Actions;
import net.awired.housecream.server.engine.ConsequenceAction;
import org.apache.camel.Body;
import org.springframework.stereotype.Component;

@Component
public class EngineResultSplitter {

    public List<ConsequenceAction> split(@Body Actions actions) throws Exception {
        ArrayList<ConsequenceAction> res = new ArrayList<ConsequenceAction>();
        for (ConsequenceAction action : actions.getActions()) {
            res.add(action);
        }
        return res;
    }

}
