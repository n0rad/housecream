package net.awired.housecream.server.core.router;

import javax.inject.Inject;
import net.awired.housecream.server.storage.dao.InPointDao;
import org.apache.camel.Exchange;

public class HcDynamicRouteFinder {

    @Inject
    private InPointDao inPoinDao;

    private int i = 0;

    public String slip(Exchange exchange /*
                                          * , @Body HcEvent body, @Header("STEP") String previousStep,
                                          * 
                                          * @Properties Map<String, Object> properties, @Headers Map<String, Object>
                                          * headers
                                          */) {
        //        if (i > 0) {
        //            System.out.println("ENDEDDDDD");
        //            return null;
        //        }
        System.out.println("yopla" + " -- " + i++);
        //        return "cxfrs:http://localhost:5879?serviceClass=net.awired.restmcu.api.resource.client.RestMcuPinResource";
        return null;
    }
}
