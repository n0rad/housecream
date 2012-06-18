package net.awired.housecream.server.it;

import javax.ws.rs.core.MediaType;
import net.awired.ajsl.web.resource.mapper.AjslResponseExceptionMapper;
import net.awired.housecream.server.common.resource.HcRestMcuNotifyResource;
import net.awired.housecream.server.common.resource.InPointResource;
import net.awired.housecream.server.common.resource.OutPointResource;
import net.awired.restmcu.api.resource.server.RestMcuNotifyResource;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.provider.JSONProvider;
import org.junit.rules.ExternalResource;
import com.google.common.collect.ImmutableList;

public class HcsTestRule extends ExternalResource {

    private HcRestMcuNotifyResource notifyResource;
    private OutPointResource outPointResource;
    private InPointResource inPointResource;

    //    private final PointProxy<InPointResource> inPointProxy;
    //    private final PointProxy<OutPointResource> outpointProxy;
    //        System.out.println(inPointProxy.getPoints());
    //        inPointProxy = new PointProxy(webInPointResource);
    //        inPointResource = ProxyClass.BuildProxy(inPointProxy, InPointResource.class);
    //        for (Long pointId : inPointProxy.getPoints()) {
    //            inPointResource.deleteInPoint(pointId);
    //        }

    public HcsTestRule() {
        JSONProvider jsonProvider = new JSONProvider();
        jsonProvider.setSupportUnwrapped(true);
        jsonProvider.setDropRootElement(true);

        AjslResponseExceptionMapper exceptionMapper = new AjslResponseExceptionMapper(jsonProvider);
        ImmutableList<Object> providers = ImmutableList.of(exceptionMapper, jsonProvider);

        // inpoint
        inPointResource = JAXRSClientFactory.create(HcsItServer.getUrl(), InPointResource.class, providers);
        WebClient.client(inPointResource).accept(MediaType.APPLICATION_JSON_TYPE)
                .type(MediaType.APPLICATION_JSON_TYPE);

        // outpoint
        outPointResource = JAXRSClientFactory.create(HcsItServer.getUrl(), OutPointResource.class, providers);
        WebClient.client(outPointResource).accept(MediaType.APPLICATION_JSON_TYPE)
                .type(MediaType.APPLICATION_JSON_TYPE);

        // notify
        notifyResource = JAXRSClientFactory.create(HcsItServer.getUrl(), HcRestMcuNotifyResource.class, providers);
        WebClient.client(notifyResource).accept(MediaType.APPLICATION_JSON_TYPE)
                .type(MediaType.APPLICATION_JSON_TYPE);
    }

    @Override
    public void before() throws Throwable {
        inPointResource.deleteAllInPoints();
        outPointResource.deleteAllOutPoints();
    }

    @Override
    protected void after() {
    }

    public InPointResource getInPointResource() {
        return inPointResource;
    }

    public OutPointResource getOutPointResource() {
        return outPointResource;
    }

    public RestMcuNotifyResource getNotifyResource() {
        return notifyResource;
    }

}
