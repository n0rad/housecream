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
package net.awired.housecream.server.api.domain.inpoint;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import net.awired.housecream.server.api.domain.Point;

@XmlRootElement(name = "InPoint")
@XmlAccessorType(XmlAccessType.PROPERTY)
@Entity
@NamedQueries({ @NamedQuery(name = InPoint.QUERY_BY_ZONE, //
query = "SELECT i FROM InPoint i where zoneId = :" + InPoint.QUERY_PARAM_ZONE_ID) })
public class InPoint extends Point {

    public static final String QUERY_BY_ZONE = "QUERY_BY_ZONE";
    public static final String QUERY_PARAM_ZONE_ID = "QUERY_PARAM_ZONE_ID";

    //    private Date lastUpdate; // input only

    private static final long serialVersionUID = 1L;

    //    @EnumNotEmpty
    //    @ApiProperty(value = "type of point", allowableValues = "any")
    private InPointType type;

    ////////////////////////////////////

    public void setType(InPointType type) {
        this.type = type;
    }

    //    @ApiProperty(value = "type of point", allowableValues = "any")
    @XmlElement
    public InPointType getType() {
        return type;
    }

}
