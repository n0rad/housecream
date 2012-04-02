package net.awired.housecream.client.it;

import static org.junit.Assert.assertNotNull;
import javax.ws.rs.Path;
import net.awired.housecream.client.HccTestRule;
import net.awired.housecream.client.NotifyServer;
import net.awired.housecream.client.common.domain.HccDevice;
import net.awired.housecream.client.common.domain.HccPinNotify;
import net.awired.housecream.client.common.resource.server.HccNotifyResource;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

public class NotifyResourceIT {

    @Rule
    public HccTestRule hcc = new HccTestRule();

    public static HccPinNotify notified;

    @Path("/")
    public static class testNotify implements HccNotifyResource {
        @Override
        public void notify(HccPinNotify pinNotify) {
            notified = pinNotify;
        }

        @Override
        public String start() {
            return "salut!!!";
        }
    }

    @ClassRule
    public static NotifyServer server = new NotifyServer(5879, testNotify.class);

    @Test
    public void should_reset_data() throws Exception {
        // set notify url
        HccDevice deviceInfo = hcc.getHccResource().getDeviceInfo();
        deviceInfo.setNotifyUrl("http://localhost:5879");
        hcc.getHccResource().updateDevice(deviceInfo);

        // change value
        hcc.getDebugResource().setDebugValue(5, 1f);

        // change value
        hcc.getDebugResource().setDebugValue(5, 0f);

        assertNotNull(notified);
    }
}
