package net.awired.housecream.server.core.application.enumeration;

import ch.qos.logback.classic.Level;

public enum LogLevelEnum {
    all(Level.ALL), //
    trace(Level.TRACE), //
    debug(Level.DEBUG), //
    info(Level.INFO), //
    warn(Level.WARN), //
    error(Level.ERROR), // 
    off(Level.OFF) //
    ;

    private final Level level;

    private LogLevelEnum(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }
}
