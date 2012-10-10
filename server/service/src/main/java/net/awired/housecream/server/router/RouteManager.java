package net.awired.housecream.server.router;

import javax.inject.Inject;
import net.awired.housecream.server.HousecreamContext;
import net.awired.housecream.server.api.domain.Point;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.engine.ConsequenceAction;
import net.awired.housecream.server.engine.EngineProcessor;
import net.awired.housecream.server.engine.StateHolder;
import net.awired.housecream.server.service.PluginService;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class RouteManager extends RouteBuilder {

    private static final String EVENT_DIRECT_HOLDER = "direct:eventHolder";

    @Inject
    private CamelContext camelContext;

    @Inject
    private EngineProcessor engineProcessor;

    @Inject
    private HousecreamContext houseCreamContext;

    @Inject
    private StateHolder stateHolder;

    @Inject
    private EngineResultSplitter splitter;

    @Inject
    private OutDynamicRouter dynamicRouter;

    @Inject
    private PluginService pluginService;

    public void registerPoint(Point point) {
        //        HousecreamPlugin plugin = pluginService.getPluginFromPrefix(point);
        //        String routerUrl = houseCreamContext.getConnectorContextPath() + "/ws/router";
        //        plugin.updatePointNotification(point, routerUrl);
        //        Float currentValue = plugin.getCurrentValue(point, camelContext);
        //        stateHolder.setState(point.getId(), currentValue);
    }

    @Override
    public void configure() throws Exception {

        //        from("cxfrs://bean://restMcuNotifyEndPoint").inOnly("seda:eventHolder");

        //        from("seda:eventHolder").process(engineProcessor).recipientList(header("outputActions"))
        //                .onPrepare(new OuptutPreProcessor()).parallelProcessing();

        from("seda:eventHolder").process(engineProcessor).split().method(splitter, "split").parallelProcessing()
                .to("direct:output");

        from("direct:output").inOut().dynamicRouter().method(dynamicRouter, "route").process(new Processor() {
            @Override
            public void process(Exchange arg0) throws Exception {
                ConsequenceAction action = arg0.getUnitOfWork().getOriginalInMessage()
                        .getHeader("ACTION", ConsequenceAction.class);
                stateHolder.setState(action.getPointId(), action.getValue());
            }
        });

        //        ConsumerTemplate createConsumerTemplate = context.createConsumerTemplate();
        //        createConsumerTemplate.

        //        ProducerTemplate template = context.createProducerTemplate();
        //
        //        // Send to a specific queue
        //        template.sendBody("activemq:MyQueue", "<hello>world!</hello>");
        //
        //        // Send with a body and header 
        //        template.sendBodyAndHeader("activemq:MyQueue", "<hello>world!</hello>", "CustomerRating", "Gold");
        //        template.send("activemq:MyQueue", new MyProcessor());

        //        .to("log:org.apache.camel.example?level=INFO&showProperties=true&showHeaders=true")
        //                .to("xmpp://talk.google.com:5222/*?serviceName=gmail.com&user=housecream.test@gmail.com&password=AZERTYUIOP");

        //        from("xmpp://talk.google.com:5222/*?serviceName=gmail.com&user=housecream.test@gmail.com&password=AZERTYUIOP")
        //                .to("seda:eventHolder");

    }

    //            DefaultMessage message = new DefaultMessage();
    //            message.setBody(action);
    //            res.add(message);

    //        exchange.setPattern(ExchangePattern.OutOnly);
    //        Message inMessage = exchange.getIn();

    //    public void configure2() throws Exception {
    //        //        .transform(body(HcEvent.class))
    //        from(EVENT_DIRECT_HOLDER).from("seda:eventHolder")
    //                .to("log:org.apache.camel.example?level=INFO&showProperties=true&showHeaders=true").dynamicRouter()
    //                .method(HcDynamicRouteFinder.class, "slip");
    //
    //        //        from("timer://name?period=10s").to(EVENT_DIRECT_HOLDER);
    //
    //        from("cxfrs://bean://restMcuNotifyEndPoint").inOnly("seda:eventHolder");
    //
    //        String out = "cxfrs://http://localhost:5879/?resourceClasses=net.awired.housecream.server.core.service.web.Toto42&loggingFeatureEnabled=true";
    //
    //        from("direct:toto42").process(new Processor() {
    //            @Override
    //            public void process(Exchange exchange) throws Exception {
    //                exchange.setPattern(ExchangePattern.OutOnly);
    //                Message inMessage = exchange.getIn();
    //                // set the operation name 
    //                inMessage.setHeader(CxfConstants.OPERATION_NAME, "setPinValue");
    //                // using the proxy client API
    //                inMessage.setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, Boolean.FALSE);
    //                // set a customer header
    //                inMessage.setHeader("key", "value");
    //                // set the parameters , if you just have one parameter 
    //                // camel will put this object into an Object[] itself
    //                inMessage.setBody(new Object[] { 3, 1f });
    //            }
    //        }).to(out);
    //        //        "cxfrs://bean://rsClient"
    //        //        from(CXF_RS_ENDPOINT_URI).to(EVENT_DIRECT_HOLDER);
    //
    //        //        from(CXF_RS_ENDPOINT_URI).process(new Processor() {
    //        //            @Override
    //        //            public void process(Exchange exchange) throws Exception {
    //        //                //                Message in = exchange.getIn();
    //        //                //                String operationName = in.getHeader(CxfConstants.OPERATION_NAME, String.class);
    //        //                //                if (!"sendEvent".equals(operationName)) {
    //        //                exchange.getOut().setBody("yes");
    //        //                //                }
    //        //            }
    //        //        });
    //
    //    }

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
