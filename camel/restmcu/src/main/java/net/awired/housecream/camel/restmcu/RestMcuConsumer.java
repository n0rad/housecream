/**
 *
 *     Housecream.org project
 *     Copyright (C) Awired.net
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
package net.awired.housecream.camel.restmcu;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import net.awired.ajsl.core.io.NetworkUtils;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.restmcu.api.domain.board.RestMcuBoardSettings;
import net.awired.restmcu.api.domain.line.RestMcuLine;
import net.awired.restmcu.api.domain.line.RestMcuLineDirection;
import net.awired.restmcu.api.resource.client.RestMcuBoardResource;
import net.awired.restmcu.api.resource.client.RestMcuLineResource;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.transport.http_jetty.JettyHTTPDestination;
import org.apache.cxf.transport.http_jetty.JettyHTTPServerEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestMcuConsumer extends DefaultConsumer {

    private static final transient Logger LOG = LoggerFactory.getLogger(RestMcuConsumer.class);

    private Server server;
    private String boardUrl;
    private int lineId;

    public RestMcuConsumer(RestMcuEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        boardUrl = endpoint.findBoardUrl();
        lineId = endpoint.findLineId();
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
        log.debug("Calling board : {} to update notifyUrl with : {}", endpoint.findBoardUrl(), notifyUrl);
        RestMcuBoardResource client = endpoint.getRestContext().buildClient(RestMcuBoardResource.class,
                endpoint.findBoardUrl());
        RestMcuBoardSettings boardSettings = new RestMcuBoardSettings();
        boardSettings.setNotifyUrl(notifyUrl);
        try {
            client.setBoardSettings(boardSettings);
        } catch (Exception e) {
            log.warn("Cannot set notifyUrl for board : " + endpoint.findBoardUrl(), e);
        }
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        RestMcuEndpoint endpoint = (RestMcuEndpoint) getEndpoint();
        URL listeningUrl = findListeningUrl();
        server = endpoint.getRestContext().buildServer(listeningUrl.toString(),
                Arrays.asList(new RestMcuCamelNotifyResource(this)));
        log.debug("Started restmcu notify server {}:{}", listeningUrl.getHost(), findListeningPort());
        //        checkLineIsInput(); // TODO if board is not available at server start this fail and block the server 
        String notifyUrl = findNotifyUrl(listeningUrl);
        updateNotificationUrl(endpoint, notifyUrl);

        server.start();
    }

    private void checkLineIsInput() throws NotFoundException {
        LOG.debug("Get line description from board to check consumer direction : {}", boardUrl);
        RestMcuLineResource restMcuClient = ((RestMcuEndpoint) getEndpoint()).getRestContext().buildClient(
                RestMcuLineResource.class, boardUrl);
        RestMcuLine line = restMcuClient.getLine(lineId);
        if (line.getDirection() != RestMcuLineDirection.INPUT) {
            throw new IllegalStateException("Cannot start a restmcu producer for a non INPUT line : " + line);
        }
    }

    @Override
    protected void doStop() throws Exception {
        server.stop();
        server.destroy();
        server = null;
        super.doStop();
    }

    public Server getServer() {
        return server;
    }

}
