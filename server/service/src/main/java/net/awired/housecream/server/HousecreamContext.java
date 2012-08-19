package net.awired.housecream.server;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import org.springframework.stereotype.Component;

@Component
public class HousecreamContext {

    @Inject
    ServletContext context;

    public String getConnectorContextPath() {
        //TODO store in conf the path and update based on connection : domain name in http request
        return "http://localhost:8080" + context.getContextPath();
    }

}
