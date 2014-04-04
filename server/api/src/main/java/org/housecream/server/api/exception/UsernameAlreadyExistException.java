package org.housecream.server.api.exception;


import javax.ws.rs.core.Response.Status;
import fr.norad.jaxrs.client.server.api.HttpStatus;

@HttpStatus(Status.CONFLICT)
public class UsernameAlreadyExistException extends Exception {
    public UsernameAlreadyExistException(String message) {
        super(message);
    }
}
