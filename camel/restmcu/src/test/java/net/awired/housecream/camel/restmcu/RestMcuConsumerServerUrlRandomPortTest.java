/**
 *
 *     Housecream.org project
 *     Copyright (C) Awired.net
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package net.awired.housecream.camel.restmcu;

import static net.awired.restmcu.it.builder.LineInfoBuilder.line;
import net.awired.jaxrs.junit.RestServerRule;
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
public class RestMcuConsumerServerUrlRandomPortTest extends CamelTestSupport {

    private LatchLineResource line = new LatchLineResource() //
            .addLine(line(2).value(42f).direction(RestMcuLineDirection.INPUT).build());
    private LatchBoardResource board = new LatchBoardResource("127.0.0.1:5879");

    @Rule
    public RestServerRule boardRule = new RestServerRule("http://0.0.0.0:5879", line, board);

    @Test
    public void should_use_notify_url_put_in_parameter() throws Exception {

        String notifyUrl = board.awaitUpdateSettings().getNotifyUrl();

        Assertions.assertThat(notifyUrl).startsWith("http://127.0.1.2:");
        Assertions.assertThat(notifyUrl.endsWith(":0")).isFalse();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("restmcu://localhost:5879/2?notifyUrl=127.0.1.2:0").to("mock:result");
            }
        };
    }
}
