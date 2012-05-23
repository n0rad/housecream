package net.awired.housecream.client.poc;


public class Main {

    public static void main(String[] args) throws Throwable {
        //        HCCResponseExceptionMapper exceptionMapper = new HCCResponseExceptionMapper();
        //
        //        //        HccPinResource pinResource;
        //        HccBoardResource boardResource;
        //
        //        Client client = Client.create();
        //        System.out.println(getboard(client).getName());
        //        setPinValue(client, 1f);
        //        setPinValue(client, 0f);
        //
        //        long currentTimeMillis = System.currentTimeMillis();
        //        setPinValue(client, 0f);
        //        long currentTimeMillis2 = System.currentTimeMillis();
        //
        //        System.out.println(currentTimeMillis2 - currentTimeMillis);

        //        boardResource = JAXRSClientFactory.create("http://localhost:8080", HccBoardResource.class,
        //                Collections.singletonList(exceptionMapper));
        //        WebClient.client(boardResource).accept("application/json");
        //        HccBoard board = boardResource.getBoard();

        new HccPocServer(8080, PocNotify.class).start();

        while (true) {
            Thread.sleep(1000);
        }
    }

    //    public static HccBoard getboard(Client client) {
    //        WebResource webResource = client.resource("http://192.168.42.245");
    //        ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
    //        //        int status = response.getStatus();
    //        HccBoard board = response.getEntity(HccBoard.class);
    //        return board;
    //    }
    //
    //    public static void setPinValue(Client client, int pin, Float value) {
    //        WebResource webResource = client.resource("http://192.168.42.245/pin/" + pin + "/value");
    //        ClientResponse response = webResource.type("application/json").accept("application/json")
    //                .put(ClientResponse.class, value.toString());
    //        //        int status = response.getStatus();
    //    }
}
