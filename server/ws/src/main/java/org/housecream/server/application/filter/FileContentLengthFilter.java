package org.housecream.server.application.filter;

import java.io.File;
import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class FileContentLengthFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {
        if (responseContext.getEntity() instanceof File
                && responseContext.getHeaderString(HttpHeaders.CONTENT_LENGTH) == null) {
            File file = (File) responseContext.getEntity();
            responseContext.getHeaders().putSingle(HttpHeaders.CONTENT_LENGTH, file.length());
        }
    }
}
