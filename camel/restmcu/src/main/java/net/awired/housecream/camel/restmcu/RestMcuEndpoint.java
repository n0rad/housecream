package net.awired.housecream.camel.restmcu;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;

/**
 * restmcu://localhost:8976/[lineId]?
 */
public class RestMcuEndpoint extends DefaultEndpoint {

    private static String HTTP_PREFIX = "http://";

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

    public String getRestmcuUrl() {
        URI uri = getEndpointConfiguration().getURI();
        String url = HTTP_PREFIX + uri.getHost();
        if (uri.getPort() != -1) {
            url += ":" + uri.getPort();
        }
        return url;
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
            throw new IllegalArgumentException("Invalid notifyUrl", e);
        }
    }

}
