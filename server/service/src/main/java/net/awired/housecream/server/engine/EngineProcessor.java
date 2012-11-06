package net.awired.housecream.server.engine;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import net.awired.ajsl.core.lang.Pair;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.housecream.server.api.domain.Event;
import net.awired.housecream.server.api.domain.PointState;
import net.awired.housecream.server.api.domain.rule.Consequence;
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

    private final Logger log = LoggerFactory.getLogger(getClass());

    private Map<Long, Pair<PointState, FactHandle>> states = Collections
            .synchronizedMap(new HashMap<Long, Pair<PointState, FactHandle>>());

    private KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
    private final StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
    private KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "hcs.droolsLogs");

    @PostConstruct
    protected void postConstruct() {
        ksession.addEventListener(new DebugAgendaEventListener());
        ksession.addEventListener(new DebugWorkingMemoryEventListener());
    }

    @PreDestroy
    protected void preDestroy() {
        logger.close();
        ksession.dispose();
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Event event = exchange.getIn().getBody(Event.class);
        FactHandle eventFact = ksession.insert(event);
        try {
            ksession.fireAllRules();
            Collection objects = ksession.getObjects(new ClassObjectFilter(Consequence.class));
            Actions actions = new Actions();
            actions.getActions().addAll(objects);
            exchange.getIn().setBody(actions);
        } finally {
            ksession.retract(eventFact);
            setPointState(event.getPointId(), event.getValue());
        }
    }

    public void registerPackages(Collection<KnowledgePackage> packages) {
        kbase.addKnowledgePackages(packages);
    }

    public void setPointState(long pointId, Float currentValue) {
        PointState state = new PointState(pointId, currentValue);
        FactHandle factHandler = ksession.insert(state);
        states.put(state.getPointId(), new Pair<PointState, FactHandle>(state, factHandler));
    }

    public float getPointState(long pointId) throws NotFoundException {
        Pair<PointState, FactHandle> pair = states.get(pointId);
        if (pair == null) {
            throw new NotFoundException("point state not found for pointId : " + pointId);
        }
        return pair.left.getValue();
    }

    public void removePointState(long pointId) {
        Pair<PointState, FactHandle> pair = states.get(pointId);
        if (pair != null) {
            ksession.retract(pair.right);
        }
        states.remove(pointId);
    }

    public boolean removeConsequenceFromState(Consequence consequence) {
        FactHandle factHandle = ksession.getFactHandle(consequence);
        ksession.retract(factHandle);
        return factHandle != null;
    }
}
