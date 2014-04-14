package org.housecream.server.application.filter;

import java.io.File;
import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ProbeMediaTypeFilter implements ContainerResponseFilter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final Tika tika = new Tika();

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        if (responseContext.getEntity() instanceof File) {
            File file = (File) responseContext.getEntity();
            try {
                String contentType = tika.detect(file);
                responseContext.getHeaders().putSingle(HttpHeaders.CONTENT_TYPE, contentType);
            } catch (IOException e) {
                logger.warn("Unable to probe file content type", e);
            }
        }
    }
}
