package net.awired.housecream.server.router;

import java.util.ArrayList;
import java.util.List;
import net.awired.housecream.server.api.domain.rule.Consequence;
import net.awired.housecream.server.engine.Actions;
import org.apache.camel.Body;
import org.springframework.stereotype.Component;

@Component
public class EngineResultSplitter {

    public List<Consequence> split(@Body Actions actions) throws Exception {
        ArrayList<Consequence> res = new ArrayList<Consequence>();
        for (Consequence action : actions.getActions()) {
            res.add(action);
        }
        return res;
    }

}
