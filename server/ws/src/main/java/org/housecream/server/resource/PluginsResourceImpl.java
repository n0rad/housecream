package org.housecream.server.resource;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import org.apache.tika.Tika;
import org.housecream.server.api.domain.PluginDescription;
import org.housecream.server.api.exception.PluginNotFoundException;
import org.housecream.server.api.resource.ChannelsResource.PluginResource;
import org.housecream.server.api.resource.ChannelsResource.PluginsResource;
import org.housecream.server.service.PluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;

@Component
public class PluginsResourceImpl implements PluginsResource {

    @Autowired
    private PluginResource pluginResource;

    @Autowired
    private PluginService pluginService;

    @Override
    public List<PluginDescription> descriptions() {
        return pluginService.getDescriptions();
    }

    @Override
    public PluginResource plugin(String id) {
        return pluginResource;
    }

    @Component
    public static class PluginResourceImpl implements PluginResource {

        @Autowired
        private PluginService pluginService;

        private Tika tika = new Tika();

        GoogleAuthorizationCodeFlow flow42 = new GoogleAuthorizationCodeFlow.Builder(new NetHttpTransport(), new JacksonFactory(),
                "140281951759-p7lb4ngqr5vc40h6mm1no3s12r71rglr.apps.googleusercontent.com",
                "XVgRpImkMa3-pa8dKcN_YNeY",
                Collections.singleton(CalendarScopes.CALENDAR))
                .build();

        @Override
        public void activate(String id, HttpServletRequest req,
                             HttpServletResponse resp) throws PluginNotFoundException {
            lock.lock();
            try {
                // load credential from persistence store
                String userId = getUserId(req);
                if (flow == null) {
                    flow = initializeFlow();
                }
                credential = flow.loadCredential(userId);
                // if credential found with an access token, invoke the user code
                if (credential != null && credential.getAccessToken() != null) {
                    String userId42 = getUserId(req);
//                    try {
//
//                        return;
//                    } catch (HttpResponseException e) {
//                        // if access token is null, assume it is because auth failed and we need to re-authorize
//                        // but if access token is not null, it is some other problem
//                        if (credential.getAccessToken() != null) {
//                            throw e;
//                        }
//                    }
                }
                // redirect to the authorization flow
                AuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl();
                authorizationUrl.setRedirectUri(getRedirectUri(req));
                onAuthorization(req, resp, authorizationUrl);
                credential = null;
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }

        /**
         * Lock on the flow and credential.
         */
        private final Lock lock = new ReentrantLock();

        /**
         * Persisted credential associated with the current request or {@code null} for none.
         */
        private Credential credential;

        /**
         * Authorization code flow to be used across all HTTP servlet requests or {@code null} before
         * initialized in {@link #initializeFlow()}.
         */
        private AuthorizationCodeFlow flow;

        protected void onAuthorization(HttpServletRequest req, HttpServletResponse resp,
                                       AuthorizationCodeRequestUrl authorizationUrl) throws ServletException, IOException {
            resp.sendRedirect(authorizationUrl.setState("styleouda").build());
        }

        protected AuthorizationCodeFlow initializeFlow() throws ServletException, IOException {
            return flow42;
        }

        protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
            GenericUrl url = new GenericUrl(req.getRequestURL().toString());
            //url.setRawPath("/oauth2callback");
            return "http://localhost:4500/admin/channel/available/gmail/callback";
//            return url.build();
        }

        protected String getUserId(HttpServletRequest req) throws ServletException, IOException {
            return "n00rad";
        }


        @Override
        public void callback(String id) throws PluginNotFoundException {

        }

        @Override
        public Response getLogo(String id) throws IOException, PluginNotFoundException {
            URL url = pluginService.getPlugin(id).getLogo();
            return Response.ok(url.openStream()).type(tika.detect(url)).build();
        }

        @Override
        public PluginDescription getPlugin(String id) throws PluginNotFoundException {
            return pluginService.getPlugin(id).description();
        }

    }

}
