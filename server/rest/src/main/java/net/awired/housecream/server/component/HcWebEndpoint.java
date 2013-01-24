package net.awired.housecream.server.component;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.camel.impl.DefaultEndpoint;

public class HcWebEndpoint extends DefaultEndpoint {

    private DefaultConsumer defaultConsumer;

    public HcWebEndpoint(String uri, HcWebComponent component) {
        super(uri, component);
    }

    @Override
    public Producer createProducer() throws Exception {
        return null;
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        return new HcWebConsumer(this, processor);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
