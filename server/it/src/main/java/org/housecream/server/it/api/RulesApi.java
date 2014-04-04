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
package org.housecream.server.it.api;

import java.util.List;
import org.housecream.server.api.domain.rule.Condition;
import org.housecream.server.api.domain.rule.Consequence;
import org.housecream.server.api.domain.rule.Rule;
import org.housecream.server.api.resource.RulesResource;
import org.housecream.server.it.ItSession;
import org.housecream.server.it.builder.RuleBuilder;

public class RulesApi {

    private final ItSession session;

    public RulesApi(ItSession session) {
        this.session = session;
    }

    public Rule create(String name, Condition condition, Consequence consequence) {
        Rule rule = session
                .getServer()
                .getResource(RulesResource.class, session)
                .createRule(RuleBuilder.rule().name(name).addCondition(condition).addConsequence(consequence).build());
        return rule;
    }

    public Rule create(String name, List<Condition> conditions, Consequence consequence) {
        Rule rule = session.getServer().getResource(RulesResource.class, session)
                .createRule(RuleBuilder.rule().name(name) //
                        .conditions(conditions).addConsequence(consequence).build());
        return rule;
    }

    public Rule create(String name, Condition condition, List<Consequence> consequences) {
        Rule rule = session.getServer().getResource(RulesResource.class, session)
                .createRule(RuleBuilder.rule().name(name) //
                        .addCondition(condition).consequences(consequences).build());
        return rule;
    }

    public void deleteAll() {
        session.getServer().getResource(RulesResource.class, session).deleteAllRules();
    }
}
