package net.awired.housecream.server.router;

import javax.inject.Inject;
import net.awired.housecream.server.command.CommandProcessor;
import net.awired.housecream.server.engine.ConsequenceAction;
import net.awired.housecream.server.engine.EngineProcessor;
import net.awired.housecream.server.engine.StateHolder;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class StaticRouteManager extends RouteBuilder {

    public static final String EVENT_HOLDER_QUEUE = "seda:eventHolder";

    @Inject
    private EngineProcessor engineProcessor;

    @Inject
    private StateHolder stateHolder;

    @Inject
    private EngineResultSplitter splitter;

    @Inject
    private OutDynamicRouter dynamicRouter;

    @Inject
    private CommandProcessor commandProcessor;

    @Override
    public void configure() throws Exception {

        from(EVENT_HOLDER_QUEUE).process(engineProcessor).split().method(splitter, "split").parallelProcessing()
                .to("direct:output");

        from("direct:output").inOut().dynamicRouter().method(dynamicRouter, "route").process(new Processor() {
            @Override
            public void process(Exchange arg0) throws Exception {
                ConsequenceAction action = arg0.getUnitOfWork().getOriginalInMessage()
                        .getHeader("ACTION", ConsequenceAction.class);
                stateHolder.setState(action.getPointId(), action.getValue());
            }
        });

        from("direct:command").process(commandProcessor);

        //        
        from(
                "axmpp://talk.google.com:5222/*?serviceName=gmail.com&user=housecream.test@gmail.com&password=AZERTYUIOP")
                .process(commandProcessor)
                .to("axmpp://talk.google.com:5222/alemaire@norad.fr?serviceName=gmail.com&user=housecream.test@gmail.com&password=AZERTYUIOP");
    }
}
