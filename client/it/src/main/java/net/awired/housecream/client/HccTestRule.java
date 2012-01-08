package net.awired.housecream.client;

import java.util.Collections;
import net.awired.housecream.client.common.domain.HccDevice;
import net.awired.housecream.client.common.resource.HCCResponseExceptionMapper;
import net.awired.housecream.client.common.resource.HccResource;
import net.awired.housecream.client.common.resource.PinResource;
import net.awired.housecream.client.common.test.DefaultTestDomainHelper;
import net.awired.housecream.client.it.HccItServer;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.junit.rules.ExternalResource;

public class HccTestRule extends ExternalResource {

    private HccResource hccResource;

    private PinResource pinResource;

    @Override
    public void before() throws Throwable {
        HCCResponseExceptionMapper exceptionMapper = new HCCResponseExceptionMapper();
        pinResource = JAXRSClientFactory.create(HccItServer.getUrl(), PinResource.class,
                Collections.singletonList(exceptionMapper));

        hccResource = JAXRSClientFactory.create(HccItServer.getUrl(), HccResource.class,
                Collections.singletonList(exceptionMapper));

        reset();
    }

    public void reset() throws Exception {
        HccDevice deviceInfo = DefaultTestDomainHelper.buildDefaultDevice();
        hccResource.updateDevice(deviceInfo);
    }

    public PinResource getPinResource() {
        return pinResource;
    }

    public HccResource getHccResource() {
        return hccResource;
    }

}
