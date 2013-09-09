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
package org.housecream.server.api.domain.rule;

import java.util.UUID;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Consequence {

    private UUID id;
    private UUID outPointId;
    private float value;
    private long delayMili;
    private TriggerType triggerType;

    public Consequence() {
    }

    public Consequence(UUID outPointId, float value) {
        this.outPointId = outPointId;
        this.value = value;
    }

    public Consequence(UUID outPointId, float value, long delayMili) {
        this.outPointId = outPointId;
        this.value = value;
        this.delayMili = delayMili;
    }

    public Consequence(UUID outPointId, float value, long delayMili, TriggerType trigger) {
        this.outPointId = outPointId;
        this.value = value;
        this.delayMili = delayMili;
        this.triggerType = trigger;
    }

}
