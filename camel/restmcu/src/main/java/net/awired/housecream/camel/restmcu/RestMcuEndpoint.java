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
package net.awired.housecream.camel.restmcu;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import net.awired.ajsl.ws.rest.RestBuilder;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;

/**
 * restmcu://localhost:8976/[lineId]?notifyUrl=192.168.42.4:8787
 */
public class RestMcuEndpoint extends DefaultEndpoint {

    private static String HTTP_PREFIX = "http://";

    private RestBuilder restContext = new RestBuilder();

    private URL notifyUrl;
    private int lineNumber;

    public RestMcuEndpoint(String uri, RestMcuComponent component) {
        super(uri, component);
        String path = getEndpointConfiguration().getURI().getPath();
        try {
            lineNumber = Integer.parseInt(path.substring(1)); // skip / from path
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid line number : " + path, e);
        }
    }

    @Override
    public Producer createProducer() throws Exception {
        return new RestMcuProducer(this);
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        return new RestMcuConsumer(this, processor);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public String findBoardUrl() {
        URI uri = getEndpointConfiguration().getURI();
        String url = HTTP_PREFIX + uri.getHost();
        if (uri.getPort() != -1) {
            url += ":" + uri.getPort();
        }
        return url;
    }

    public int findLineId() {
        return Integer.valueOf(getEndpointConfiguration().getURI().getPath().substring(1));
    }

    public URL getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        try {
            if (notifyUrl.startsWith(HTTP_PREFIX)) {
                this.notifyUrl = new URL(notifyUrl);
            } else {
                this.notifyUrl = new URL(HTTP_PREFIX + notifyUrl);
            }
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid notifyUrl : " + notifyUrl, e);
        }
    }

    public RestBuilder getRestContext() {
        return restContext;
    }

    public void setRestContext(RestBuilder restContext) {
        this.restContext = restContext;
    }

}
