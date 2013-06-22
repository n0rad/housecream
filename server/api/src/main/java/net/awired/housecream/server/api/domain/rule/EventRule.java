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
package net.awired.housecream.server.api.domain.rule;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import net.awired.housecream.server.api.domain.IdEntityImpl;

@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class EventRule extends IdEntityImpl<Long> {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(unique = true)
    private String name;

    private Integer salience;

    @Valid
    private List<Condition> conditions = new ArrayList<>();

    @Valid
    private List<Consequence> consequences = new ArrayList<>();

}
