package net.awired.housecream.server.command;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import com.google.common.base.Charsets;

@Component
public class CliProcessor implements Processor {

    private CommandArgumentManager argumentManager = new CommandArgumentManager();

    @Override
    public void process(Exchange exchange) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        argumentManager.setOutputStream(ps);
        argumentManager.setErrorStream(ps);
        if (argumentManager.parseWithSuccess(exchange.getIn().getBody(String.class).split(" "))) {
            //TODO
        }

        exchange.getIn().setBody(baos.toString(Charsets.UTF_8.displayName()));
    }
}
