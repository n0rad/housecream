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
package net.awired.housecream.server.engine;

import java.util.Date;
import net.awired.housecream.server.api.domain.rule.Consequence;
import net.awired.housecream.server.api.domain.rule.TriggerType;

public class Action extends Consequence {

    private Date creation;
    private Integer ruleSource;

    public Action(long outPointId, float value, long delayMili, TriggerType trigger) {
        super(outPointId, value, delayMili, trigger);
    }

    public Action() {
        creation = new Date();

    }

    public Action(ConsequenceAction consequenceAction) {
        this(consequenceAction.getOutPointId(), consequenceAction.getValue(), consequenceAction.getDelayMili(),
                consequenceAction.getTriggerType());
    }

    public Date getCreation() {
        return creation;
    }

    public void setCreation(Date creation) {
        this.creation = creation;
    }

    public Integer getRuleSource() {
        return ruleSource;
    }

    public void setRuleSource(Integer ruleSource) {
        this.ruleSource = ruleSource;
    }
}
