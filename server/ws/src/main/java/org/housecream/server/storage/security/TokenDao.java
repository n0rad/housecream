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
import fr.norad.jaxrs.oauth2.api.TokenNotFoundException;
import fr.norad.jaxrs.oauth2.api.spec.domain.GrantType;
import fr.norad.jaxrs.oauth2.api.spec.domain.Token;
import fr.norad.jaxrs.oauth2.core.persistence.TokenRepository;

@Repository
public class TokenDao implements TokenRepository {

    private final Session session;
    private final PreparedStatement selectQuery;
    private final PreparedStatement insertQuery;
    private final PreparedStatement deleteQuery;

    @Autowired
    public TokenDao(Session session) {
        this.session = session;
        selectQuery = session.prepare("SELECT * FROM tokens WHERE accessToken = ?");
        deleteQuery = session.prepare("DELETE FROM tokens WHERE accessToken = ?");
        insertQuery = session.prepare("INSERT INTO tokens(" +
                "accessToken, scopes, username, issuedAt, grantType, tokenType, clientId, lifetime, refreshToken" +
                ") VALUES(?,?,?,?,?,?,?,?,?)");
    }

    @Override
    public void saveToken(Token token) {
        session.execute(insertQuery.bind(token.getAccessToken(),
                token.getScopes(),
                token.getUsername(),
                token.getIssuedAt(),
                token.getGrantType().toString(),
                token.getTokenType(),
                token.getClientId(),
                token.getLifetime(),
                token.getRefreshToken()));
    }

    @Override
    public void deleteToken(String accessToken) {
        session.execute(deleteQuery.bind(accessToken));
    }

    @Override
    public Token findToken(String accessToken) throws TokenNotFoundException {
        ResultSet rows = session.execute(selectQuery.bind(accessToken));
        if (rows.isExhausted()) {
            throw new TokenNotFoundException("Cannot found token with accessToken = " + accessToken);
        }
        return bind(rows.one());
    }

    private Token bind(Row row) {
        Token token = new Token();
        token.setAccessToken(row.getString("accessToken"));
        token.setScopes(row.getSet("scopes", String.class));
        token.setUsername(row.getString("username"));
        token.setGrantType(GrantType.valueOf(row.getString("grantType")));
        token.setClientId(row.getString("clientId"));
        token.setIssuedAt(row.getLong("issuedAt"));
        token.setLifetime(row.getInt("lifetime"));
        token.setRefreshToken(row.getString("refreshToken"));
        token.setTokenType(row.getString("tokenType"));
        return token;
    }
}
