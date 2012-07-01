package net.awired.housecream.server.it;

public class HcsItServer {

    private static final String HCS_HOST_DEFAULT = "localhost";
    private static final String HCS_HOST_PROPERTY_NAME = "hcs.host";

    private static final String HCS_PORT_DEFAULT = "8080";
    private static final String HCS_PORT_PROPERTY_NAME = "hcs.port";

    private static final String HCS_PATH_DEFAULT = "/hcs/ws";
    private static final String HCS_PATH_PROPERTY_NAME = "hcs.path";

    public static String getUrl() {
        return "http://" + System.getProperty(HCS_HOST_PROPERTY_NAME, HCS_HOST_DEFAULT) //
                + ":" + System.getProperty(HCS_PORT_PROPERTY_NAME, HCS_PORT_DEFAULT) //
                + System.getProperty(HCS_PATH_PROPERTY_NAME, HCS_PATH_DEFAULT);
    }
}
