package net.awired.housecream.server.it;

public class HcsItServer {

    private static final String URL_DEFAULT = "http://localhost:8080/hcs";
    private static final String URL_PROPERTY_NAME = "hcs.url";

    public static String getUrl() {
        return System.getProperty(URL_PROPERTY_NAME, URL_DEFAULT);
    }
}
