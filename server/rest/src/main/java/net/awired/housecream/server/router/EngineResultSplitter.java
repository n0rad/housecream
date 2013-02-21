package net.awired.housecream.server.router;

import java.util.List;
import net.awired.housecream.server.engine.Action;
import net.awired.housecream.server.engine.Actions;
import org.apache.camel.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EngineResultSplitter {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public List<Action> split(@Body Actions actions) throws Exception {
        log.debug("splitting actions {}", actions);
        return actions.getActions();
    }
}
