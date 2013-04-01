package net.awired.housecream.server.oauth;

import org.apache.cxf.rs.security.oauth2.common.OAuthPermission;

public enum AccessScope {

    READ_CALENDAR(new OAuthPermission("readCalendar", "Read the calendar"), true), //
    UPDATE_CALENDAR(new OAuthPermission("updateCalendar-", "Update the calendar at "), false), //

    ;

    private final OAuthPermission permission;

    private AccessScope(OAuthPermission permission, boolean def) {
        this.permission = permission;
        permission.setDefault(def);
    }

    public OAuthPermission getPermission() {
        return permission;
    }

    public static OAuthPermission findOAuthPermission(String scope) {
        for (AccessScope accessScope : values()) {
            if (accessScope.getPermission().getPermission().equals(scope)) {
                return accessScope.getPermission();
            }
        }
        throw new RuntimeException("cannot found OAuthPermission for scope : " + scope);
    }

}
