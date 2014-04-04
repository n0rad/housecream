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
package org.housecream.server.resource;

import java.util.UUID;
import org.housecream.server.api.domain.rule.Rule;
import org.housecream.server.api.resource.RulesResource.RuleResource;
import org.housecream.server.application.JaxRsResource;
import org.housecream.server.storage.dao.RuleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import fr.norad.core.lang.exception.NotFoundException;

@JaxRsResource
@Validated
public class RuleResourceImpl implements RuleResource {

    @Autowired
    private RuleDao ruleDao;

    @Override
    public void deleteRule(UUID ruleId) {
        ruleDao.delete(ruleId);
    }

    @Override
    public Rule getRule(UUID ruleId) throws NotFoundException {
        return ruleDao.find(ruleId);
    }
}
