package net.awired.housecream.server.common.resource;

public class NoFloorFoundException extends Exception {

    public NoFloorFoundException(String msg, Throwable e) {
        super(msg, e);
    }
}
