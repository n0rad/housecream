package net.awired.housecream.server.engine.builder;

import java.util.Collection;
import net.awired.housecream.server.api.domain.rule.Condition;
import net.awired.housecream.server.api.domain.rule.ConditionType;
import net.awired.housecream.server.api.domain.rule.Consequence;
import net.awired.housecream.server.api.domain.rule.EventRule;
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

    private static final String[] IMPORTS = new String[] { "net.awired.housecream.server.engine.*",
            "net.awired.housecream.server.api.domain.*", "net.awired.housecream.server.api.domain.rule.*" };

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
            if (condition.getType() == ConditionType.event) {
                builder.append("Event");
            } else if (condition.getType() == ConditionType.state) {
                builder.append("not PointState(pointId == ");
                builder.append(condition.getPointId());
                builder.append(") || PointState");
            } else {
                throw new RuntimeException("unknown condition type " + condition.getType());
            }
            builder.append("(pointId == ");
            builder.append(condition.getPointId());
            builder.append(", value == ");
            builder.append(condition.getValue());
            builder.append(")");
            builder.append('\n');
        }

        for (Consequence consequence : rule.getConsequences()) {
            builder.append("not Consequence(outPointId == (long)");
            builder.append(consequence.getOutPointId());
            builder.append(")\n");
        }

        builder.append("    then\n");
        for (Consequence consequence : rule.getConsequences()) {
            builder.append("insert(new Consequence((long)" + consequence.getOutPointId() + ",(float)"
                    + consequence.getValue() + ", " + consequence.getDelayMili() + ", "
                    + consequence.getTriggerType() + "));\n");
        }
        builder.append("end\n");
        return builder.toString();
    }
}
