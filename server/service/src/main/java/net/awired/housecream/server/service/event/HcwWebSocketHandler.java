package net.awired.housecream.server.service.event;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HcwWebSocketHandler extends WebSocketHandler {

    final Set<HcwWebSocket> webSockets = new CopyOnWriteArraySet<HcwWebSocket>();

    @Override
    public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
        return new HcwWebSocket(this);
    }

    class HcwWebSocket implements WebSocket.OnTextMessage {

        private final Logger log = LoggerFactory.getLogger(getClass());

        private HcwWebSocketHandler hcwWebSocketHandler;
        private Connection connection;

        HcwWebSocket(HcwWebSocketHandler hcwWebSocketHandler) {
            this.hcwWebSocketHandler = hcwWebSocketHandler;
        }

        @Override
        public void onOpen(Connection connection) {
            log.info("Connection opened by client : " + connection);
            this.connection = connection;
            hcwWebSocketHandler.webSockets.add(this);
        }

        @Override
        public void onMessage(String data) {
            //            try {
            //                for (HcwWebSocket webSocket : hcwWebSocketHandler.webSockets) {
            //                    webSocket.connection.sendMessage(data);
            //                }
            //            } catch (IOException x) {
            //                connection.close();
            //            }
        }

        @Override
        public void onClose(int closeCode, String message) {
            log.info("Closing connection {} with code {} and message {} ", new Object[] { connection, closeCode,
                    message });
            this.hcwWebSocketHandler.webSockets.remove(this);
        }
    }

}
