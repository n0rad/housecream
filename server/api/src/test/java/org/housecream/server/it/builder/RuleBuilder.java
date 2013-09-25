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
package org.housecream.server.it.builder;

import java.util.ArrayList;
import java.util.List;
import org.housecream.server.api.domain.rule.Condition;
import org.housecream.server.api.domain.rule.Consequence;
import org.housecream.server.api.domain.rule.Rule;

public class RuleBuilder {

    private String name;
    private List<Consequence> consequences = new ArrayList<Consequence>();
    private List<Condition> conditions = new ArrayList<Condition>();

    public static RuleBuilder rule() {
        return new RuleBuilder();
    }

    public Rule build() {
        Rule eventRule = new Rule();
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
