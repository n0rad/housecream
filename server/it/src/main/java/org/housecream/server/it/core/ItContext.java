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
package org.housecream.server.it.core;

import fr.norad.core.io.NetworkUtils;

public class ItContext {

    private static final String HCWS_HOST_DEFAULT = "localhost";
    private static final String HCWS_HOST_PROPERTY_NAME = "hcws.host";

    private static final String HCWS_PORT_DEFAULT = "8080";
    private static final String HCWS_PORT_PROPERTY_NAME = "hcws.port";

    private static final String HCWS_PATH_DEFAULT = "/housecream-ws/ws";
    private static final String HCWS_PATH_PROPERTY_NAME = "hcws.path";

    private static String hcwsHost;

    static {
        hcwsHost = NetworkUtils.getFirstNonWifiIp();
        if (hcwsHost == null) {
            hcwsHost = NetworkUtils.getFirstNonLocalhostIp();
        }
        if (hcwsHost == null) {
            hcwsHost = HCWS_HOST_DEFAULT;
        }
    }

    public static String getUrl() {
        return "http://" + hcwsHost + ":" + System.getProperty(HCWS_PORT_PROPERTY_NAME, HCWS_PORT_DEFAULT) //
                + System.getProperty(HCWS_PATH_PROPERTY_NAME, HCWS_PATH_DEFAULT);
    }

    public static String getAuthUrl() {
        return getUrl() + "/oauth2";
    }

}
