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
package net.awired.housecream.server.storage.dao;

import java.util.List;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.ajsl.persistence.dao.impl.GenericDaoImpl;
import net.awired.housecream.server.api.domain.rule.Condition;
import net.awired.housecream.server.api.domain.rule.Consequence;
import net.awired.housecream.server.api.domain.rule.EventRule;
import org.springframework.stereotype.Repository;

@Repository
public class RuleDao extends GenericDaoImpl<EventRule, Long> {

    public RuleDao() {
        super(EventRule.class, Long.class);
    }

    @Override
    public EventRule find(Long id) throws NotFoundException {
        EventRule find = super.find(id);
        for (@SuppressWarnings("unused")
        Condition condition : find.getConditions()) {
        }
        for (@SuppressWarnings("unused")
        Consequence consequence : find.getConsequences()) {
        }
        return find;
    }

    @Override
    public List<EventRule> findAll() {
        List<EventRule> findList = super.findAll();
        for (EventRule eventRule : findList) {
            for (@SuppressWarnings("unused")
            Condition condition : eventRule.getConditions()) {
            }
            for (@SuppressWarnings("unused")
            Consequence consequence : eventRule.getConsequences()) {
            }
        }
        return findList;
    }
}
