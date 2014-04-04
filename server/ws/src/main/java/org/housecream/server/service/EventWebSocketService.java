/**
 *
 *     Copyright (C) Housecream.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package org.housecream.server.service;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketHandler;
import org.housecream.server.api.domain.PointState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EventWebSocketService {

    @Autowired
    private ObjectMapper objectMapper;

    private Server server;
    private HcwWebSocketHandler handler;

    @PostConstruct
    public void init() {
        try {
            server = new Server(8888);
            handler = new HcwWebSocketHandler();
            handler.setHandler(new DefaultHandler());
            server.setHandler(handler);
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

    @Async
    public void notifyStateUpdate(PointState state) {
        handler.sendMessage(state);
    }

    public class HcwWebSocketHandler extends WebSocketHandler {

        final Set<HcwWebSocket> webSockets = new CopyOnWriteArraySet<HcwWebSocket>();

        @PostConstruct
        public void init() {
            setHandler(new DefaultHandler());
        }

        @Override
        public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
            return new HcwWebSocket(this);
        }

        public void sendMessage(PointState state) {
            String eventAsJson;
            try {
                eventAsJson = objectMapper.writeValueAsString(state);
            } catch (Exception e) {
                throw new RuntimeException("Cannot construct json version of state : " + state, e);
            }
            for (HcwWebSocket webSocket : webSockets) {
                try {
                    webSocket.connection.sendMessage(eventAsJson);
                } catch (IOException e) {
                    webSocket.connection.close();
                }
            }
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
                log.error("Receiving message from WebSocket : " + data);
            }

            @Override
            public void onClose(int closeCode, String message) {
                log.info("Closing connection {} with code {} and message {} ", new Object[] { connection, closeCode,
                        message });
                this.hcwWebSocketHandler.webSockets.remove(this);
            }
        }

    }

}
