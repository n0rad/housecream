package net.awired.housecream.server.it;

public class HcsItServer {

    private static final String DEFAULT_PORT = "8080";
    private static final String HCS_IT_PORT = "hcs.it.port";

    public static String getUrl() {
        String port = System.getProperty(HCS_IT_PORT, DEFAULT_PORT);
        return "http://localhost:" + port + "/hcs";
    }
}
