package net.awired.housecream.server.component;

import net.awired.housecream.server.ApplicationContextProvider;
import net.awired.housecream.server.command.CommandService;
import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;

public class HcWebConsumer extends DefaultConsumer {

    public HcWebConsumer(Endpoint endpoint, Processor processor) {
        super(endpoint, processor);
    }

    @Override
    public void start() throws Exception {
        super.start();
        ApplicationContextProvider.getInstance().getBean(CommandService.class).setConsumer(this);
    }

    @Override
    public void stop() throws Exception {
        ApplicationContextProvider.getInstance().getBean(CommandService.class).setConsumer(null);
        super.stop();
    }

}
