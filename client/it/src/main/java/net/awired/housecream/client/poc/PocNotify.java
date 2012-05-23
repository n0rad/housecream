package net.awired.housecream.client.poc;

import javax.ws.rs.Path;
import net.awired.housecream.client.common.domain.board.HccBoardNotification;
import net.awired.housecream.client.common.domain.pin.HccPinNotification;
import net.awired.housecream.client.common.resource.client.HccPinResource;
import net.awired.housecream.client.common.resource.server.HccNotifyResource;

@Path("/notify")
public class PocNotify implements HccNotifyResource {

    private HccPinResource pinResource;

    public PocNotify() {
        System.out.println("new");
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

    //    public class threadtest extends Thread {
    //        private final HccPinNotification pinNotification;
    //        private Client client;
    //
    //        public threadtest(HccPinNotification pinNotification) {
    //            this.pinNotification = pinNotification;
    //            client = Client.create();
    //        }
    //
    //        @Override
    //        public void run() {
    //        }
    //    }

    @Override
    public void pinNotification(HccPinNotification pinNotification) {
        //        Main.setPinValue(Main.client, pinNotification.getId() + 1, pinNotification.getValue() == 1 ? 0f : 1f);

        //        long currentTimeMillis = System.currentTimeMillis();
        System.out.println("pin notif id :" + pinNotification.getPinId() + "value :" + pinNotification.getValue());
        //        System.out.println("sending");
        //        WebResource webResource = client.resource("http://192.168.42.245/pin/" + (pinNotification.getId() + 1)
        //                + "/value");
        //        System.out.println(System.currentTimeMillis() - currentTimeMillis);
        //        ClientResponse response = webResource.type("application/json").accept("application/json")
        //                .put(ClientResponse.class, pinNotification.getValue() == 1 ? "0" : "1");
        //        System.out.println(response);
        //
        //        System.out.println(System.currentTimeMillis() - currentTimeMillis);

        //        try {
        //                if (true || true) {
        //                    return;
        //                }
        //            pinResource.setValue(pinNotification.getId() + 1, pinNotification.getValue() == 1 ? 0f : 1f);

        //        new Thread(new threadtest(pinNotification)).start();
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
