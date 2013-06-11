/**
 *
 *     Housecream.org project
 *     Copyright (C) Awired.net
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
