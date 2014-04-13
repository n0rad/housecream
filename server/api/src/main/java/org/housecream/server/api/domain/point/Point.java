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
package org.housecream.server.api.domain.point;

import java.net.URI;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Point {

    private UUID id;

    private PointType type;

    @NotNull
    @Size(min = 1, max = 20)
    private String name;

    @Size(min = 0, max = 100)
    private String description;

    @NotNull
    private URI uri;

    @XmlTransient
    private Float value;
}

// group
// attributes

// valeur temporelle (default is now)

// valeur sortie constante
//valeur sortie one shot (+ duree)
//vleur sortie repetitive (+ frequence)


// entretien de la voiture
// tondeuse -> manual - entier - h - /// h % 24 -> todoo -> entretien tondeuse
// todo ?
// audio text (temp)
// audio in -> command
// mail -- in out
// xmpp -- in out
//
