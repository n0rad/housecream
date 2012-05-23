package net.awired.housecream.server.it;

import javax.ws.rs.core.MediaType;
import net.awired.ajsl.web.resource.mapper.AjslResponseExceptionMapper;
import net.awired.housecream.server.common.resource.InPointResource;
import net.awired.housecream.server.it.proxy.PointProxy;
import net.awired.housecream.server.it.proxy.ProxyClass;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.provider.JSONProvider;
import org.junit.rules.ExternalResource;
import com.google.common.collect.ImmutableList;

public class HcsTestRule extends ExternalResource {

    private InPointResource inPointResource;
    private final PointProxy<InPointResource> pointProxy;

    public HcsTestRule() {
        JSONProvider jsonProvider = new JSONProvider();
        jsonProvider.setSupportUnwrapped(true);
        jsonProvider.setDropRootElement(true);

        AjslResponseExceptionMapper exceptionMapper = new AjslResponseExceptionMapper(jsonProvider);
        ImmutableList<Object> providers = ImmutableList.of(exceptionMapper, jsonProvider);

        InPointResource webPointResource = JAXRSClientFactory.create(HcsItServer.getUrl(), InPointResource.class,
                providers);
        WebClient.client(webPointResource).accept(MediaType.APPLICATION_JSON_TYPE)
                .type(MediaType.APPLICATION_JSON_TYPE);

        pointProxy = new PointProxy(webPointResource);
        inPointResource = ProxyClass.BuildProxy(pointProxy, InPointResource.class);

        //                pointResource = (PointResource) Proxy.newProxyInstance(PointResource.class.getClassLoader(),
        //                        new Class[] { PointResource.class }, new ProxyClass(webPointResource));
    }

    @Override
    public void before() throws Throwable {
        System.out.println(pointProxy.getPoints());
    }

    @Override
    protected void after() {
        for (Long pointId : pointProxy.getPoints()) {
            inPointResource.deleteInPoint(pointId);
        }
    }

    public InPointResource getInPointResource() {
        return inPointResource;
    }

}
