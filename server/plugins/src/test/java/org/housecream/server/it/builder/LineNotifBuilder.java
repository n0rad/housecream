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
package org.housecream.server.it.builder;

import org.housecream.restmcu.api.domain.line.RestMcuLineNotification;
import org.housecream.restmcu.api.domain.line.RestMcuLineNotify;

public class LineNotifBuilder {

    private int id;
    private RestMcuLineNotify notify;
    private float oldValue;
    private String source;
    private float value;

    public RestMcuLineNotification build() {
        RestMcuLineNotification pinNotification = new RestMcuLineNotification();
        pinNotification.setId(id);
        pinNotification.setNotify(notify);
        pinNotification.setOldValue(oldValue);
        pinNotification.setSource(source);
        pinNotification.setValue(value);
        return pinNotification;
    }

    public LineNotifBuilder id(int id) {
        this.id = id;
        return this;
    }

}
