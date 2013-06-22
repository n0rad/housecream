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
package net.awired.housecream.server.api.domain;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import lombok.Data;

/**
 * @XmlAccessorType is none because jaxb cannot handle Serializable generics
 *                  to serialize id override getId() & setId() with @XmlElement
 */
@Data
@MappedSuperclass
@XmlAccessorType(XmlAccessType.NONE)
public abstract class IdEntityImpl<KEY_TYPE extends Serializable> implements IdEntity<KEY_TYPE> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    protected Long id;

}
