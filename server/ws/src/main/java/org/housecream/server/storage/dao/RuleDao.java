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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.housecream.server.api.domain.rule.EventRule;
import org.springframework.stereotype.Repository;

@Repository
public class RuleDao {

    public void delete(UUID ruleId) {
        // TODO Auto-generated method stub

    }

    public void save(EventRule rule) {
        // TODO Auto-generated method stub

    }

    public EventRule find(UUID ruleId) {
        return null;
    }

    public List<EventRule> findAll() {
        // TODO Auto-generated method stub
        return new ArrayList<>();
    }

    public void deleteAll() {
        // TODO Auto-generated method stub

    }

}
