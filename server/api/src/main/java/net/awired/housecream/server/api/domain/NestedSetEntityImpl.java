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
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import lombok.Data;

@Data
@MappedSuperclass
@XmlAccessorType(XmlAccessType.NONE)
public abstract class NestedSetEntityImpl<KEY_TYPE extends Serializable> implements NestedSet<KEY_TYPE> {

    @Id
    @GeneratedValue
    protected Long id;

    @Column(nullable = true)
    protected Long parentId;
    protected Long threadId; // null on root
    protected Long left;
    protected Long right;

    ///////////////////////////////////////////////////////////////////////////////

    @Override
    @XmlElement
    public Long getLeft() {
        return left;
    }

    @Override
    @XmlElement
    public Long getRight() {
        return right;
    }

}
