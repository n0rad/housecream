package net.awired.housecream.camel.restmcu;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import net.awired.ajsl.core.io.NetworkUtils;
import net.awired.ajsl.web.rest.RestContext;
import net.awired.restmcu.api.domain.board.RestMcuBoardSettings;
import net.awired.restmcu.api.resource.client.RestMcuBoardResource;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.transport.http_jetty.JettyHTTPDestination;
import org.apache.cxf.transport.http_jetty.JettyHTTPServerEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestMcuConsumer extends DefaultConsumer {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private Server server;

    public RestMcuConsumer(RestMcuEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        URL listeningUrl = findListeningUrl();
        server = new RestContext().prepareServer(listeningUrl.toString(),
                Arrays.asList(new RestMcuCamelNotifyResource(endpoint, this)));
        String notifyUrl = findNotifyUrl(listeningUrl);
        log.debug("Started restmcu notify server {}:{}", listeningUrl.getHost(), findListeningPort());
        updateNotificationUrl(endpoint, notifyUrl);
    }

    private URL findListeningUrl() {
        URL notifyUrl = ((RestMcuEndpoint) getEndpoint()).getNotifyUrl();
        if (notifyUrl == null) {
            try {
                return new URL("http://0.0.0.0:0");
            } catch (MalformedURLException e) {
            }
        }
        return notifyUrl;
    }

    private String findNotifyUrl(URL listeningUrl) {
        String notifyUrl = "http://";
        if (listeningUrl.getHost().equals("0.0.0.0")) {
            notifyUrl += NetworkUtils.getFirstNonWifiIp();
        } else {
            notifyUrl += listeningUrl.getHost();
        }
        notifyUrl += ':';
        notifyUrl += findListeningPort();
        return notifyUrl;
    }

    private int findListeningPort() {
        JettyHTTPDestination jettyDestination = JettyHTTPDestination.class.cast(server.getDestination());
        JettyHTTPServerEngine serverEngine = JettyHTTPServerEngine.class.cast(jettyDestination.getEngine());
        org.eclipse.jetty.server.Server httpServer = serverEngine.getServer();
        return httpServer.getConnectors()[0].getLocalPort();
    }

    private void updateNotificationUrl(RestMcuEndpoint endpoint, String notifyUrl) {
        log.debug("Calling board : {} to update notifyUrl with : {}", endpoint.getRestmcuUrl(), notifyUrl);
        RestContext restContext = new RestContext();
        RestMcuBoardResource client = restContext.prepareClient(RestMcuBoardResource.class, endpoint.getRestmcuUrl(),
                null, true);
        RestMcuBoardSettings boardSettings = new RestMcuBoardSettings();
        boardSettings.setNotifyUrl(notifyUrl);
        try {
            client.setBoardSettings(boardSettings);
        } catch (Exception e) {
            log.warn("Cannot set notifyUrl for board : " + endpoint.getRestmcuUrl(), e);
        }
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        server.start();
    }

    @Override
    protected void doStop() throws Exception {
        server.stop();
        server.destroy();
        super.doStop();
    }

    public Server getServer() {
        return server;
    }

}
