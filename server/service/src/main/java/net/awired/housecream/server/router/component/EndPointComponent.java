package net.awired.housecream.server.router.component;

import net.awired.housecream.server.api.domain.Point;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.engine.ConsequenceAction;
import org.apache.camel.CamelContext;
import org.apache.camel.Message;

public interface EndPointComponent {

    void updatePointNotification(Point point, String routerUrl);

    Float getCurrentValue(Point point, CamelContext camelContext);

    Message buildOutputMessage(ConsequenceAction action, OutPoint outpoint);

}
