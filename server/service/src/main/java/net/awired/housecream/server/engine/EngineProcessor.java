package net.awired.housecream.server.engine;

import java.util.ArrayList;
import java.util.Collection;
import javax.inject.Inject;
import net.awired.housecream.server.common.domain.Point;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.definition.KnowledgePackage;
import org.drools.event.rule.DebugAgendaEventListener;
import org.drools.event.rule.DebugWorkingMemoryEventListener;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EngineProcessor implements Processor {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();

    @Inject
    private StateHolder stateHolder;

    public void registerPoint(Point point) {

    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Event body = exchange.getIn().getBody(Event.class);

        final StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        ksession.addEventListener(new DebugAgendaEventListener());
        ksession.addEventListener(new DebugWorkingMemoryEventListener());

        // setup the audit logging
        KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "hcs.droolsLogs");

        for (Object fact : stateHolder.getFacts()) {
            log.debug("Inserting fact: " + fact);
            FactHandle insert = ksession.insert(fact);
        }
        FactHandle eventFact = ksession.insert(body);
        Actions actions = new Actions();
        ArrayList<String> camelUrls = new ArrayList<String>();

        exchange.getIn().setBody(actions);
        exchange.getIn().setHeader("outputActions", camelUrls);
        FactHandle outputFact = ksession.insert(actions);

        ksession.fireAllRules();

        ksession.retract(outputFact);
        ksession.retract(eventFact);
        stateHolder.setState(body.getPointId(), body.getValue());

        logger.close();

        ksession.dispose();

    }

    public void registerPackages(Collection<KnowledgePackage> packages) {
        kbase.addKnowledgePackages(packages);
    }

}
