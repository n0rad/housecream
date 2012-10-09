package net.awired.housecream.camel.restmcu;

import net.awired.ajsl.test.RestServerRule;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.fest.assertions.Assertions;
import org.junit.Rule;
import org.junit.Test;

public class RestMcuComponentTest extends CamelTestSupport {

    @Rule
    public RestServerRule notifyRule = new RestServerRule("http://0.0.0.0:5879", new LatchLineResource() {
        {
            LineInfo value = new LineInfo();
            value.value = 42f;
            lines.put(2, value);

            LineInfo value3 = new LineInfo();
            value3.value = 43f;
            lines.put(3, value3);
        }
    });

    @Test
    public void should_read_line_2_and_set_line_3_to_line_2_value() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMinimumMessageCount(1);

        LatchLineResource resource = notifyRule.getResource(LatchLineResource.class);

        Assertions.assertThat(resource.awaitLineValue(3)).isEqualTo(42);
        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("restmcu://localhost:5879/2").to("restmcu://localhost:5879/3").to("mock:result");
            }
        };
    }
}
