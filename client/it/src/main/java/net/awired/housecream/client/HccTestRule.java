package net.awired.housecream.client;

import net.awired.ajsl.web.resource.mapper.AjslResponseExceptionMapper;
import net.awired.housecream.client.common.resource.client.HccBoardResource;
import net.awired.housecream.client.common.resource.client.HccPinResource;
import net.awired.housecream.client.common.test.DefaultTestDebugResource;
import net.awired.housecream.client.it.HccItServer;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.provider.JSONProvider;
import org.junit.rules.ExternalResource;
import com.google.common.collect.ImmutableList;

public class HccTestRule extends ExternalResource {

    private HccBoardResource boardResource;

    private HccPinResource pinResource;

    private DefaultTestDebugResource debugResource;

    public HccTestRule() {
        JSONProvider jsonProvider = new JSONProvider();
        jsonProvider.setSupportUnwrapped(true);
        jsonProvider.setDropRootElement(true);

        AjslResponseExceptionMapper exceptionMapper = new AjslResponseExceptionMapper(jsonProvider);
        ImmutableList<Object> providers = ImmutableList.of(exceptionMapper, jsonProvider);

        pinResource = JAXRSClientFactory.create(HccItServer.getUrl(), HccPinResource.class, providers);
        boardResource = JAXRSClientFactory.create(HccItServer.getUrl(), HccBoardResource.class, providers);
        debugResource = JAXRSClientFactory.create(HccItServer.getUrl(), DefaultTestDebugResource.class, providers);
    }

    @Override
    public void before() throws Throwable {
        reset();
    }

    public void reset() throws Exception {
        //        HccBoard deviceInfo = DefaultITDomainHelper.buildDefaultDevice();
        //        boardResource.setBoard(deviceInfo);

        //        for (int i = 0; i < deviceInfo.getNumberOfPin() - 1; i++) {
        //            HccPinInfo info = DefaultITDomainHelper.buildDefaultPin(i).getInfo();
        //            if (info != null) {
        //                pinResource.setPinInfo(i, info);
        //            }
        //        }
        //
        //        for (int i = 0; i < deviceInfo.getNumberOfPin() - 1; i++) {
        //            HccPin pin = DefaultITDomainHelper.buildDefaultPin(i);
        //            if (pin.getValue() != null) {
        //                debugResource.setDebugValue(i, pin.getValue());
        //            }
        //        }
    }

    public HccPinResource getPinResource() {
        return pinResource;
    }

    public HccBoardResource getBoardResource() {
        return boardResource;
    }

    public DefaultTestDebugResource getDebugResource() {
        return debugResource;
    }

}
