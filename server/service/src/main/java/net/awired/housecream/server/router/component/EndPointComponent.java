package net.awired.housecream.server.router.component;

import net.awired.housecream.server.common.domain.Point;
import org.apache.camel.CamelContext;

public interface EndPointComponent {

    void updatePointNotification(Point point, String routerUrl);

    Float getCurrentValue(Point point, CamelContext camelContext);

}
