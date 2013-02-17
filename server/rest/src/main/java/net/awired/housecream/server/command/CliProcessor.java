package net.awired.housecream.server.command;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import javax.inject.Inject;
import net.awired.aclm.argument.args.CliNoParamArgument;
import net.awired.aclm.argument.args.CliOneParamArgument;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.storage.dao.InPointDao;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import com.google.common.base.Charsets;

@Component
public class CliProcessor implements Processor {

    private CommandArgumentManager argumentManager = new CommandArgumentManager();

    @Inject
    private InPointDao inPointDao;

    @Override
    public void process(Exchange exchange) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        argumentManager.setOutputStream(ps);
        argumentManager.setErrorStream(ps);
        try {
            if (argumentManager.parseWithSuccess(exchange.getIn().getBody(String.class).split(" "))) {
                if (argumentManager.getInpoint().isSet()) {
                    processInPoint(argumentManager.getInpoint(), argumentManager.getId(), ps);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(ps);
        }
        exchange.getIn().setBody(baos.toString(Charsets.UTF_8.displayName()));
    }

    private void processInPoint(CliNoParamArgument inpoint, CliOneParamArgument<Long> id, PrintStream ps)
            throws NotFoundException {
        if (id.isSet()) {
            InPoint find = inPointDao.find(id.getParamOneValue());
            ps.print(find.toString());
        } else {
            List<InPoint> findAll = inPointDao.findAll();
            ps.print(findAll.toString());
        }
    }
}
