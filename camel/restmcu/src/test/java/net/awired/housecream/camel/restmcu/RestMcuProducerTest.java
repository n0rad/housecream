package net.awired.housecream.camel.restmcu;

import net.awired.ajsl.test.RestServerRule;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.fest.assertions.Assertions;
import org.junit.Rule;
import org.junit.Test;

public class RestMcuProducerTest extends CamelTestSupport {

    @EndpointInject(uri = "mock:result")
    protected MockEndpoint result;

    @Rule
    public RestServerRule boardRule = new RestServerRule("http://0.0.0.0:5879", new LatchLineResource() {
        {
            LineInfo value3 = new LineInfo();
            value3.value = 43f;
            lines.put(3, value3);
        }
    }, new LatchBoardResource());

    @Test
    public void should_send_new_line_value() throws Exception {
        result.expectedMinimumMessageCount(1);
        ProducerTemplate template = context.createProducerTemplate();
        template.sendBody("direct:start", 42f);

        LatchLineResource resource = boardRule.getResource(LatchLineResource.class);

        Assertions.assertThat(resource.awaitLineValue(3)).isEqualTo(42);
        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("direct:start").to("restmcu://localhost:5879/3").to("mock:result");
            }
        };
    }
}
