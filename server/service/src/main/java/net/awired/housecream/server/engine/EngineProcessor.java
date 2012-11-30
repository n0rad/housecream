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

    public EngineProcessor() {
        //        System.setProperty("drools.assertBehaviour", "identity");
        //        drools.consequenceExceptionHandler = <qualified class name>
    }

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

    @SuppressWarnings("unchecked")
    private static <T> Collection<T> cast(Collection<?> p, Class<T> t) {
        return (Collection<T>) p;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Event event = exchange.getIn().getBody(Event.class);
        log.debug("Asking to processing event : {}", event);
        logCurrentFacts();
        log.debug("Start processing event : {}", event);
        FactHandle eventFact = ksession.insert(event);
        try {
            ksession.fireAllRules();
            Collection<FactHandle> consequenceHandlers = cast(
                    ksession.getFactHandles(new ClassObjectFilter(ConsequenceAction.class)), FactHandle.class);
            Actions actions = new Actions();
            for (FactHandle consequenceHandler : consequenceHandlers) {
                Action action = new Action((ConsequenceAction) ksession.getObject(consequenceHandler));
                actions.add(action);
                ksession.insert(action);
                ksession.retract(consequenceHandler);
            }
            exchange.getIn().setBody(actions);
            logCurrentFacts();
        } finally {
            ksession.retract(eventFact);
            setPointState(event.getPointId(), event.getValue());
        }
    }

    public void registerPackages(Collection<KnowledgePackage> packages) {
        kbase.addKnowledgePackages(packages);
    }

    public void setPointState(long pointId, Float currentValue) {
        log.debug("Set point state pointId={}, value={}", pointId, currentValue);
        PointState state = new PointState(pointId, currentValue);
        //TODO may be a problem as we insert before remove
        FactHandle factHandler = ksession.insert(state);
        Pair<PointState, FactHandle> previous = states.put(state.getPointId(), new Pair<PointState, FactHandle>(
                state, factHandler));
        if (previous != null) {
            ksession.retract(previous.right);
        }
        logCurrentFacts();
    }

    public float getPointState(long pointId) throws NotFoundException {
        Pair<PointState, FactHandle> pair = states.get(pointId);
        if (pair == null) {
            throw new NotFoundException("Point state not found for pointId : " + pointId);
        }
        return pair.left.getValue();
    }

    public void removePointState(long pointId) {
        log.debug("Removing point state for id : {}" + pointId);
        Pair<PointState, FactHandle> pair = states.get(pointId);
        if (pair != null) {
            ksession.retract(pair.right);
        }
        states.remove(pointId);
        logCurrentFacts();
    }

    public boolean findAndRemoveActionFromFacts(Action action) {
        log.debug("Removing action from facts : {}", action);
        FactHandle factHandle = ksession.getFactHandle(action);
        ksession.retract(factHandle);
        logCurrentFacts();
        return factHandle != null;
    }

    private void logCurrentFacts() {
        if (!log.isDebugEnabled()) {
            return;
        }
        for (long key : states.keySet()) {
            log.debug("Point state : " + states.get(key).left);
        }
        Collection<Object> objects = ksession.getObjects();
        for (Object object : objects) {
            log.debug("Fact : " + object);
        }
    }
}
