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
package net.awired.housecream.server.service;

import net.awired.core.lang.exception.NotFoundException;
import net.awired.housecream.server.api.domain.rule.EventRule;
import net.awired.housecream.server.api.resource.RuleResource;
import net.awired.housecream.server.storage.dao.RuleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class RuleService implements RuleResource {

    @Autowired
    private RuleDao ruleDao;

    @Override
    public void deleteRule(long ruleId) {
        ruleDao.delete(ruleId);
    }

    @Override
    public EventRule getRule(long ruleId) throws NotFoundException {
        return ruleDao.find(ruleId);
    }
}
