package net.awired.housecream.camel.restmcu;

import java.util.Arrays;
import net.awired.ajsl.core.lang.exception.UpdateException;
import net.awired.ajsl.web.rest.RestContext;
import net.awired.restmcu.api.domain.board.RestMcuBoardSettings;
import net.awired.restmcu.api.resource.client.RestMcuBoardResource;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.cxf.endpoint.Server;

public class RestMcuConsumer extends DefaultConsumer {

    private Server server;

    public RestMcuConsumer(RestMcuEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        String notifyUrl = "http://localhost:8765";
        server = new RestContext().prepareServer(notifyUrl,
                Arrays.asList(new RestMcuCamelNotifyResource(endpoint, this)));
        updateNotificationUrl(endpoint, notifyUrl);
    }

    private void updateNotificationUrl(RestMcuEndpoint endpoint, String notifyUrl) {
        RestContext restContext = new RestContext();
        RestMcuBoardResource client = restContext.prepareClient(RestMcuBoardResource.class, endpoint.getRestMcuUrl(),
                null, true);
        RestMcuBoardSettings boardSettings = new RestMcuBoardSettings();
        boardSettings.setNotifyUrl(notifyUrl);
        try {
            client.setBoardSettings(boardSettings);
        } catch (UpdateException e) {
            throw new IllegalStateException("Cannot set notifyUrl on board", e);
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
        super.doStop();
    }

    public Server getServer() {
        return server;
    }

}
