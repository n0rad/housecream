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
            "net.awired.housecream.server.api.domain.*" };

    //package net.awired.housecream.server.core.engine
    //
    //rule "light on"  
    //    when
    //        PointStat(PointId == 1, value == 1)
    //        PointStat(PointId == 2, value == 0)
    //    then
    //        System.out.println("turn on the light 42");
    //end

    //rule "my first rule2"
    //    when
    //$a : Actions( )
    //not Event(pointId == 262) || Event(pointId == 262, value == 1.0)
    //PointState(pointId == 143, value == 1.0)
    //    then
    //$a.add(new ConsequenceAction((long)143,(float)0.0));
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
            builder.append("not ConsequenceAction(pointId == (long)");
            builder.append(consequence.getOutPointId());
            builder.append(")\n");
            //            builder.append("not Flag(pointId == " + consequence.getOutPointId() + ")\n");
        }

        builder.append("    then\n");
        for (Consequence consequence : rule.getConsequences()) {
            //            builder.append("$a.add(new ConsequenceAction((long)" + consequence.getOutPointId() + ",(float)"
            //                    + consequence.getValue() + "));\n");

            builder.append("insert(new ConsequenceAction((long)" + consequence.getOutPointId() + ",(float)"
                    + consequence.getValue() + "));\n");
            //            builder.append("System.out.println(\"consequence : " + consequence.getOutPointId()
            //                    + " will be at state : " + consequence.getValue() + "\");");
        }
        // builder.append("modify( $a );");
        //consequences
        builder.append("end\n");
        return builder.toString();
    }
}
