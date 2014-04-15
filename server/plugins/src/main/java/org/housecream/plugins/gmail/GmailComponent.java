package org.housecream.plugins.gmail;


import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import org.apache.camel.Message;
import org.apache.commons.lang3.tuple.Pair;
import org.housecream.plugins.api.HousecreamPlugin;
import org.housecream.server.api.domain.PluginDescription;
import org.housecream.server.api.domain.config.Config;
import org.housecream.server.api.domain.point.Point;
import org.housecream.server.api.domain.rule.Consequence;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.Credential.Builder;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeCallbackServlet;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeServlet;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;

public class GmailComponent implements HousecreamPlugin {

    private final PluginDescription pluginDescription;
    private GoogleServiceConfig config;

    public GmailComponent() {
        pluginDescription = new PluginDescription()
                .setId("gmail")
                .setName("Gmail")
                .setDescription("<p><b>Gmail</b> is a free (<a href=\"/wiki/Gratis_vs_libre\" title=\"Gratis vs libre\" class=\"mw-redirect\"><i>gratis</i></a>), <a href=\"/wiki/Advertising\" title=\"Advertising\">advertising</a>-supported <a href=\"/wiki/Email\" title=\"Email\">email</a> service provided by <a href=\"/wiki/Google\" title=\"Google\">Google</a>.")
                .setConfigClass(GoogleServiceConfig.class);
    }

    @Override
    public PluginDescription description() {
        return pluginDescription;
    }

    @Override
    public URL getLogo() {
        return getClass().getResource("gmail-logo.png");
    }

    @Override
    public <T extends Config> void setConfig(T config) {
        this.config = (GoogleServiceConfig) config;
    }

    @Override
    public boolean isCommand() {
        return false;
    }

    @Override
    public URI validateAndNormalizeUri(URI pointUri) throws ValidationException {
        return null;
    }

    @Override
    public Pair<Object, Map<String, Object>> prepareOutBodyAndHeaders(Consequence action, Point point) {
        return null;
    }

    @Override
    public Float readValue(Message in) {
        return null;
    }


    ////////////////////////////

    void toto() {
        Credential build = new Builder(BearerToken.queryParameterAccessMethod()).build();

//        Credential credential = new Credential.Builder(accessMethod)
//                .setJsonFactory(jsonFactory)
//                .setTransport(httpTransport)
//                .setTokenServerEncodedUrl(tokenServerEncodedUrl)
//                .setClientAuthentication(clientAuthentication)
//                .setRequestInitializer(requestInitializer)
//                .build();
    }


    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(new NetHttpTransport(), new JacksonFactory(),
            "140281951759-kfsbi7nt3ap7ddck20h1mlq1aseb4omp.apps.googleusercontent.com",
            "ZCzOxbRgJl8vdQ4ppzivEq36",
            Collections.singleton(CalendarScopes.CALENDAR))
            .build();

    public class toto extends AbstractAuthorizationCodeServlet {

        @Override
        protected AuthorizationCodeFlow initializeFlow() throws ServletException, IOException {
            return flow;
        }

        @Override
        protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
            GenericUrl url = new GenericUrl(req.getRequestURL().toString());
            url.setRawPath("/oauth2callback");
            return url.build();
        }

        @Override
        protected String getUserId(HttpServletRequest req) throws ServletException, IOException {
            return "n00rad";
        }
    }

    public class toto2 extends AbstractAuthorizationCodeCallbackServlet {

        @Override
        protected void onError(HttpServletRequest req, HttpServletResponse resp, AuthorizationCodeResponseUrl errorResponse) throws ServletException, IOException {
            resp.sendRedirect("/denied");
        }

        @Override
        protected void onSuccess(HttpServletRequest req, HttpServletResponse resp, Credential credential) throws ServletException, IOException {
            resp.sendRedirect("/granted");
        }

        @Override
        protected AuthorizationCodeFlow initializeFlow() throws ServletException, IOException {
            return flow;
        }

        @Override
        protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
            GenericUrl url = new GenericUrl(req.getRequestURL().toString());
            url.setRawPath("/oauth2callback");
            return url.build();
        }

        @Override
        protected String getUserId(HttpServletRequest req) throws ServletException, IOException {
            return "n00rad";
        }
    }
}
