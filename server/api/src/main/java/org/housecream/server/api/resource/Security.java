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
package org.housecream.server.api.resource;

import static fr.norad.jaxrs.oauth2.api.ScopeStrategy.ALL;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashSet;
import java.util.Set;
import fr.norad.jaxrs.oauth2.api.Scope;
import fr.norad.jaxrs.oauth2.api.ScopeStrategy;
import fr.norad.jaxrs.oauth2.api.Secured;

@Secured
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Security {
    Scopes[] value();

    ScopeStrategy strategy() default ALL;

    public enum Scopes implements Scope {
        INPOINT_READ,
        INPOINT_WRITE,
        USER_READ,
        USER_WRITE,
        PROPERTIES_READ,
        PROPERTIES_WRITE,
        CLIENT_READ,
        CLIENT_WRITE,
        ZONE_READ,
        ZONE_WRITE,
        OUTPOINT_READ,
        OUTPOINT_WRITE,
        RULE_READ,
        RULE_WRITE,;

        @Override
        public String scopeIdentifier() {
            return name();
        }

        public static Set<String> toStrings() {
            Scopes[] values = Scopes.values();
            Set<String> res = new HashSet<>(values.length);
            for (Scopes str : values) {
                res.add(str.name());
            }
            return res;
        }
    }

}