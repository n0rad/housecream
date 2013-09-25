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
package org.housecream.server.storage.dao;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.housecream.server.api.domain.rule.ConditionType.event;
import static org.housecream.server.it.builder.RuleBuilder.rule;
import java.util.UUID;
import org.housecream.server.api.domain.rule.Condition;
import org.housecream.server.api.domain.rule.Consequence;
import org.housecream.server.api.domain.rule.Rule;
import org.junit.Test;

public class RuleDaoTest {

    @org.junit.Rule
    public CassandraDaoRule<RuleDao> cassandra = new CassandraDaoRule<>(RuleDao.class);

    @Test
    public void should_save_rule() throws Exception {
        Rule rule = rule().name("rule1").addCondition(new Condition(UUID.randomUUID(), 1f, event))
                .addConsequence(new Consequence(UUID.randomUUID(), 1f)).build();

        cassandra.dao().save(rule);

        Rule savedRule = cassandra.dao().find(rule.getId());
        assertThat(savedRule).isEqualTo(rule);
    }
}
