package net.awired.housecream.server.service.event;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private Server server = null;

    @PostConstruct
    public void init() {
        try {
            server = new Server(8888);

            HcwWebSocketHandler hcwWebSocketHandler = new HcwWebSocketHandler();
            hcwWebSocketHandler.setHandler(new DefaultHandler());
            server.setHandler(hcwWebSocketHandler);

            server.start();

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void destroy() {
        if (server != null) {
            try {
                server.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
