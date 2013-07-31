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
package org.housecream.server.engine;

import java.util.ArrayList;
import java.util.List;
import com.google.common.base.Objects;

public class Actions {

    private final List<Action> actions = new ArrayList<Action>();

    @Override
    public String toString() {
        return Objects.toStringHelper(this) //
                .add("actions", actions) //
                .toString();
    }

    public void add(Action action) {
        actions.add(action);
    }

    public List<Action> getActions() {
        return actions;
    }

}