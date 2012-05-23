package net.awired.housecream.client.it;

import static org.junit.Assert.assertNotNull;
import javax.ws.rs.Path;
import net.awired.housecream.client.HccTestRule;
import net.awired.housecream.client.NotifyServer;
import net.awired.housecream.client.common.domain.board.HccBoard;
import net.awired.housecream.client.common.domain.board.HccBoardNotification;
import net.awired.housecream.client.common.domain.pin.HccPinNotification;
import net.awired.housecream.client.common.resource.server.HccNotifyResource;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

public class NotifyResourceIT {

    @Rule
    public HccTestRule hcc = new HccTestRule();

    public static HccPinNotification notification;

    @Path("/")
    public static class testNotify implements HccNotifyResource {
        @Override
        public void pinNotification(HccPinNotification pinNotification) {
            //            HccPinNotification pinNotification
            System.out.println("salutttt!");
            notification = pinNotification;
            throw new RuntimeException();
        }

        @Override
        public void boardNotification(HccBoardNotification boardNotification) {
            throw new RuntimeException();
        }

    }

    @ClassRule
    public static NotifyServer server = new NotifyServer(5879, testNotify.class);

    @Test
    public void should_test() throws Exception {
        HccBoard deviceInfo = hcc.getBoardResource().getBoard();
        deviceInfo.setNotifyUrl("http://localhost:5879");
        hcc.getBoardResource().setBoard(deviceInfo);

        // change value
        hcc.getDebugResource().setDebugValue(5, 1f);

        // change value
        hcc.getDebugResource().setDebugValue(5, 0f);

        assertNotNull(notification);
    }

    @Test
    @Ignore
    public void should_reset_data() throws Exception {
        // set notify url
        HccBoard deviceInfo = hcc.getBoardResource().getBoard();
        deviceInfo.setNotifyUrl("http://localhost:5879");
        hcc.getBoardResource().setBoard(deviceInfo);

        // change value
        hcc.getDebugResource().setDebugValue(5, 1f);

        // change value
        hcc.getDebugResource().setDebugValue(5, 0f);

        assertNotNull(notification);
    }
}
