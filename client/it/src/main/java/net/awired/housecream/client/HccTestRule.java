package net.awired.housecream.client;

import java.util.Collections;
import net.awired.housecream.client.common.domain.HccDevice;
import net.awired.housecream.client.common.domain.HccPinInfo;
import net.awired.housecream.client.common.resource.HCCResponseExceptionMapper;
import net.awired.housecream.client.common.resource.HccResource;
import net.awired.housecream.client.common.resource.PinResource;
import net.awired.housecream.client.common.test.DefaultTestDomainHelper;
import net.awired.housecream.client.it.HccItServer;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.rules.ExternalResource;

public class HccTestRule extends ExternalResource {

    private HccResource hccResource;

    private PinResource pinResource;

    public HccTestRule() {
        HCCResponseExceptionMapper exceptionMapper = new HCCResponseExceptionMapper();
        pinResource = JAXRSClientFactory.create(HccItServer.getUrl(), PinResource.class,
                Collections.singletonList(exceptionMapper));
        WebClient.client(pinResource).accept("application/xml");

        hccResource = JAXRSClientFactory.create(HccItServer.getUrl(), HccResource.class,
                Collections.singletonList(exceptionMapper));
        WebClient.client(hccResource).accept("application/xml");
    }

    @Override
    public void before() throws Throwable {
        reset();
    }

    public void reset() throws Exception {
        HccDevice deviceInfo = DefaultTestDomainHelper.buildDefaultDevice();
        hccResource.updateDevice(deviceInfo);

        for (int i = 0; i < deviceInfo.getNumberOfPin() - 1; i++) {
            HccPinInfo info = DefaultTestDomainHelper.buildDefaultPin(i).getInfo();
            if (info != null) {
                pinResource.setPinInfo(i, info);
            }
        }
    }

    public PinResource getPinResource() {
        return pinResource;
    }

    public HccResource getHccResource() {
        return hccResource;
    }

}
