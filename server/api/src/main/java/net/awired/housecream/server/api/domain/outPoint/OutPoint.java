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
package net.awired.housecream.server.api.domain.outPoint;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import net.awired.housecream.server.api.domain.Point;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@Entity
@NamedQueries({ @NamedQuery(name = OutPoint.QUERY_BY_ZONE, //
query = "SELECT i FROM OutPoint i where zoneId = :" + OutPoint.QUERY_PARAM_ZONE_ID) })
public class OutPoint extends Point {

    private static final long serialVersionUID = 1L;

    public static final String QUERY_PARAM_ZONE_ID = "QUERY_PARAM_ZONE_ID";
    public static final String QUERY_BY_ZONE = "QUERY_PARAM_ZONE_ID";

    private OutPointType type;

    public OutPointType getType() {
        return type;
    }

    public void setType(OutPointType type) {
        this.type = type;
    }

}
