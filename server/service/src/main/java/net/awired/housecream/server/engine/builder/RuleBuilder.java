package net.awired.housecream.server.engine.builder;

import java.util.Collection;
import net.awired.housecream.server.common.domain.rule.Condition;
import net.awired.housecream.server.common.domain.rule.Consequence;
import net.awired.housecream.server.common.domain.rule.EventRule;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RuleBuilder {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static final String RULE_PACKAGE = "net.awired.housecream.server.service.rule";

    private static final String[] IMPORTS = new String[] { "net.awired.housecream.server.engine.Event" };

    //package net.awired.housecream.server.core.engine
    //
    //rule "light on"  
    //    when
    //        PointStat(PointId == 1, value == 1)
    //        PointStat(PointId == 2, value == 0)
    //    then
    //        System.out.println("turn on the light 42");
    //end

    public Collection<KnowledgePackage> build(EventRule rule) {
        final KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

        String generateDrl = generateDrl(rule);

        log.debug("building rule\n" + generateDrl);
        kbuilder.add(ResourceFactory.newByteArrayResource(generateDrl.getBytes()), ResourceType.DRL);
        if (kbuilder.hasErrors()) {
            String error = "Unable to compile rule : " + kbuilder.getErrors().toString() + " for rule " + generateDrl;
            kbuilder.undo();
            throw new RuntimeException(error);
        }
        return kbuilder.getKnowledgePackages();
    }

    private String generateDrl(EventRule rule) {
        StringBuilder builder = new StringBuilder(300);
        builder.append("package ");
        builder.append(RULE_PACKAGE);
        builder.append(";\n\n");
        for (String importClass : IMPORTS) {
            builder.append("import ");
            builder.append(importClass);
            builder.append(";\n");
        }

        builder.append("\nrule \"");
        builder.append(rule.getName());
        builder.append("\"\n    when\n");
        for (Condition condition : rule.getConditions()) {
            builder.append("Event(pointId == ");
            builder.append(condition.getPointId());
            builder.append(", value == ");
            builder.append(condition.getValue());
            builder.append(")");
            builder.append('\n');
        }

        builder.append("    then\n");
        for (Consequence consequence : rule.getConsequences()) {
            builder.append("System.out.println(\"consequence : " + consequence.getOutPointId()
                    + " will be at state : " + consequence.getValue() + "\");");
            builder.append('\n');
        }
        //consequences
        builder.append("end\n");
        return builder.toString();
    }
}
