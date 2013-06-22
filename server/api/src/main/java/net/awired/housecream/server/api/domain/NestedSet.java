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
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public interface NestedSet<KEY_TYPE extends Serializable> extends IdEntity<KEY_TYPE> {

    Long getParentId();

    void setParentId(KEY_TYPE parentId);

    Long getThreadId();

    void setThreadId(KEY_TYPE threadId);

    Long getLeft();

    void setLeft(Long left);

    Long getRight();

    void setRight(Long right);

}
