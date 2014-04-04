/**
 *
 *     Copyright (C) Housecream.org
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
package org.housecream.plugins.restmcu;

import static org.housecream.restmcu.it.builder.LineInfoBuilder.line;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.fest.assertions.api.Assertions;
import org.housecream.restmcu.api.domain.line.RestMcuLineDirection;
import org.housecream.restmcu.it.resource.LatchBoardResource;
import org.housecream.restmcu.it.resource.LatchLineResource;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import fr.norad.jaxrs.junit.RestServerRule;

@Ignore("its working but starting with a notify url specified on an ip is kept between test even if server is destroyed")
public class RestMcuConsumerServerUrlTest extends CamelTestSupport {

    private LatchLineResource line = new LatchLineResource() //
            .addLine(line(2).value(42f).direction(RestMcuLineDirection.INPUT).build());
    private LatchBoardResource board = new LatchBoardResource("127.0.0.1:5879");

    @Rule
    public RestServerRule boardRule = new RestServerRule("http://0.0.0.0:5879", line, board);

    @Test
    public void should_use_notify_url_put_in_parameter() throws Exception {
        String notifyUrl = board.awaitUpdateSettings().getNotifyUrl();
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
