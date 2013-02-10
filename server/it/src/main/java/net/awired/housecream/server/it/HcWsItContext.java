package net.awired.housecream.server.it;

import net.awired.ajsl.core.io.NetworkUtils;

public class HcWsItContext {

    private static final String HCWS_HOST_DEFAULT = "localhost";
    private static final String HCWS_HOST_PROPERTY_NAME = "hcws.host";

    private static final String HCWS_PORT_DEFAULT = "8080";
    private static final String HCWS_PORT_PROPERTY_NAME = "hcws.port";

    private static final String HCWS_PATH_DEFAULT = "/housecream/ws";
    private static final String HCWS_PATH_PROPERTY_NAME = "hcws.path";

    private static String hcwsHost;

    static {
        hcwsHost = NetworkUtils.getFirstNonWifiIp();
        if (hcwsHost == null) {
            hcwsHost = NetworkUtils.getFirstNonLocalhostIp();
        }
        if (hcwsHost == null) {
            hcwsHost = HCWS_HOST_DEFAULT;
        }
    }

    public static String getUrl() {
        return "http://" + hcwsHost + ":" + System.getProperty(HCWS_PORT_PROPERTY_NAME, HCWS_PORT_DEFAULT) //
                + System.getProperty(HCWS_PATH_PROPERTY_NAME, HCWS_PATH_DEFAULT);
    }

}
