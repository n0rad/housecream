package net.awired.housecream.client.it;

public class HccItServer {

    private static final String DEFAULT_PORT = "8080";
    private static final String HCC_IT_PORT = "hcc.it.port";

    public static String getUrl() {
        String port = System.getProperty(HCC_IT_PORT, DEFAULT_PORT);
        return "http://localhost:" + port + "/hcc/";
    }

}
