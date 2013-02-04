package net.awired.housecream.camel.solar;

import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class SolarConsumerTest extends CamelTestSupport {

    @EndpointInject(uri = "mock:result")
    protected MockEndpoint result;

    @Test
    public void should_() throws Exception {
        result.expectedMinimumMessageCount(1);
        String salut = "salut";
        result.expectedBodiesReceived(salut);
        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("solar:America/New_York?latitude=39.9522222&longitude=-75.1641667").to("mock:result");
            }
        };
    }

}
