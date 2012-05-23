package net.awired.housecream.server.core.service;

import org.apache.camel.builder.RouteBuilder;
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
        from("cxfrs://bean://hccNotifyEndPoint").inOnly("seda:eventHolder");

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
