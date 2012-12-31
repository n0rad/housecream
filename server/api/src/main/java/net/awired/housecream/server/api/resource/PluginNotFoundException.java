package net.awired.housecream.server.api.resource;

public class PluginNotFoundException extends Exception {

    public PluginNotFoundException(String message) {
        super(message);
    }

    public PluginNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
