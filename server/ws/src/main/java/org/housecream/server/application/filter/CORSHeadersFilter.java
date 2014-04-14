package org.housecream.server.application.filter;

import static javax.ws.rs.HttpMethod.DELETE;
import static javax.ws.rs.HttpMethod.GET;
import static javax.ws.rs.HttpMethod.OPTIONS;
import static javax.ws.rs.HttpMethod.POST;
import static javax.ws.rs.HttpMethod.PUT;
import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.HttpHeaders.CACHE_CONTROL;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import java.io.IOException;
import java.util.Set;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;

//@Provider
//@Component
public class CORSHeadersFilter implements ContainerResponseFilter {

    private static final String HEADER_ORIGIN = "Origin";
    private static final String HEADER_AC_ALLOW_METHODS = "Access-Control-Allow-Methods";
    private static final String HEADER_AC_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    private static final String HEADER_AC_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    private static final String HEADER_AC_EXPOSE_HEADERS = "Access-Control-Expose-Headers-HEADERS";
    private static final String HEADER_AC_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    private static final String X_ON_APPID = "X-On-Appid";
    private static final String X_ON_SIGN = "X-On-Sign";
    private static final String X_REQUESTED_WITH = "X-Requested-With";
    private static final String SET_COOKIE = "Set-Cookie";


    private Set<String> allowedOrigins;

    public static CORSHeadersFilter enableCORSWithOrigins(Set<String> origins) {
        return new CORSHeadersFilter(origins);
    }

    public static CORSHeadersFilter enableCORSWithOrigins(String origins) {
        return new CORSHeadersFilter(origins);
    }

    @Autowired
    public CORSHeadersFilter(@Value("${api.cors.origin}") String corsOriginsAsString) {
        this.allowedOrigins = Sets.newHashSet(Splitter.on(',').trimResults().omitEmptyStrings().split
                (corsOriginsAsString));
    }

    private CORSHeadersFilter(Set<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {
        String originHeader = requestContext.getHeaderString(HEADER_ORIGIN);

        if (originHeader != null) {
            MultivaluedMap<String, Object> headers = responseContext.getHeaders();
            headers.putSingle(HEADER_AC_ALLOW_METHODS, Joiner.on(", ").join(GET, PUT, POST, DELETE, OPTIONS));
            headers.putSingle(HEADER_AC_ALLOW_HEADERS, Joiner.on(", ").join(ACCEPT, CACHE_CONTROL, X_ON_APPID,
                    X_REQUESTED_WITH, CONTENT_TYPE, AUTHORIZATION, X_ON_SIGN));
            headers.putSingle(HEADER_AC_ALLOW_CREDENTIALS, Boolean.TRUE);
            headers.putSingle(HEADER_AC_EXPOSE_HEADERS, SET_COOKIE);

            if (allowedOrigins.contains(originHeader)) {
                headers.putSingle(HEADER_AC_ALLOW_ORIGIN, originHeader);
            } else {
                headers.putSingle(HEADER_AC_ALLOW_ORIGIN, Joiner.on(' ').skipNulls().join(allowedOrigins));
            }
        }
    }
}
