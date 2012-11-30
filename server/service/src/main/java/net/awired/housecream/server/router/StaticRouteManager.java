package net.awired.housecream.server.router;

import javax.inject.Inject;
import net.awired.housecream.server.api.domain.Event;
import net.awired.housecream.server.command.CommandProcessor;
import net.awired.housecream.server.engine.EngineProcessor;
import net.awired.housecream.server.service.event.EventService;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class StaticRouteManager extends RouteBuilder {

    public static final String EVENT_HOLDER_QUEUE = "seda:eventHolder";

    @Inject
    private EngineProcessor engine;

    @Inject
    private EngineResultSplitter splitter;

    @Inject
    private CommandProcessor commandProcessor;

    @Inject
    private ConsequenceDelayer delayer;

    @Inject
    private ConsequenceProcessor consequenceProcessor;

    @Inject
    private EventService eventService;

    @Inject
    private OutDynamicRouter dynamicRouter;

    @Inject
    private OutEndProcessor endProcessor;

    @Override
    public void configure() throws Exception {

        from(EVENT_HOLDER_QUEUE) //
                .transform(body(Event.class)) //
                .bean(eventService, "saveEventAsync") //
                .process(engine) //
                .split().method(splitter, "split").parallelProcessing() //
                .delay().method(delayer, "calculateDelay") //
                .to("direct:output");

        from("direct:output") //
                .process(consequenceProcessor) //
                .dynamicRouter().method(dynamicRouter, "route") //
                .process(endProcessor);

        from("direct:command").process(commandProcessor);

        //        
        from(
                "axmpp://talk.google.com:5222/*?serviceName=gmail.com&user=housecream.test@gmail.com&password=AZERTYUIOP")
                .process(commandProcessor)
                .to("axmpp://talk.google.com:5222/alemaire@norad.fr?serviceName=gmail.com&user=housecream.test@gmail.com&password=AZERTYUIOP");
    }
}
