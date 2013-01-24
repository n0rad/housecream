package net.awired.housecream.server.engine;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import net.awired.ajsl.core.lang.Pair;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.housecream.server.api.domain.Event;
import net.awired.housecream.server.api.domain.PointState;
import net.awired.housecream.server.engine.builder.RuleBuilder;
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

    @Inject
    private RuleBuilder ruleBuilder;

    public EngineProcessor() {
        //        System.setProperty("drools.assertBehaviour", "identity");
        //        drools.consequenceExceptionHandler = <qualified class name>
    }

    @PostConstruct
    protected void postConstruct() {
        ksession.addEventListener(new DebugAgendaEventListener());
        ksession.addEventListener(new DebugWorkingMemoryEventListener());
        registerPackages(ruleBuilder.getOutEventRule());
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
        Object event = exchange.getIn().getBody(Object.class);
        log.debug("Asking to processing event : {}", event);
        logCurrentFacts("before fire rules");
        log.debug("Start processing event : {}", event);
        FactHandle eventFact = ksession.insert(event);
        try {
            ksession.fireAllRules();
            Collection<FactHandle> consequenceHandlers = cast(
                    ksession.getFactHandles(new ClassObjectFilter(ConsequenceAction.class)), FactHandle.class);
            Actions actions = new Actions();
            for (FactHandle consequenceHandler : consequenceHandlers) {
                ConsequenceAction consequence = (ConsequenceAction) ksession.getObject(consequenceHandler);
                if (consequence.getDelayMili() == 0) {
                    try {
                        float currentState = getPointState(consequence.getOutPointId());
                        if (currentState == consequence.getValue()) {
                            log.debug("Skip direct action {} as the point already have this state", consequence);
                            ksession.retract(consequenceHandler);
                            continue;
                        }
                    } catch (NotFoundException e) {
                        // nothing to do if there is no point state
                    }
                }
                Action action = new Action(consequence);
                actions.add(action);
                ksession.insert(action);
                ksession.retract(consequenceHandler);
            }
            exchange.getIn().setBody(actions);
            logCurrentFacts("after fired rules");
        } finally {
            ksession.retract(eventFact);
            // TODO sux
            if (event instanceof Event) {
                setPointState(((Event) event).getPointId(), ((Event) event).getValue());
            }
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
        logCurrentFacts("after add point state");
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
        logCurrentFacts("after removing point state");
    }

    public boolean findAndRemoveActionFromFacts(Action action) {
        log.debug("Find and removing action from facts : {}", action);
        FactHandle factHandle = ksession.getFactHandle(action);
        if (factHandle != null) {
            ksession.retract(factHandle);
        }
        logCurrentFacts("after find and remove");
        return factHandle != null;
    }

    private void logCurrentFacts(String info) {
        if (!log.isDebugEnabled()) {
            return;
        }
        log.debug("FACTS: " + info);
        for (long key : states.keySet()) {
            log.debug("Point state : " + states.get(key).left);
        }
        Collection<Object> objects = ksession.getObjects();
        for (Object object : objects) {
            log.debug("Fact : " + object);
        }
    }
}
