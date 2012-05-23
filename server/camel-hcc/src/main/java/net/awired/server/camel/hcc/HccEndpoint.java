package net.awired.server.camel.hcc;

import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultPollingEndpoint;

public class HccEndpoint extends DefaultPollingEndpoint {

    @Override
    public Producer createProducer() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isSingleton() {
        // TODO Auto-generated method stub
        return false;
    }

}
