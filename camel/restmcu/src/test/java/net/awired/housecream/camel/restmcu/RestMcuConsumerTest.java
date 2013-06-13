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

import static net.awired.restmcu.api.domain.line.RestMcuLineDirection.INPUT;
import static net.awired.restmcu.api.domain.line.RestMcuLineNotifyCondition.SUP_OR_EQUAL;
import static net.awired.restmcu.it.builder.LineInfoBuilder.line;
import static net.awired.restmcu.it.builder.NotifBuilder.notif;
import net.awired.jaxrs.junit.RestServerRule;
import net.awired.restmcu.api.domain.board.RestMcuBoardNotification;
import net.awired.restmcu.api.domain.board.RestMcuBoardNotificationType;
import net.awired.restmcu.api.domain.line.RestMcuLineNotification;
import net.awired.restmcu.it.resource.LatchBoardResource;
import net.awired.restmcu.it.resource.LatchLineResource;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Rule;
import org.junit.Test;

public class RestMcuConsumerTest extends CamelTestSupport {

    @EndpointInject(uri = "mock:result")
    protected MockEndpoint result;

    private LatchLineResource line = new LatchLineResource().addLine(line(2).value(42f).direction(INPUT).build());
    private LatchBoardResource board = new LatchBoardResource("127.0.0.1:5879");

    @Rule
    public RestServerRule boardRule = new RestServerRule("http://0.0.0.0:5879", line, board);

    @Test
    public void should_update_notification_url_and_receive_notif() throws Exception {
        result.expectedMinimumMessageCount(1);
        RestMcuLineNotification build = notif(line.lineInfo(2)).oldVal(41f).val(42f).notify(SUP_OR_EQUAL, 1).build();
        result.expectedBodiesReceived(build);

        board.sendNotif(build);

        assertMockEndpointsSatisfied();
    }

    @Test
    public void should_receive_board_notification() throws Exception {
        result.expectedMinimumMessageCount(1);
        RestMcuBoardNotification notif = new RestMcuBoardNotification(RestMcuBoardNotificationType.BOOT);
        result.expectedBodiesReceived(notif);

        board.sendNotif(notif);

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("restmcu://localhost:5879/2").to("mock:result");
            }
        };
    }

}
