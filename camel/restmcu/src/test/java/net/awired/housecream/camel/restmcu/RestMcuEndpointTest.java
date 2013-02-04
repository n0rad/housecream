package net.awired.housecream.camel.restmcu;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.net.URL;
import org.apache.camel.EndpointConfiguration;
import org.junit.Test;

public class RestMcuEndpointTest {

    @Test
    public void should_set_notify_url_with_http() throws Exception {
        String notifyUrl = "http://192.168.42.4:0";
        String uri = "restmcu://toto.com:50/5";
        String url = "http://toto.com:50/5";

        RestMcuComponent component = mock(RestMcuComponent.class);
        EndpointConfiguration value = mock(EndpointConfiguration.class);
        when(component.createConfiguration(anyString())).thenReturn(value);
        when(value.getURI()).thenReturn(new URL(url).toURI());
        RestMcuEndpoint restMcuEndpoint = new RestMcuEndpoint(uri, component);

        restMcuEndpoint.setNotifyUrl(notifyUrl);

        assertThat(restMcuEndpoint.getNotifyUrl()).isEqualTo(new URL("http://192.168.42.4:0"));
    }

    @Test
    public void should_set_notify_url_without_http() throws Exception {
        String notifyUrl = "192.168.42.4:0";
        String uri = "restmcu://toto.com:50/5";
        String url = "http://toto.com:50/5";

        RestMcuComponent component = mock(RestMcuComponent.class);
        EndpointConfiguration value = mock(EndpointConfiguration.class);
        when(component.createConfiguration(anyString())).thenReturn(value);
        when(value.getURI()).thenReturn(new URL(url).toURI());
        RestMcuEndpoint restMcuEndpoint = new RestMcuEndpoint(uri, component);

        restMcuEndpoint.setNotifyUrl(notifyUrl);

        assertThat(restMcuEndpoint.getNotifyUrl()).isEqualTo(new URL("http://192.168.42.4:0"));
    }

}
