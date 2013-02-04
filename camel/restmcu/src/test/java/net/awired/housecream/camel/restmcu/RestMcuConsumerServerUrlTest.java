package net.awired.housecream.camel.restmcu;

import net.awired.ajsl.test.RestServerRule;
import net.awired.restmcu.api.domain.line.RestMcuLine;
import net.awired.restmcu.api.domain.line.RestMcuLineDirection;
import net.awired.restmcu.it.resource.LatchBoardResource;
import net.awired.restmcu.it.resource.LatchLineResource;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.fest.assertions.api.Assertions;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

@Ignore("its working but starting with a notify url specified on an ip is kept between test even if server is destroyed")
public class RestMcuConsumerServerUrlTest extends CamelTestSupport {

    @Rule
    public RestServerRule boardRule = new RestServerRule("http://0.0.0.0:5879", new LatchLineResource() {
        {
            LineInfo lineInfo = new LineInfo();
            lineInfo.value = 42f;
            lineInfo.description = new RestMcuLine();
            lineInfo.description.setDirection(RestMcuLineDirection.INPUT);
            lines.put(2, lineInfo);
        }
    }, new LatchBoardResource());

    @Test
    public void should_use_notify_url_put_in_parameter() throws Exception {
        String notifyUrl = boardRule.getResource(LatchBoardResource.class).awaitUpdateSettings().getNotifyUrl();
        Assertions.assertThat(notifyUrl).isEqualTo("http://127.0.0.2:8787");
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("restmcu://localhost:5879/2?notifyUrl=127.0.0.2:8787").to("mock:result");
            }
        };
    }
}
