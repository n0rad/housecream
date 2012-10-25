package net.awired.housecream.server.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import net.awired.housecream.server.api.domain.Event;
import net.awired.housecream.server.api.domain.Point;
import net.awired.housecream.server.service.event.EventService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.definition.KnowledgePackage;
import org.drools.event.rule.DebugAgendaEventListener;
import org.drools.event.rule.DebugWorkingMemoryEventListener;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.ClassObjectFilter;
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

    @Inject
    private EventService eventService;

    public void registerPoint(Point point) {
        //TODO IT
    }

    @Override
    public void process(Exchange exchange) throws Exception {

        Event body = exchange.getIn().getBody(Event.class);
        eventService.saveEventAsync(body);

        final StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        ksession.addEventListener(new DebugAgendaEventListener());
        ksession.addEventListener(new DebugWorkingMemoryEventListener());

        // setup the audit logging
        KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "hcs.droolsLogs");

        List<FactHandle> factHandlers = new ArrayList<FactHandle>();
        for (Object fact : stateHolder.getFacts()) {
            log.debug("Inserting fact: " + fact);
            factHandlers.add(ksession.insert(fact));
        }
        FactHandle eventFact = ksession.insert(body);
        //        Actions actions = new Actions();
        //        ArrayList<String> camelUrls = new ArrayList<String>();

        //        exchange.getIn().setHeader("outputActions", camelUrls);
        //      FactHandle outputFact = ksession.insert(actions);

        int fireAllRules = ksession.fireAllRules();
        System.out.println("Rule fired : " + fireAllRules);

        Collection objects = ksession.getObjects(new ClassObjectFilter(ConsequenceAction.class));
        Actions body2 = new Actions();
        body2.getActions().addAll(objects);
        exchange.getIn().setBody(body2);

        //        ksession.retract(outputFact);
        ksession.retract(eventFact);
        for (FactHandle fh : factHandlers) {
            ksession.retract(fh);
        }
        Collection<FactHandle> factHandles = ksession.getFactHandles();
        stateHolder.setState(body.getPointId(), body.getValue());

        logger.close();

        ksession.dispose();

    }

    public void registerPackages(Collection<KnowledgePackage> packages) {
        kbase.addKnowledgePackages(packages);
    }

}
