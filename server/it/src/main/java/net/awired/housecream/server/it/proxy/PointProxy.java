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
package net.awired.housecream.server.it.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import net.awired.housecream.server.api.domain.Point;

public class PointProxy<T> extends ProxyClass<T> {

    private final List<Long> points = new ArrayList<Long>();

    public PointProxy(T o) {
        super(o);
    }

    @Override
    protected void handleBefore(Method m, Object[] args) {
    }

    @Override
    protected void handleAfter(Method m, Object[] args) {
    }

    @Override
    protected void handleSuccess(Method m, Object[] args, Object result) {
        if (m.getName().equals("createInPoint")) {
            points.add(((Point) result).getId());
        }
    }

    public List<Long> getPoints() {
        return points;
    }

}
