package net.awired.housecream.server.core.router;

import java.util.Map;
import javax.inject.Inject;
import net.awired.housecream.server.core.domain.HcEvent;
import net.awired.housecream.server.storage.dao.InPointDao;
import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.camel.Headers;
import org.apache.camel.Properties;

public class HcDynamicRouteFinder {

    @Inject
    private InPointDao inPoinDao;

    private int i = 0;

    public String slip(Exchange exchange, @Body HcEvent body, @Header("STEP") String previousStep,
            @Properties Map<String, Object> properties, @Headers Map<String, Object> headers) {
        if (i > 0) {
            System.out.println("ENDEDDDDD");
            return null;
        }
        System.out.println("yopla" + body + " -- " + i++);
        //        return "cxfrs:http://localhost:5879?serviceClass=net.awired.restmcu.api.resource.client.RestMcuPinResource";

        //

        return "direct:toto42";
        //        return "cxfrs://bean://rsClient";
    }
}
