package net.awired.housecream.server.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import net.awired.housecream.server.api.domain.Event;
import net.awired.housecream.server.api.domain.Point;
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

    @Inject
    private StateHolder stateHolder;

    private KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
    private final StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
    private KnowledgeRuntimeLogger logger;

    @PostConstruct
    protected void postConstruct() {
        logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "hcs.droolsLogs");

        ksession.addEventListener(new DebugAgendaEventListener());
        ksession.addEventListener(new DebugWorkingMemoryEventListener());

    }

    @PreDestroy
    protected void preDestroy() {
        logger.close();
        ksession.dispose();
    }

    public void registerPoint(Point point) {
        //TODO IT
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Event body = exchange.getIn().getBody(Event.class);

        List<FactHandle> factHandlers = new ArrayList<FactHandle>();
        for (Object fact : stateHolder.getFacts()) {
            log.debug("Inserting fact: " + fact);
            factHandlers.add(ksession.insert(fact));
        }
        FactHandle eventFact = ksession.insert(body);

        ksession.fireAllRules();

        Collection objects = ksession.getObjects(new ClassObjectFilter(ConsequenceAction.class));
        Actions actions = new Actions();
        actions.getActions().addAll(objects);
        exchange.getIn().setBody(actions);

        ksession.retract(eventFact);
        for (FactHandle fh : factHandlers) {
            ksession.retract(fh);
        }

        stateHolder.setState(body.getPointId(), body.getValue());
    }

    public void registerPackages(Collection<KnowledgePackage> packages) {
        kbase.addKnowledgePackages(packages);
    }

}
