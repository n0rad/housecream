package net.awired.housecream.client.it;

public class HccItServer {

    private static final String URL_DEFAULT = "http://localhost:8080/hcc";
    private static final String URL_PROPERTY_NAME = "hcc.url";

    public static String getUrl() {
        return System.getProperty(URL_PROPERTY_NAME, URL_DEFAULT);
    }
}
