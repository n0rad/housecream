package net.awired.housecream.server.oauth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.cxf.rs.security.oauth2.common.AccessTokenRegistration;
import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.common.OAuthPermission;
import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken;
import org.apache.cxf.rs.security.oauth2.common.UserSubject;
import org.apache.cxf.rs.security.oauth2.grants.code.AuthorizationCodeDataProvider;
import org.apache.cxf.rs.security.oauth2.grants.code.AuthorizationCodeRegistration;
import org.apache.cxf.rs.security.oauth2.grants.code.ServerAuthorizationCodeGrant;
import org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException;
import org.apache.cxf.rs.security.oauth2.tokens.bearer.BearerAccessToken;
import org.springframework.stereotype.Component;

@Component
public class OAuthManager implements AuthorizationCodeDataProvider {

    private Client client;
    private ServerAuthorizationCodeGrant grant;
    private ServerAccessToken at;

    public OAuthManager() {
        client = new Client("clientId", "secret", true);
        client.setAllowedGrantTypes(Arrays.asList("password"));
    }

    public void registerClient(Client c) {
        this.client = c;
    }

    @Override
    public Client getClient(String clientId) throws OAuthServiceException {
        return client == null || !client.getClientId().equals(clientId) ? null : client;
    }

    // grant management
    @Override
    public ServerAuthorizationCodeGrant createCodeGrant(AuthorizationCodeRegistration reg)
            throws OAuthServiceException {
        grant = new ServerAuthorizationCodeGrant(client, 3600L);
        grant.setRedirectUri(reg.getRedirectUri());
        grant.setSubject(reg.getSubject());
        List<String> scope = reg.getApprovedScope().isEmpty() ? reg.getRequestedScope() : reg.getApprovedScope();
        grant.setApprovedScopes(scope);
        return grant;
    }

    @Override
    public ServerAuthorizationCodeGrant removeCodeGrant(String code) throws OAuthServiceException {
        ServerAuthorizationCodeGrant theGrant = null;
        if (grant.getCode().equals(code)) {
            theGrant = grant;
            grant = null;
        }
        return theGrant;
    }

    // token management
    @Override
    public ServerAccessToken createAccessToken(AccessTokenRegistration reg) throws OAuthServiceException {
        ServerAccessToken token = new BearerAccessToken(reg.getClient(), 3600L);

        List<String> scope = reg.getApprovedScope().isEmpty() ? reg.getRequestedScope() : reg.getApprovedScope();
        token.setScopes(convertScopeToPermissions(reg.getClient(), scope));
        token.setSubject(reg.getSubject());
        token.setGrantType(reg.getGrantType());

        at = token;

        return token;
    }

    @Override
    public ServerAccessToken getAccessToken(String tokenId) throws OAuthServiceException {
        return at == null || !at.getTokenKey().equals(tokenId) ? null : at;
    }

    @Override
    public void removeAccessToken(ServerAccessToken token) throws OAuthServiceException {
        at = null;
    }

    @Override
    public ServerAccessToken refreshAccessToken(Client clientId, String refreshToken, List<String> scopes)
            throws OAuthServiceException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ServerAccessToken getPreauthorizedToken(Client client, List<String> scopes, UserSubject subject,
            String grantType) throws OAuthServiceException {
        return null;
    }

    @Override
    public List<OAuthPermission> convertScopeToPermissions(Client client, List<String> scopes) {
        List<OAuthPermission> list = new ArrayList<OAuthPermission>();
        for (String scope : scopes) {
            list.add(AccessScope.findOAuthPermission(scope));
        }
        if (!scopes.contains(AccessScope.READ_CALENDAR.getPermission())) {
            list.add(AccessScope.READ_CALENDAR.getPermission());
        }
        return list;
    }
}
