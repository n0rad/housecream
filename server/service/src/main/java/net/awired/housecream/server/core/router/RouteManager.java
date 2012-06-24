package net.awired.housecream.server.core.router;

import javax.inject.Inject;
import net.awired.housecream.server.common.domain.Point;
import net.awired.housecream.server.common.domain.inpoint.InPoint;
import net.awired.housecream.server.core.HouseCreamContext;
import net.awired.housecream.server.core.engine.EngineProcessor;
import net.awired.housecream.server.core.engine.StateHolder;
import net.awired.housecream.server.core.router.component.EndPointComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.springframework.stereotype.Component;

@Component
public class RouteManager extends RouteBuilder {

    private static final String EVENT_DIRECT_HOLDER = "direct:eventHolder";

    private CamelContext camelContext;

    @Inject
    private EngineProcessor engineProcessor;

    @Inject
    private HouseCreamContext houseCreamContext;

    @Inject
    private StateHolder stateHolder;

    public void registerPoint(Point point) {
        EndPointComponent component = ComponentType.findComponentForPoint(point);
        String routerUrl = houseCreamContext.getConnectorContextPath() + "/router";
        component.updatePointNotification(point, routerUrl);
        Float currentValue = component.getCurrentValue(point, camelContext);
        stateHolder.setState(point, currentValue);
    }

    @Override
    public void configure() throws Exception {
        from("cxfrs://bean://restMcuNotifyEndPoint").inOnly("seda:eventHolder");

        from("seda:eventHolder").process(engineProcessor).dynamicRouter().method(HcDynamicRouteFinder.class, "slip");

        from("xmpp://talk.google.com:5222/?serviceName=gmail.com&user=housecream.test@gmail.com&password=AZERTYUIOP")
                .to("seda:eventHolder");

    }

    public void configure2() throws Exception {
        //        .transform(body(HcEvent.class))
        from(EVENT_DIRECT_HOLDER).from("seda:eventHolder")
                .to("log:org.apache.camel.example?level=INFO&showProperties=true&showHeaders=true").dynamicRouter()
                .method(HcDynamicRouteFinder.class, "slip");

        //        from("timer://name?period=10s").to(EVENT_DIRECT_HOLDER);

        from("cxfrs://bean://restMcuNotifyEndPoint").inOnly("seda:eventHolder");

        String out = "cxfrs://http://localhost:5879/?resourceClasses=net.awired.housecream.server.core.service.web.Toto42&loggingFeatureEnabled=true";

        from("direct:toto42").process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.setPattern(ExchangePattern.OutOnly);
                Message inMessage = exchange.getIn();
                // set the operation name 
                inMessage.setHeader(CxfConstants.OPERATION_NAME, "setPinValue");
                // using the proxy client API
                inMessage.setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, Boolean.FALSE);
                // set a customer header
                inMessage.setHeader("key", "value");
                // set the parameters , if you just have one parameter 
                // camel will put this object into an Object[] itself
                inMessage.setBody(new Object[] { 3, 1f });
            }
        }).to(out);
        //        "cxfrs://bean://rsClient"
        //        from(CXF_RS_ENDPOINT_URI).to(EVENT_DIRECT_HOLDER);

        //        from(CXF_RS_ENDPOINT_URI).process(new Processor() {
        //            @Override
        //            public void process(Exchange exchange) throws Exception {
        //                //                Message in = exchange.getIn();
        //                //                String operationName = in.getHeader(CxfConstants.OPERATION_NAME, String.class);
        //                //                if (!"sendEvent".equals(operationName)) {
        //                exchange.getOut().setBody("yes");
        //                //                }
        //            }
        //        });

    }

    private float getPointValue(InPoint inPoint) {
        ProducerTemplate createProducerTemplate = camelContext.createProducerTemplate();
        createProducerTemplate.request(inPoint.getUrl(), new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                System.out.println(exchange.getIn().getBody());
            }
        });
        return 0;
    }
}
