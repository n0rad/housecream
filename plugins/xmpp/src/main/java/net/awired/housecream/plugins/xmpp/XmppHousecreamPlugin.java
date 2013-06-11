/**
 *
 *     Housecream.org project
 *     Copyright (C) Awired.net
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
package net.awired.housecream.plugins.xmpp;

import java.net.URI;
import java.util.Map;
import javax.validation.ValidationException;
import net.awired.housecream.plugins.api.InHousecreamPlugin;
import net.awired.housecream.plugins.api.OutHousecreamPlugin;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.rule.Consequence;
import org.apache.camel.Message;
import org.apache.commons.lang3.tuple.Pair;

public class XmppHousecreamPlugin implements InHousecreamPlugin, OutHousecreamPlugin {

    @Override
    public String scheme() {
        return "axmpp";
    }

    @Override
    public Pair<Object, Map<String, Object>> prepareOutBodyAndHeaders(Consequence action, OutPoint outpoint) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isCommand() {
        return true;
    }

    @Override
    public URI validateAndNormalizeUri(URI pointUri) throws ValidationException {
        return null;
    }

    @Override
    public Float readInValue(Message in) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

}
