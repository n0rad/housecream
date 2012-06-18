package net.awired.housecream.server.core.OLD.engine;

import java.util.Collection;
import net.awired.housecream.server.core.OLD.rule.RuleRunner;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.event.rule.DebugAgendaEventListener;
import org.drools.event.rule.DebugWorkingMemoryEventListener;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;

public class EventEngine {

    public static void main(String[] args) {

        Object[] facts = { AlarmMode.NORMAL, new PointStat(1, 1f), new PointStat(2, 1f) };
        String[] rules = new String[] { "/net/awired/housecream/server/core/engine/hcs.drl" };

        //

        final KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

        for (String ruleFile : rules) {
            System.out.println("Loading file: " + ruleFile);
            kbuilder.add(ResourceFactory.newClassPathResource(ruleFile, RuleRunner.class), ResourceType.DRL);
        }

        // Check the builder for errors
        if (kbuilder.hasErrors()) {
            System.out.println(kbuilder.getErrors().toString());
            throw new RuntimeException("Unable to compile \"HelloWorld.drl\".");
        }

        // get the compiled packages (which are serializable)
        final Collection<KnowledgePackage> pkgs = kbuilder.getKnowledgePackages();

        // add the packages to a knowledgebase (deploy the knowledge packages).
        final KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(pkgs);

        final StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

        ksession.addEventListener(new DebugAgendaEventListener());
        ksession.addEventListener(new DebugWorkingMemoryEventListener());

        // setup the audit logging
        KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "hcs.droolsLogs");

        for (Object fact : facts) {
            System.out.println("Inserting fact: " + fact);
            ksession.insert(fact);
        }

        ksession.fireAllRules();

        logger.close();

        ksession.dispose();

    }
}
