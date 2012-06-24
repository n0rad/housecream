package net.awired.housecream.server.core.engine;

import java.util.Collection;
import javax.inject.Inject;
import net.awired.housecream.server.common.domain.Point;
import net.awired.housecream.server.core.OLD.engine.AlarmMode;
import net.awired.housecream.server.core.OLD.engine.PointStat;
import net.awired.housecream.server.core.domain.HcEvent;
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
import org.springframework.stereotype.Component;

@Component
public class EngineProcessor implements Processor {

    private KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();

    @Inject
    private StateHolder stateHolder;

    public void registerPoint(Point point) {

    }

    @Override
    public void process(Exchange exchange) throws Exception {
        HcEvent body = exchange.getIn().getBody(HcEvent.class);

        Object[] facts = { AlarmMode.NORMAL, new PointStat(1, 1f), new PointStat(2, 1f) };

        final StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        ksession.addEventListener(new DebugAgendaEventListener());
        ksession.addEventListener(new DebugWorkingMemoryEventListener());

        // setup the audit logging
        KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "hcs.droolsLogs");

        for (Object fact : facts) {
            System.out.println("Inserting fact: " + fact);
            FactHandle insert = ksession.insert(fact);
        }

        ksession.fireAllRules();

        logger.close();

        ksession.dispose();

    }

    public void registerPackages(Collection<KnowledgePackage> packages) {
        kbase.addKnowledgePackages(packages);
    }

}
