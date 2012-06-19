package net.awired.housecream.server.core.router;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.springframework.stereotype.Component;

@Component
public class HcRouter extends RouteBuilder {

    private static final String EVENT_DIRECT_HOLDER = "direct:eventHolder";

    @Override
    public void configure() throws Exception {
        from(EVENT_DIRECT_HOLDER).from("seda:eventHolder")
                .to("log:org.apache.camel.example?level=INFO&showProperties=true&showHeaders=true").dynamicRouter()
                .method(HcDynamicRouteFinder.class, "slip");

        //        from("timer://name?period=10s").to(DIRECT_EVENT_HOLDER);

        from("cxfrs://bean://restMcuNotifyEndPoint").inOnly("seda:eventHolder");

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
        }).to("cxfrs://bean://rsClient");

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
}
