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
import net.awired.generic.jpa.entity.IdEntityImpl;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;

@Entity
public class InPointAction extends IdEntityImpl<Long> {

    private static final long serialVersionUID = 1L;

    private InPoint inPoint;
    private OutPoint OutPoint;
    private String action; // should contain a point as its may be the output
                           // point,file,mail,nagios,xmpp, 
                           //    private Order order;
}
