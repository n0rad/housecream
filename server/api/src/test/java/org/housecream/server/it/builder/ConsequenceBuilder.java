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

import org.housecream.server.api.domain.outPoint.OutPoint;
import org.housecream.server.api.domain.rule.Consequence;
import org.housecream.server.api.domain.rule.TriggerType;

public class ConsequenceBuilder {

    public static Consequence consequence(OutPoint light, float value) {
        Consequence consequence = new Consequence();
        consequence.setOutPointId(light.getId());
        consequence.setValue(value);
        return consequence;
    }

    public static Consequence consequence(OutPoint light, int value, int delayMs) {
        Consequence consequence = new Consequence();
        consequence.setOutPointId(light.getId());
        consequence.setValue(value);
        consequence.setDelayMili(delayMs);
        return consequence;
    }

    public static Consequence consequence(OutPoint light, int value, int delayMs, TriggerType nonRetrigger) {
        Consequence consequence = new Consequence();
        consequence.setOutPointId(light.getId());
        consequence.setValue(value);
        consequence.setDelayMili(delayMs);
        consequence.setTriggerType(nonRetrigger);
        return consequence;
    }

}
