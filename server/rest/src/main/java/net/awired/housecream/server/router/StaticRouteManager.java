package net.awired.housecream.server.router;

import javax.inject.Inject;
import net.awired.housecream.server.command.CliProcessor;
import net.awired.housecream.server.engine.EngineProcessor;
import net.awired.housecream.server.service.event.EventService;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class StaticRouteManager extends RouteBuilder {

    private static final String DIRECT_ENGINE = "direct:engine";
    public static final String EVENT_HOLDER_QUEUE = "seda:eventHolder?concurrentConsumers=50";
    public static final String OUT_HOLDER_QUEUE = "direct:outHolder";

    @Inject
    private EngineProcessor engine;

    @Inject
    private EngineResultSplitter splitter;

    @Inject
    private CliProcessor cliProcessor;

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
                .bean(eventService, "saveEventAsync") //
                .to(DIRECT_ENGINE);

        from(DIRECT_ENGINE) //
                .process(engine) //
                .split().method(splitter, "split").parallelProcessing() //
                .delay().method(delayer, "calculateDelay").asyncDelayed() //
                .to(OUT_HOLDER_QUEUE);

        from(OUT_HOLDER_QUEUE) //
                .process(consequenceProcessor) //
                .dynamicRouter().method(dynamicRouter, "route") //
                .process(endProcessor);

        from("direct:command").process(cliProcessor);

        from("hcweb:genre").to(DIRECT_ENGINE);

        from(
                "axmpp://talk.google.com:5222/*?serviceName=gmail.com&user=housecream.test@gmail.com&password=AZERTYUIOP")
                .process(cliProcessor)
                .to("axmpp://talk.google.com:5222/alemaire@norad.fr?serviceName=gmail.com&user=housecream.test@gmail.com&password=AZERTYUIOP");
    }
}
