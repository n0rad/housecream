package net.awired.housecream.server.it;

import net.awired.ajsl.core.io.NetworkUtils;

public class HcsItContext {

    private static final String HCS_HOST_DEFAULT = "localhost";
    private static final String HCS_HOST_PROPERTY_NAME = "hcs.host";

    private static final String HCS_PORT_DEFAULT = "8080";
    private static final String HCS_PORT_PROPERTY_NAME = "hcs.port";

    private static final String HCS_PATH_DEFAULT = "/housecream/ws";
    private static final String HCS_PATH_PROPERTY_NAME = "hcs.path";

    private static String hcsHost;

    static {
        hcsHost = NetworkUtils.getFirstNonWifiIp();
        if (hcsHost == null) {
            hcsHost = NetworkUtils.getFirstNonLocalhostIp();
        }
        if (hcsHost == null) {
            hcsHost = HCS_HOST_DEFAULT;
        }
    }

    public static String getUrl() {
        return "http://" + hcsHost + ":" + System.getProperty(HCS_PORT_PROPERTY_NAME, HCS_PORT_DEFAULT) //
                + System.getProperty(HCS_PATH_PROPERTY_NAME, HCS_PATH_DEFAULT);
    }

}
