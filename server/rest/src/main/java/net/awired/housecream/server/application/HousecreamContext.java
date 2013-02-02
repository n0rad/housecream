package net.awired.housecream.server.application;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import net.awired.ajsl.core.io.NetworkUtils;
import org.springframework.stereotype.Component;

@Component
@Deprecated
public class HousecreamContext {

    @Inject
    private ServletContext context;

    private Integer port = 8080;
    private String ip;

    public void provideSampleConnection(String ip, int port) {
        if (this.ip == null && !ip.startsWith("127.")) {
            this.ip = ip; //TODO do not store ip localhost (use network)
        }
        if (this.port == null) {
            this.port = port;
        }
    }

    public String getConnectorContextPath() {
        if (ip == null) {
            ip = NetworkUtils.getFirstNonWifiIp();
            if (ip == null) {
                ip = NetworkUtils.getFirstNonLocalhostIp();
            }
        }
        //TODO store in conf the path and update based on connection : domain name in http request

        return "http://" + ip + ":" + port + context.getContextPath(); //TODO PORT may be null
    }

}
