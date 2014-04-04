/**
 *
 *     Copyright (C) Housecream.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package org.housecream.server.storage.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import fr.norad.jaxrs.oauth2.api.spec.domain.GrantType;
import fr.norad.jaxrs.oauth2.core.domain.RefreshToken;
import fr.norad.jaxrs.oauth2.core.persistence.RefreshTokenNotFoundException;
import fr.norad.jaxrs.oauth2.core.persistence.RefreshTokenRepository;

@Repository
public class RefreshTokenDao implements RefreshTokenRepository {

    private final Session session;
    private final PreparedStatement selectQuery;
    private final PreparedStatement insertQuery;
    private final PreparedStatement deleteQuery;

    @Autowired
    public RefreshTokenDao(Session session) {
        this.session = session;
        selectQuery = session.prepare("SELECT * FROM refresh_tokens WHERE refreshToken = ?");
        deleteQuery = session.prepare("DELETE FROM refresh_tokens WHERE refreshToken = ?");
        insertQuery = session.prepare("INSERT INTO refresh_tokens(" +
                "refreshToken, sourceAccessToken, issuedAt, lifetime, grantType, username, clientId, scopes" +
                ") VALUES(?,?,?,?,?,?,?,?)");
    }

    @Override
    public RefreshToken findRefreshToken(String refreshToken) throws RefreshTokenNotFoundException {
        ResultSet rows = session.execute(selectQuery.bind(refreshToken));
        if (rows.isExhausted()) {
            throw new RefreshTokenNotFoundException("Cannot found refreshToken with refreshToken = " + refreshToken);
        }
        return bind(rows.one());
    }

    @Override
    public void saveRefreshToken(RefreshToken refreshToken) {
        session.execute(insertQuery.bind(refreshToken.getRefreshToken(),
                refreshToken.getSourceAccessToken(),
                refreshToken.getIssuedAt(),
                refreshToken.getLifetime(),
                refreshToken.getGrantType().toString(),
                refreshToken.getUsername(),
                refreshToken.getClientId(),
                refreshToken.getScopes()));
    }

    @Override
    public void deleteRefreshToken(String refreshToken) {
        session.execute(deleteQuery.bind(refreshToken));
    }

    private RefreshToken bind(Row row) {
        RefreshToken token = new RefreshToken();
        token.setSourceAccessToken(row.getString("sourceAccessToken"));
        token.setScopes(row.getSet("scopes", String.class));
        token.setUsername(row.getString("username"));
        token.setGrantType(GrantType.valueOf(row.getString("grantType")));
        token.setClientId(row.getString("clientId"));
        token.setIssuedAt(row.getLong("issuedAt"));
        token.setLifetime(row.getInt("lifetime"));
        token.setRefreshToken(row.getString("refreshToken"));
        return token;
    }
}
