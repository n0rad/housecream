package net.awired.housecream.server.router;

import java.util.List;
import net.awired.housecream.server.engine.Action;
import net.awired.housecream.server.engine.Actions;
import org.apache.camel.Body;
import org.springframework.stereotype.Component;

@Component
public class EngineResultSplitter {

    public List<Action> split(@Body Actions actions) throws Exception {
        return actions.getActions();
    }

}
