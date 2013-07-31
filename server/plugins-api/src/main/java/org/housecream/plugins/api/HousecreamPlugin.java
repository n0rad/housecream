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
package org.housecream.plugins.api;

import java.net.URI;
import javax.validation.ValidationException;

public interface HousecreamPlugin {

    String scheme();

    //TODO MOVE
    //
    //    Float getCurrentValue(Point point, CamelContext camelContext);
    //

    boolean isCommand();

    /**
     * @return can return null if valid.
     */
    URI validateAndNormalizeUri(URI pointUri) throws ValidationException;

}