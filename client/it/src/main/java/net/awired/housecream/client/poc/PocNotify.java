package net.awired.housecream.client.poc;

import javax.ws.rs.Path;
import net.awired.housecream.client.common.domain.board.HccBoardNotification;
import net.awired.housecream.client.common.domain.pin.HccPinNotification;
import net.awired.housecream.client.common.resource.client.HccPinResource;
import net.awired.housecream.client.common.resource.server.HccNotifyResource;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Path("/notify")
public class PocNotify implements HccNotifyResource {

    private HccPinResource pinResource;

    WebResource webResource;

    public PocNotify() {
        Client client = Client.create();
        webResource = client.resource("http://192.168.1.122:8181/");
        //        //        pinResource = JAXRSClientFactory.create("http://192.168.42.245/", HccPinResource.class);
        //        WebClient client = WebClient.create("http://192.168.42.245/");
        //        WebClient.getConfig(client).getHttpConduit().getClient().setAllowChunking(false);
        //        //        client.
        //        //        Client client = ClientProxy.getClient(port);
        //        //        HTTPConduit http = (HTTPConduit) client.getConduit();
        //        //        HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
        //        //        httpClientPolicy.setConnectionTimeout(36000);
        //        //        httpClientPolicy.setAllowChunking(false);
        //        //        http.setClient(httpClientPolicy);
        //        pinResource = JAXRSClientFactory.fromClient(client, HccPinResource.class);
        //        //            WebClient.client(pinResource).accept("application/json");
    }

    public class threadtest extends Thread {
        private final HccPinNotification pinNotification;

        public threadtest(HccPinNotification pinNotification) {
            this.pinNotification = pinNotification;
        }

        @Override
        public void run() {
            ClientResponse response = webResource.path("pin/" + (pinNotification.getId() + 1) + "/value").put(
                    ClientResponse.class, pinNotification.getValue() == 1 ? "0" : "1");
            System.out.println(response);
        }
    }

    @Override
    public void pinNotification(HccPinNotification pinNotification) {
        System.out.println("pin notif id :" + pinNotification.getId());
        //        try {
        //                if (true || true) {
        //                    return;
        //                }
        //            pinResource.setValue(pinNotification.getId() + 1, pinNotification.getValue() == 1 ? 0f : 1f);

        new Thread(new threadtest(pinNotification)).start();
        //                return "sal";
        //        } catch (PinNotFoundException e) {
        //            // TODO Auto-generated catch block
        //            e.printStackTrace();
        //        } catch (HccUpdateException e) {
        //            // TODO Auto-generated catch block
        //            e.printStackTrace();
        //        }
    }

    @Override
    public void boardNotification(HccBoardNotification boardNotification) {
        System.out.println("board notif");
    }

}
