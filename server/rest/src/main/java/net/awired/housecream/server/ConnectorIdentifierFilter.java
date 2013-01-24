package net.awired.housecream.server;

import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class ConnectorIdentifierFilter extends GenericFilterBean {
    @Inject
    private HousecreamContext hcContext;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        hcContext.provideSampleConnection(request.getLocalAddr(), request.getLocalPort());
        chain.doFilter(request, response);
    }
}
