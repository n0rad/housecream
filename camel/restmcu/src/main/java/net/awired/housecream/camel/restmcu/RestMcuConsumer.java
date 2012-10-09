package net.awired.housecream.camel.restmcu;

import java.util.Arrays;
import net.awired.ajsl.web.rest.RestContext;
import net.awired.restmcu.api.domain.board.RestMcuBoardNotification;
import net.awired.restmcu.api.domain.line.RestMcuLineNotification;
import net.awired.restmcu.api.resource.server.RestMcuNotifyResource;
import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.cxf.endpoint.Server;

public class RestMcuConsumer extends DefaultConsumer {

    private Server server;

    private static class Toto implements RestMcuNotifyResource {

        @Override
        public void lineNotification(RestMcuLineNotification lineNotification) {
            // TODO Auto-generated method stub

        }

        @Override
        public void boardNotification(RestMcuBoardNotification boardNotification) {
            // TODO Auto-generated method stub

        }

        @Override
        public long getPosixTime() {
            // TODO Auto-generated method stub
            return 0;
        }

    }

    public RestMcuConsumer(Endpoint endpoint, Processor processor) {
        super(endpoint, processor);
        Toto toto = new Toto();
        server = new RestContext().prepareServer("http://localhost:8765", Arrays.asList(toto));
        //        server = svrBean.create();
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
