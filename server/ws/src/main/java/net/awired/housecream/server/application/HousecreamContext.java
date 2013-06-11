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
package net.awired.housecream.server.application;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import net.awired.ajsl.core.io.NetworkUtils;
import org.springframework.stereotype.Component;

@Component
@Deprecated
public class HousecreamContext {

    @Inject
    private ServletContext context;

    private Integer port = 8080;
    private String ip;

    public void provideSampleConnection(String ip, int port) {
        if (this.ip == null && !ip.startsWith("127.")) {
            this.ip = ip; //TODO do not store ip localhost (use network)
        }
        if (this.port == null) {
            this.port = port;
        }
    }

    public String getConnectorContextPath() {
        if (ip == null) {
            ip = NetworkUtils.getFirstNonWifiIp();
            if (ip == null) {
                ip = NetworkUtils.getFirstNonLocalhostIp();
            }
        }
        //TODO store in conf the path and update based on connection : domain name in http request

        return "http://" + ip + ":" + port + context.getContextPath(); //TODO PORT may be null
    }

}
