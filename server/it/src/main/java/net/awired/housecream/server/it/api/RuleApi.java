package net.awired.housecream.server.it.api;

import java.util.List;
import net.awired.housecream.server.api.domain.rule.Condition;
import net.awired.housecream.server.api.domain.rule.Consequence;
import net.awired.housecream.server.api.domain.rule.EventRule;
import net.awired.housecream.server.api.resource.RulesResource;
import net.awired.housecream.server.it.HcWsItSession;
import net.awired.housecream.server.it.builder.RuleBuilder;

public class RuleApi {

    private final HcWsItSession session;

    public RuleApi(HcWsItSession session) {
        this.session = session;
    }

    public EventRule create(String name, Condition condition, Consequence consequence) {
        EventRule rule = session
                .getServer()
                .getResource(RulesResource.class, session)
                .createRule(RuleBuilder.rule().name(name).addCondition(condition).addConsequence(consequence).build());
        return rule;
    }

    public EventRule create(String name, List<Condition> conditions, Consequence consequence) {
        EventRule rule = session.getServer().getResource(RulesResource.class, session)
                .createRule(RuleBuilder.rule().name(name) //
                        .conditions(conditions).addConsequence(consequence).build());
        return rule;
    }

    public EventRule create(String name, Condition condition, List<Consequence> consequences) {
        EventRule rule = session.getServer().getResource(RulesResource.class, session)
                .createRule(RuleBuilder.rule().name(name) //
                        .addCondition(condition).consequences(consequences).build());
        return rule;
    }

    public void deleteAll() {
        session.getServer().getResource(RulesResource.class, session).deleteAllRules();
    }
}
