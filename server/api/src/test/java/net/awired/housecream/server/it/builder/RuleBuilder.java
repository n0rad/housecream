package net.awired.housecream.server.it.builder;

import java.util.ArrayList;
import java.util.List;
import net.awired.housecream.server.api.domain.rule.Condition;
import net.awired.housecream.server.api.domain.rule.Consequence;
import net.awired.housecream.server.api.domain.rule.EventRule;

public class RuleBuilder {

    private String name;
    private List<Consequence> consequences = new ArrayList<Consequence>();
    private List<Condition> conditions = new ArrayList<Condition>();

    public static RuleBuilder rule() {
        return new RuleBuilder();
    }

    public EventRule build() {
        EventRule eventRule = new EventRule();
        eventRule.setName(name);
        if (!consequences.isEmpty()) {
            eventRule.setConsequences(consequences);
        }
        if (!conditions.isEmpty()) {
            eventRule.setConditions(conditions);
        }
        return eventRule;
    }

    public RuleBuilder name(String name) {
        this.name = name;
        return this;
    }

    public RuleBuilder addConsequence(Consequence consequence) {
        consequences.add(consequence);
        return this;
    }

    public RuleBuilder addCondition(Condition condition) {
        conditions.add(condition);
        return this;
    }

    public RuleBuilder conditions(List<Condition> conditions) {
        this.conditions.addAll(conditions);
        return this;
    }

    public RuleBuilder consequences(List<Consequence> consequences) {
        this.consequences.addAll(consequences);
        return this;
    }

}
