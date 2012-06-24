package net.awired.housecream.server.core;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import org.springframework.stereotype.Component;

@Component
public class HouseCreamContext {

    @Inject
    ServletContext context;

    public String getConnectorContextPath() {
        return "http://localhost:8080" + context.getContextPath();
    }

}
