package org.housecream.server.api.domain.channel;

import javax.ws.rs.core.Response.Status;
import fr.norad.jaxrs.client.server.api.HttpStatus;

@HttpStatus(Status.NOT_FOUND)
public class ChannelNotFoundException extends Exception {
    public ChannelNotFoundException(String message) {
        super(message);
    }
}
