/**
 *
 *     Copyright (C) Housecream.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package org.housecream.server.router;

import org.apache.camel.builder.RouteBuilder;
import org.housecream.server.command.CliProcessor;
import org.housecream.server.engine.EngineProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StaticRouteManager extends RouteBuilder {

    private Logger log = LoggerFactory.getLogger(getClass());

    public static final String DIRECT_COMMAND = "direct:command";
    public static final String DIRECT_ENGINE = "direct:engine";
    public static final String DIRECT_OUT_PROCESS = "direct:out";
    public static final String SEDA_DELAY = "seda:delay";
    //    public static final String EVENT_HOLDER_QUEUE = "seda:eventHolder?concurrentConsumers=50";

    @Autowired
    private EngineProcessor engine;

    @Autowired
    private EngineResultSplitter splitter;

    @Autowired
    private CliProcessor cliProcessor;

    @Autowired
    private ConsequenceDelayer delayer;

    @Autowired
    private ConsequenceProcessor consequenceProcessor;

    @Autowired
    private OutDynamicRouter dynamicRouter;

    @Autowired
    private OutEndProcessor endProcessor;

    @Autowired
    private ActionAggregationStrategy aggregationStrategy;

    @Override
    public void configure() throws Exception {
        //        from(EVENT_HOLDER_QUEUE) //
        //                .bean(eventService, "saveEventAsync") //
        //                .to(DIRECT_ENGINE);

        from(DIRECT_ENGINE) //
                .bean(aggregationStrategy, "fillCorrelationId") //
                .process(engine) //
                .bean(aggregationStrategy, "fillAggregationSize") //
                .split().method(splitter, "split").parallelProcessing() //
                .bean(aggregationStrategy, "removeCorrelationIdOnAsync") //
                .choice() //
                .when(header(aggregationStrategy.AGGREGATION_ID_HEADER).isNotNull()).to(DIRECT_OUT_PROCESS) //
                .otherwise().inOnly(SEDA_DELAY);

        from(SEDA_DELAY).delay().method(delayer, "calculateDelay").asyncDelayed().to(DIRECT_OUT_PROCESS); //

        from(DIRECT_OUT_PROCESS).process(consequenceProcessor) //
                .dynamicRouter().method(dynamicRouter, "route") //
                .process(endProcessor); //
        //                .aggregate(header(AGGREGATION_ID_HEADER), aggregationStrategy) //
        //                .completionTimeout(3000).completionSize(header(AGGREGATION_SIZE_HEADER)) //
        //                .ignoreInvalidCorrelationKeys().closeCorrelationKeyOnCompletion(10) //
        //                .process(new Processor() {
        //                    @Override
        //                    public void process(Exchange exchange) throws Exception {
        //                        log.debug("Receive aggregation result {}", exchange);
        //                        System.out.println("GENRESTYLEOUDA");
        //                    }
        //                });

        from(DIRECT_COMMAND).process(cliProcessor);

        from("hcweb:genre").to(DIRECT_ENGINE);

        //        from(
        //                "axmpp://talk.google.com:5222/*?serviceName=gmail.com&user=housecream.test@gmail.com&password=AZERTYUIOP")
        //                .process(cliProcessor)
        //                .to("axmpp://talk.google.com:5222/alemaire@norad.fr?serviceName=gmail.com&user=housecream.test@gmail.com&password=AZERTYUIOP");
    }
}
