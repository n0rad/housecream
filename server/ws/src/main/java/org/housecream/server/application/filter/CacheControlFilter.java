package org.housecream.server.application.filter;

import static org.housecream.server.application.filter.Cached.Privacy.PRIVATE;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import org.joda.time.DateTime;

public class CacheControlFilter implements ContainerResponseFilter {

    private final int cacheDuration;

    private final boolean privateCache;

    public CacheControlFilter(int cacheDuration) {
        this.cacheDuration = cacheDuration;
        this.privateCache = true;
    }

    public CacheControlFilter(int cacheDuration, Cached.Privacy privacy) {
        this.cacheDuration = cacheDuration;
        this.privateCache = PRIVATE.equals(privacy);
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        Date lastModified = getLastModifiedTime(responseContext);
        if (lastModified != null) {
            evaluatePreconditions(requestContext, responseContext, lastModified);
        }
        addCacheHeaders(responseContext);
    }

    private void addCacheHeaders(ContainerResponseContext responseContext) {
        final Object expiresValue;
        CacheControl cacheControl = new CacheControl();
        cacheControl.setNoTransform(false);
        if (cacheDuration == 0) {
            expiresValue = "0";
            cacheControl.setNoCache(true);
            cacheControl.setNoStore(true);
            cacheControl.setMustRevalidate(true);
        } else {
            expiresValue = DateTime.now().plusSeconds(cacheDuration).toDate();
            cacheControl.setPrivate(privateCache);
            cacheControl.setMaxAge(cacheDuration);
        }

        responseContext.getHeaders().putSingle(HttpHeaders.EXPIRES, expiresValue);
        responseContext.getHeaders().putSingle(HttpHeaders.CACHE_CONTROL, cacheControl);
    }

    private Date getLastModifiedTime(ContainerResponseContext responseContext) {
        Date lastModifiedHeader = responseContext.getLastModified();
        if (lastModifiedHeader != null) {
            return lastModifiedHeader;
        }
        if (responseContext.getEntity() instanceof File) {
            return findFileLastModified((File) responseContext.getEntity());
        }
        return null;
    }

    private Date findFileLastModified(File file) {
        try {
            return new Date(Files.getLastModifiedTime(file.toPath()).toMillis());
        } catch (IOException e) {
            return null;
        }
    }

    private void evaluatePreconditions(ContainerRequestContext requestContext,
                                       ContainerResponseContext responseContext,
                                       Date lastModified) {
        responseContext.getHeaders().putSingle(HttpHeaders.LAST_MODIFIED, lastModified);
        ResponseBuilder builder = requestContext.getRequest().evaluatePreconditions(lastModified);
        if (builder != null) {
            responseContext.setEntity(null, responseContext.getEntityAnnotations(), responseContext.getMediaType());
            responseContext.setStatusInfo(Status.NOT_MODIFIED);
        }
    }
}
