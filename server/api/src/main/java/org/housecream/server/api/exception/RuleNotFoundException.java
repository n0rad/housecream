package org.housecream.server.api.exception;


import javax.ws.rs.core.Response.Status;
import fr.norad.jaxrs.client.server.api.HttpStatus;

@HttpStatus(Status.NOT_FOUND)
public class RuleNotFoundException extends Exception {
    public RuleNotFoundException(String message) {
        super(message);
    }
}
