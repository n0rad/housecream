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
package org.housecream.server.api.domain.zone;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import lombok.Data;
import org.housecream.server.api.domain.CoordinateShape;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@XmlRootElement(name = "zone")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlSeeAlso({ Area.class, Building.class, Floor.class, Land.class, Room.class, Field.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = Area.class, name = Area.ZONE_TYPE_NAME),
        @JsonSubTypes.Type(value = Building.class, name = Building.ZONE_TYPE_NAME),
        @JsonSubTypes.Type(value = Floor.class, name = Floor.ZONE_TYPE_NAME),
        @JsonSubTypes.Type(value = Land.class, name = Land.ZONE_TYPE_NAME),
        @JsonSubTypes.Type(value = Room.class, name = Room.ZONE_TYPE_NAME),
        @JsonSubTypes.Type(value = Field.class, name = Field.ZONE_TYPE_NAME) })
public abstract class Zone {

    private static final long serialVersionUID = 42L;

    @Id
    private UUID id;

    @Column
    @NotNull
    @Size(min = 1, max = 20)
    private String name;

    private Long parentId;

    private String imageMime;
    @Lob
    private byte[] image;

    private CoordinateShape parentZoneCoordinatesShape;
    private String parentZoneCoordinates;

    // TODO   private Integer ordering;

}
