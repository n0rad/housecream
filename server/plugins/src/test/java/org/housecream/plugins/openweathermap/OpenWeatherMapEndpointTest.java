package org.housecream.plugins.openweathermap;

import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class OpenWeatherMapEndpointTest extends CamelTestSupport {

    @EndpointInject(uri = "mock:result")
    protected MockEndpoint result;

    @EndpointInject(uri = "mock:result2")
    protected MockEndpoint result2;

    @Test
    public void should_send_new_line_value() throws Exception {
        result.expectedMessageCount(1);
        result2.expectedMessageCount(1);


//        getMockEndpoint("mock:result").expectedBodiesReceived(42);
//        getMockEndpoint("mock:result2").expectedBodyReceived().predicate().body(Integer.class).;
        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("openweathermap:temperature/current?cityName=Paris").to("mock:result");
                from("openweathermap:humidity/current?cityName=Paris").to("mock:result2");
            }
        };
    }
}
