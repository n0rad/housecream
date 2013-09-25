/**
 *
 *     Copyright (C) Housecream.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package org.housecream.server.engine.builder;

import java.util.Collection;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.io.ResourceFactory;
import org.housecream.server.api.domain.rule.Condition;
import org.housecream.server.api.domain.rule.ConditionType;
import org.housecream.server.api.domain.rule.Consequence;
import org.housecream.server.api.domain.rule.Rule;
import org.housecream.server.api.domain.rule.TriggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DroolsRuleBuilder {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static final String RULE_PACKAGE = "org.housecream.server.service.rule";

    private static final String[] IMPORTS = new String[] { "java.util.*", //   
            "org.housecream.server.engine.*", //
            "org.housecream.server.api.domain.*", //
            "org.housecream.server.api.domain.rule.*" };

    public Collection<KnowledgePackage> build(Rule rule) {
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

    private String generateDrl(Rule rule) {
        StringBuilder builder = new StringBuilder(300);
        appendHeader(builder, rule);

        builder.append("    when\n");
        appendCondition(builder, rule);

        for (Consequence consequence : rule.getConsequences()) {
            if (consequence.getTriggerType() == TriggerType.NON_RETRIGGER) {
                builder.append("not Action(outPointId == UUID.fromString(\"");
                builder.append(consequence.getOutPointId());
                builder.append("\"))\n");
            } else {
                builder.append("not ConsequenceAction(outPointId == UUID.fromString(\"");
                builder.append(consequence.getOutPointId());
                builder.append("\"))\n");
            }
        }

        builder.append("    then\n");
        for (Consequence consequence : rule.getConsequences()) {
            builder.append("insert(new ConsequenceAction(UUID.fromString(\"" + consequence.getOutPointId()
                    + "\"),(float)" + consequence.getValue() + ", " + consequence.getDelayMili() + ", "
                    + buildTriggerJavaType(consequence.getTriggerType()) + "));\n");
        }
        builder.append("end\n");

        for (Consequence consequence : rule.getConsequences()) {
            if (consequence.getTriggerType() == TriggerType.RETRIGGER) {
                builder.append("\nrule \"" + rule.getName() + "-RETRIGGER" + consequence.getId() + "\"\n");
                builder.append("salience 10\n");
                builder.append("    when\n");

                appendCondition(builder, rule);

                builder.append("$retrigger:");
                builder.append("Action(outPointId == UUID.fromString(\"" + consequence.getOutPointId() + "\"))\n");
                builder.append("    then\n");
                builder.append("retract($retrigger);\n");
                builder.append("end\n");
            }
        }

        return builder.toString();
    }

    private void appendHeader(StringBuilder builder, Rule rule) {
        builder.append("package " + RULE_PACKAGE + ";\n\n");
        for (String importClass : IMPORTS) {
            builder.append("import " + importClass + ";\n");
        }
        builder.append("\nrule \"" + rule.getName() + "\"\n");
        builder.append("salience " + rule.getSalience() + '\n');
    }

    private void appendCondition(StringBuilder builder, Rule rule) {
        for (Condition condition : rule.getConditions()) {
            if (condition.getType() == ConditionType.event) {
                builder.append("Event");
            } else if (condition.getType() == ConditionType.state) {
                builder.append("not PointState(pointId == UUID.fromString(\"");
                builder.append(condition.getPointId());
                builder.append("\")) || PointState");
            } else {
                throw new RuntimeException("unknown condition type " + condition.getType());
            }
            builder.append("(pointId == UUID.fromString(\"");
            builder.append(condition.getPointId());
            builder.append("\"), value == ");
            builder.append(condition.getValue());
            builder.append(")");
            builder.append('\n');
        }
    }

    private String buildTriggerJavaType(TriggerType triggerType) {
        if (triggerType == null) {
            return "null";
        }
        return "TriggerType." + triggerType;
    }

    public Collection<KnowledgePackage> getOutEventRule() {
        final KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

        StringBuilder builder = new StringBuilder(300);
        Rule eventRule = new Rule();
        eventRule.setName("outEvent");
        eventRule.setSalience(1);
        appendHeader(builder, eventRule);

        builder.append("    when\n");
        builder.append("$outEvent : OutEvent()");
        builder.append("    then\n");
        builder.append("insert(new ConsequenceAction($outEvent.getOutPointId(), (float)$outEvent.getValue(), 0, null));");
        builder.append("end\n");
        String generateDrl = builder.toString();

        log.debug("building rule\n" + generateDrl);
        kbuilder.add(ResourceFactory.newByteArrayResource(generateDrl.getBytes()), ResourceType.DRL);
        if (kbuilder.hasErrors()) {
            String error = "Unable to compile rule : " + kbuilder.getErrors().toString() + " for rule " + generateDrl;
            kbuilder.undo();
            throw new RuntimeException(error);
        }
        return kbuilder.getKnowledgePackages();
    }
}
